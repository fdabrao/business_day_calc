(ns bdc.core
  (:require [clj-time.core :as time]
            [clj-time.local :as l]
            [clj-time.format :as f]))

(def business-work  {:monday
                     {:include true
                      :sched ["08:00" "18:00"]}
                     
                     :tuesday
                     {:include true
                      :sched ["08:00" "18:00"]}
                     
                     :wednesday
                     {:include true
                      :sched ["08:00" "18:00"]}
                     
                     :thursday
                     {:include true
                      :sched ["08:00" "18:00"]}
                     
                     :friday
                     {:include true
                      :sched ["08:00" "18:00"]}
                     
                     :saturday
                     {:include false
                      :sched ["08:00" "12:00"]}
                     
                     :sunday
                     {:include false
                      :sched ["09:00" "18:00"]}})

(defn date->weekday [date]
  (case (time/day-of-week date)
    1 :monday
    2 :tuesday
    3 :wednesday
    4 :thursday
    5 :friday
    6 :saturday 
    7 :sunday))

(defn date->work-interval [date start finish]
  (let [dt (f/unparse (f/formatter "dd/MM/yyyy") date)]
    [(f/parse (f/formatter "dd/MM/yyyy HH:mm") (str dt " " start))
     (f/parse (f/formatter "dd/MM/yyyy HH:mm") (str dt " " finish))]))

(defn in-interval? [date start end]
  (and (or (time/equal? date start) (time/after? date start))
       (or (time/equal? date end) (time/before? date end))))

(defn after-interval? [date end]
  (time/after? date end))

(defn str->datetime [str-data]
  (f/parse (f/formatter "dd/MM/yyyy HH:mm") str-data))

(defn next-day [data]
  (time/plus data (time/days 1)))

(defn next-day-midnight [date]
  (let [day (f/unparse (f/formatter "dd/MM/yyyy") date)
        mid-night (f/parse (f/formatter "dd/MM/yyyy HH:mm") (str day " 00:00"))]
    (time/plus mid-night (time/days 1))))

(defn dates-minutes [start end]
  (time/in-minutes (time/interval start end)))

(defn add-business-date-minutes [date minutes work-schedule]
  (let [{include? :include [start end] :sched} ((date->weekday date) work-schedule)
        [start-work end-work] (date->work-interval date start end)]
    (if (not include?)
      (recur (next-day-midnight date) minutes work-schedule)
      (if (after-interval? date end-work)
        (recur (next-day start-work) minutes work-schedule)
        (if (in-interval? date start-work end-work)
          (if (time/after? (.plusMinutes date minutes) end-work)
            (recur
             (next-day start-work)
             (- minutes (dates-minutes date end-work))
             work-schedule)
            (.plusMinutes date minutes))
          (recur start-work minutes work-schedule))))))

(comment

  (add-business-date-minutes (str->datetime "11/08/2017 09:05") 4320 business-work)
  (add-business-date-minutes (str->datetime "11/08/2017 17:00") 120 business-work))

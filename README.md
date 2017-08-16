# Clojure Business Day Calculator

Simple library for end date calculation. Add minutes to start date considering defined work schedule.

# How it works

Configure your business days schedule into `def` or map and pass it to main function:

```
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
```
 You must include all the days and define the days you want to include for calculation.
 ```
 {:monday {:include true :sched ["08:00" "18:00"]}
  :tuesday {:include true :sched ["08:00" "18:00"]}
  ...
  :saturday {:include false :sched ["08:00" "18:00"]}
  :sunday {:include false :sched ["08:00" "18:00"]}
 }
 ```
 If configuration is `:include false`, the schedule will be ignored, so anything will be accepted.

Add 2 hours:
 ```
user> (require '[bdc.core :as bdc])
user> (bdc/add-business-date-minutes (bdc/str->datetime "11/08/2017 09:00") 120 bdc/business-work)
#object[org.joda.time.DateTime 0x2ef0e353 "2017-08-11T11:00:00.000Z"]
```

Add 72 hours:
```
user> (bdc/add-business-date-minutes (bdc/str->datetime "11/08/2017 09:00") 4320 bdc/business-work)
#object[org.joda.time.DateTime 0x1bfc2e9a "2017-08-22T11:00:00.000Z"]
user> 
 ```

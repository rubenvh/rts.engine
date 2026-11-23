(ns rts.domain.economy 
  (:require
    [rts.domain.time :refer [ticks-per-day]]))

(def yields {:spring 1 :summer 2 :autumn 1 :winter 0})
(def units-fed-per-forager 3)
(def food-per-unit (max 1 (quot ticks-per-day units-fed-per-forager)))
(defn food-consumption [units] (* food-per-unit (count units)))
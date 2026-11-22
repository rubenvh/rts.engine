(ns rts.domain.time)

(def ticks-per-day 100)
(def days-per-season 75)
(def seasons [:spring :summer :autumn :winter])

(defn day-of [tick]
  (quot tick ticks-per-day))

(defn season-of [tick]
  (seasons (mod (quot (day-of tick) days-per-season) (count seasons))))
(ns rts.domain.time)

(def ticks-per-day 10)
(def days-per-season 10)
(def seasons [:spring :summer :autumn :winter])

(defn day-of [tick]
  (quot tick ticks-per-day))

(defn season-of [tick]
  (seasons (mod (quot (day-of tick) days-per-season) (count seasons))))
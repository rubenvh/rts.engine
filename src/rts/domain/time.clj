(ns rts.domain.time)

(def ticks-per-day 10)
(def days-per-season 10)
(def ticks-per-season (* ticks-per-day days-per-season))

(def seasons [:spring :summer :autumn :winter])

(defn day-of [tick]
  (quot tick ticks-per-day))

(defn season-of [tick]
 (seasons (mod (quot tick ticks-per-season) (count seasons))))

(defn season-progress [tick]
  (/ (mod tick ticks-per-season) ticks-per-season))
(ns rts.adapters.render
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [rts.application.commands :refer [handle]]
            [rts.domain.grid :refer [coords terrain-at]]
            [rts.domain.time :refer [day-of season-of season-progress
                                     ticks-per-season ticks-per-year]]
            [rts.domain.world :refer [initial-state]]))

(def tile-size 24)
(def status-bar-height 20)

(def palette {:grass {:spring [120 200 80]
                      :summer [90 160 60]
                      :autumn [180 150 80]
                      :winter [200 205 190]}
              :forest {:spring [70 140 70]
                       :summer [40 100 45]
                       :autumn [160 80 35]
                       :winter [85 70 60]}
              :water {:spring [70 140 200]
                      :summer [40 110 180]
                      :autumn [90 110 140]
                      :winter [170 200 225]}})

(defn blend [color1 color2 ratio]
  (mapv (fn [a b] (+ a (* ratio (- b a)))) color1 color2))

(defn terrain-color [terrain tick]
  (let [half-season-ago (mod (- tick (quot ticks-per-season 2)) ticks-per-year)
        season (season-of half-season-ago)
        next-season (season-of (+ ticks-per-season half-season-ago))
        terrain-set (terrain palette)]
    (blend (season terrain-set) (next-season terrain-set) (season-progress half-season-ago))))

(defn status-text [state]
  (let [{:keys [resources]} state
        tick (get-in state [:time :tick] 0)]
    (str "food: " (:food resources) " | day " (inc (day-of tick)) ", " (name (season-of tick)))))

(defn draw-bar [state]
  (let [{:keys [grid]} state
        {:keys [width height]} grid]
    (q/fill 0)
    (q/rect 0 (* height tile-size) (* width tile-size) status-bar-height)
    (q/fill 255)
    (q/text (status-text state) 10 (+ 15 (* height tile-size)))))

(defn draw-world [state]
  (let [{:keys [units grid]} state
        {:keys [width height]} grid]
    (doseq [[x y] (coords width height)
            :let [t (terrain-at grid [x y])
                  [r g b] (terrain-color t (get-in state [:time :tick] 0))]]
      (q/fill r g b)
      (q/rect (* x tile-size) (* y tile-size) tile-size tile-size))
    (q/fill 189 88 184)
    (doseq [{:keys [position]} (vals units)
            :let [[x y] position]]
      (q/ellipse (+ (/ tile-size 2) (* x tile-size)) (+ (/ tile-size 2) (* y tile-size)) tile-size tile-size))
    (draw-bar state)))

(defn setup []
  (q/no-stroke)
  (q/frame-rate 1))

(defn -main [& _]
  (let [{:keys [width height]} (:grid initial-state)]
    (q/sketch
     :title "rts.engine"
     :size [(* tile-size width) (+ (* tile-size height) status-bar-height)]
     :middleware [m/fun-mode]
     :setup  (fn [] (setup) initial-state)
     :update (fn [state] (:state (handle state  {:cmd :tick})))
     :draw draw-world)))
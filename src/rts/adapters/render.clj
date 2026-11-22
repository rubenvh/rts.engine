(ns rts.adapters.render
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [rts.application.commands :refer [handle]]
            [rts.domain.grid :refer [coords terrain-at]]
            [rts.domain.time :refer [day-of season-of]]
            [rts.domain.world :refer [initial-state]]))

(def tile-size 24)
(def status-bar-height 20)

(defn terrain-color [terrain]
  (case terrain
    :grass [124 246 14]
    :water [23 167 246]
    :forest [49 112 38]))

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
                  [r g b] (terrain-color t)]]
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
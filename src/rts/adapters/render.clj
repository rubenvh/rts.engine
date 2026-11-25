(ns rts.adapters.render
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [rts.application.commands :refer [handle]]
            [rts.domain.grid :refer [coords terrain-at]]
            [rts.domain.state :refer [unit-at]]
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

(defn pixel->tile [[x y]]
  [(quot x tile-size) (quot y tile-size)])

(defn draw-bar [app-state]
  (let [game-state (:game app-state)
        {:keys [grid]} game-state
        {:keys [width height]} grid]
    (q/fill 0)
    (q/rect 0 (* height tile-size) (* width tile-size) status-bar-height)
    (q/fill 255)
    (q/text (status-text game-state) 10 (+ 15 (* height tile-size)))))

(defn draw-on-tile [draw-fn x y]
  (draw-fn (* x tile-size) (* y tile-size) tile-size tile-size))

(defn draw-world [app-state]
  (let [game-state (:game app-state)
        {:keys [units grid]} game-state
        {:keys [width height]} grid]
    (doseq [[x y] (coords width height)
            :let [t (terrain-at grid [x y])
                  [r g b] (terrain-color t (get-in game-state [:time :tick] 0))]]
      (q/fill r g b)
      (draw-on-tile q/rect x y))
    (q/fill 189 88 184)
    (doseq [{:keys [position]} (vals units)
            :let [[x y] position]]
      (draw-on-tile q/ellipse x y))
    (let [selected (get-in app-state [:ui :selected])]
      (when-let [[x y] (get-in units [selected :position])]
        (q/no-fill)
        (q/stroke 255 255 0)        ; yellow outline
        (q/stroke-weight 2)
        (draw-on-tile q/ellipse x y)
        (q/no-stroke)))
    (draw-bar app-state)))

(defn on-click [app-state click-event]
  (let [{:keys [x y button]} click-event
        tile (pixel->tile [x y])]
    (or (case button
          :left (let [unit-id (unit-at (:game app-state) tile)]
                  (assoc-in app-state [:ui :selected] unit-id))
          :right (when-let [selected (get-in app-state [:ui :selected])]
                   (let [result (handle (:game app-state) {:cmd :move-unit :unit-id selected :to tile})]
                     (when-not (:error result)
                       (assoc app-state :game (:state result)))))
          nil)
        app-state)))



(defn setup []
  (q/no-stroke)
  (q/ellipse-mode :corner)
  (q/frame-rate 30))

(defn -main [& _]
  (let [{:keys [width height]} (:grid initial-state)]
    (q/sketch
     :title "rts.engine"
     :size [(* tile-size width) (+ (* tile-size height) status-bar-height)]
     :middleware [m/fun-mode]
     :setup  (fn [] (setup) {:game initial-state :ui {}})
     :update (fn [state] (if (zero? (mod (q/frame-count) 30))
                           (update state :game #(:state (handle % {:cmd :tick})))
                           state))
     :draw draw-world
     :mouse-pressed on-click)))
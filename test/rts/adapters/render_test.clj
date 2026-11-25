(ns rts.adapters.render-test
  (:require [clojure.test :refer [deftest is]]
            [rts.adapters.render :refer [blend on-click palette pixel->tile status-text terrain-color]]
            [rts.domain.time :refer [ticks-per-season]]))

(deftest terrain-colors-are-distinct-rgb-triples
  (let [colors (map #(terrain-color % 0) [:grass :forest :water])]
    (is (every? (fn [[r g b :as c]]
                  (and (= 3 (count c))
                       (every? #(<= 0 % 255) [r g b])))
                colors))
    (is (= 3 (count (set colors))))))

(deftest terrain-color-blends-through-the-year
  (let [{:keys [spring summer winter]} (:forest palette)
        mid (quot ticks-per-season 2)]
    ;; canonical color peaks mid-season
    (is (= spring (terrain-color :forest mid)))
    ;; at a season boundary we're halfway between the two canonicals
    (is (= (blend spring summer 1/2)
           (terrain-color :forest ticks-per-season)))
    ;; the year wraps: tick 0 is halfway from winter back to spring
    (is (= (blend winter spring 1/2)
           (terrain-color :forest 0)))))

(deftest blend-mixes-two-colors-by-progress
  (is (= [10 20 30] (blend [10 20 30] [20 40 60] 0)))
  (is (= [20 40 60] (blend [10 20 30] [20 40 60] 1)))
  (is (= [15 30 45] (blend [10 20 30] [20 40 60] 1/2)))
  (is (= [10 80 50] (blend [0 100 40] [40 20 80] 1/4))))

(deftest converts-pixel-to-tile-coordinates
  ;; tile-size is 24: a tile spans pixels 0-23, 24-47, ...
  (is (= [0 0] (pixel->tile [0 0])))
  (is (= [0 0] (pixel->tile [23 23])))
  (is (= [1 0] (pixel->tile [24 5])))
  (is (= [2 3] (pixel->tile [60 85]))))

(deftest left-click-selects-and-deselects
  (let [state {:game {:units {1 {:type :forager :position [2 3]}
                              7 {:type :hunter :position [0 0]}}}
               :ui {}}]
    ;; tile [2 3] spans pixels x 48-71, y 72-95
    (is (= 1 (get-in (on-click state {:x 50 :y 80 :button :left})
                     [:ui :selected])))
    (is (= 7 (get-in (on-click state {:x 10 :y 10 :button :left})
                     [:ui :selected])))
    ;; empty ground: deselect
    (is (nil? (get-in (on-click (assoc-in state [:ui :selected] 1)
                                {:x 120 :y 120 :button :left})
                      [:ui :selected])))))

(deftest right-click-moves-the-selected-unit
	(let [game {:grid {:width 6 :height 6}
    					:units {1 {:type :forager :position [2 3]}}}
    		state {:game game :ui {:selected 1}}]
    ;; adjacent tile [3 3] = pixels x 72-95, y 72-95
    (is (= [3 3] (get-in (on-click state {:x 80 :y 80 :button :right})
                      	 [:game :units 1 :position])))
    ;; nothing selected: nothing happens
    (is (= (assoc-in state [:ui :selected] nil)
    			 (on-click (assoc-in state [:ui :selected] nil)
    								 {:x 80 :y 80 :button :right})))
    ;; rejected command (too far): state unchanged
    (is (= state (on-click state {:x 130 :y 80 :button :right})))))

(deftest status-text-shows-food-day-and-season
  (is (= "food: 3 | day 26, autumn"
         (status-text {:resources {:food 3} :time {:tick 250}})))
  (is (= "food: 0 | day 801, spring"
         (status-text {:resources {:food 0} :time {:tick 8000}}))))

(ns rts.adapters.render-test
	(:require [clojure.test :refer [deftest is]]
						[rts.adapters.render :refer [blend palette status-text terrain-color]]
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

(deftest status-text-shows-food-day-and-season
	(is (= "food: 3 | day 26, autumn"
				 (status-text {:resources {:food 3} :time {:tick 250}})))
	(is (= "food: 0 | day 801, spring"
				 (status-text {:resources {:food 0} :time {:tick 8000}}))))

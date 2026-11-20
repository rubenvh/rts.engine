(ns rts.domain.grid-test
	(:require [clojure.test :refer [deftest is]]
						[rts.domain.grid :refer [in-bounds? adjacent? terrain-at coords]]))

(deftest lists-all-coordinates
	(is (= [[0 0] [1 0] [2 0]
					[0 1] [1 1] [2 1]]
				 (coords 3 2))))

(deftest looks-up-terrain
	(let [grid {:width 3 :height 2 :tiles {[1 0] :water [2 1] :forest}}]
		(is (= :water (terrain-at grid [1 0])))
		(is (= :forest (terrain-at grid [2 1])))
		(is (= :grass (terrain-at grid [0 0])))))

(deftest checks-grid-bounds
	(is (in-bounds? [2 1] 3 2))
	(is (not (in-bounds? [3 1] 3 2)))
 	(is (not (in-bounds? [2 2] 3 2))))

(deftest checks-adjacency
	(is (adjacent? [2 3] [3 3]))
	(is (adjacent? [2 3] [3 4]))
	(is (adjacent? [2 3] [1 2]))
	(is (not (adjacent? [2 3] [4 5])))
	(is (not (adjacent? [2 3] [2 3]))))
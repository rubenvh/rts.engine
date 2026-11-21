(ns rts.adapters.render-test
	(:require [clojure.test :refer [deftest is]]
						[rts.adapters.render :refer [terrain-color]]))

(deftest terrain-colors-are-distinct-rgb-triples
	(let [colors (map terrain-color [:grass :forest :water])]
		(is (every? (fn [[r g b :as c]]
									(and (= 3 (count c))
											 (every? #(<= 0 % 255) [r g b])))
								colors))
		(is (= 3 (count (set colors))))))

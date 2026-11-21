(ns rts.domain.world-test
	(:require [clojure.test :refer [deftest is]]
						[rts.domain.world :refer [initial-state]]))

(deftest world-has-a-playable-map
	(let [{:keys [grid units]} initial-state
				tiles (vals (:tiles grid))]
		(is (pos? (:width grid)))
		(is (pos? (:height grid)))
		(is (some #{:water} tiles))
		(is (some #{:forest} tiles))
		(is (some #(= :forager (:type %)) (vals units)))))

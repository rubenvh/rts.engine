(ns rts.domain.time-test
	(:require [clojure.test :refer [deftest is]]
						[rts.domain.time :refer [day-of season-of ticks-per-day days-per-season]]))

(deftest derives-day-from-ticks
	(is (= 0 (day-of 0)))
	(is (= 3 (day-of (* 3 ticks-per-day))))
	(is (= 3 (day-of (+ 2 (* 3 ticks-per-day))))))

(deftest derives-season-from-ticks
	(let [season-len (* days-per-season ticks-per-day)]
		(is (= :spring (season-of 0)))
		(is (= :summer (season-of season-len)))
		(is (= :autumn (season-of (* 2 season-len))))
		(is (= :winter (season-of (* 3 season-len))))
		(is (= :spring (season-of (* 4 season-len))))))

(ns rts.domain.time-test
	(:require [clojure.test :refer [deftest is]]
						[rts.domain.time :refer [day-of season-of season-progress ticks-per-day days-per-season]]))

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

(deftest derives-season-progress-from-ticks
	(let [season-len (* days-per-season ticks-per-day)]
		(is (== 0 (season-progress 0)))
		(is (== 1/2 (season-progress (quot season-len 2))))
		(is (== 3/4 (season-progress (* 3 (quot season-len 4)))))
		;; a new season starts back at zero
		(is (== 0 (season-progress season-len)))))

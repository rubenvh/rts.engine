(ns rts.application.commands-test
	(:require [clojure.test :refer [deftest is]]
						[rts.application.commands :refer [decide handle]]))

(deftest handle-applies-decided-events
	(is (= {:state {:grid {:width 6 :height 6 :tiles {[2 3] :forest}}
									:units {1 {:type :forager :position [2 3]}}
									:resources {:food 1}}
					:events [{:type :food-gathered :amount 1}]}
				 (handle {:grid {:width 6 :height 6 :tiles {[2 3] :forest}}
									:units {1 {:type :forager :position [2 3]}}}
								 {:cmd :forage :unit-id 1}))))

(deftest handle-passes-errors-through
	(is (= {:error :unit-not-found}
				 (handle {:units {}}
								 {:cmd :forage :unit-id 99}))))

(deftest decides-unit-moved-event
	(is (= [{:type :unit-moved :unit-id 1 :position [3 4]}]
				 (decide {:grid {:width 6 :height 6}
								 :units {1 {:type :worker :position [2 3]}}}
								 {:cmd :move-unit :unit-id 1 :to [3 4]}))))

(deftest rejects-move-beyond-one-tile
	(is (= {:error :too-far}
				 (decide {:grid {:width 6 :height 6}
								 :units {1 {:type :worker :position [2 3]}}}
								 {:cmd :move-unit :unit-id 1 :to [4 5]}))))

(deftest rejects-move-for-missing-unit
	(is (= {:error :unit-not-found}
				 (decide {:units {}}
								 {:cmd :move-unit :unit-id 99 :to [4 5]}))))

(deftest rejects-unknown-command
	(is (= {:error :unknown-command}
				 (decide {:units {}}
								 {:cmd :teleport :unit-id 1 :to [4 5]}))))

(deftest decides-unit-spawned-event
	(is (= [{:type :unit-spawned :unit-id 2 :unit-type :hunter :position [0 0]}]
				 (decide {:grid {:width 6 :height 6}
								 :units {1 {:type :forager :position [2 3]}}}
								 {:cmd :spawn-unit :unit-id 2 :unit-type :hunter :at [0 0]}))))

(deftest rejects-spawn-for-taken-unit-id
	(is (= {:error :unit-id-taken}
				 (decide {:grid {:width 6 :height 6}
								 :units {1 {:type :forager :position [2 3]}}}
								 {:cmd :spawn-unit :unit-id 1 :unit-type :hunter :at [0 0]}))))

(deftest rejects-forage-off-forest
	(is (= {:error :nothing-to-forage}
				 (decide {:grid {:width 6 :height 6 :tiles {[0 0] :forest}}
								 :units {1 {:type :forager :position [2 3]}}}
								 {:cmd :forage :unit-id 1}))))

(deftest decides-food-gathered-event
	(is (= [{:type :food-gathered :amount 1}]
				 (decide {:grid {:width 6 :height 6 :tiles {[2 3] :forest}}
								 :units {1 {:type :forager :position [2 3]}}}
								 {:cmd :forage :unit-id 1}))))

(deftest rejects-forage-for-non-forager
	(is (= {:error :not-a-forager}
				 (decide {:grid {:width 6 :height 6}
								 :units {1 {:type :hunter :position [2 3]}}}
								 {:cmd :forage :unit-id 1}))))

(deftest decides-tick-gathers-for-each-forager-on-forest
	(is (= [{:type :time-passed}
					{:type :food-gathered :amount 1}]
				 (decide {:grid {:width 6 :height 6 :tiles {[2 3] :forest [0 0] :forest}}
								 :units {1 {:type :forager :position [2 3]}
												 2 {:type :hunter :position [0 0]}
												 3 {:type :forager :position [4 4]}}}
								 {:cmd :tick}))))

(deftest decides-tick-with-no-foragers
	(is (= [{:type :time-passed}]
				 (decide {:grid {:width 6 :height 6}
								 :units {2 {:type :hunter :position [0 0]}}}
								 {:cmd :tick}))))

(deftest rejects-move-into-water
	(is (= {:error :impassable}
				 (decide {:grid {:width 6 :height 6 :tiles {[3 3] :water}}
								 :units {1 {:type :worker :position [2 3]}}}
								 {:cmd :move-unit :unit-id 1 :to [3 3]}))))

(deftest rejects-move-outside-grid
	(is (= {:error :out-of-bounds}
				 (decide {:grid {:width 3 :height 2}
								 :units {1 {:type :worker :position [2 1]}}}
								 {:cmd :move-unit :unit-id 1 :to [3 1]}))))
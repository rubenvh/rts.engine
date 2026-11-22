(ns rts.domain.state-test
  (:require [clojure.test :refer [deftest is]]
            [rts.domain.state :refer [evolve]]))

(deftest evolves-unit-spawned-event
  (is (= {:units {1 {:type :worker :position [2 3]}}}
         (evolve {:units {}}
                 {:type :unit-spawned
                  :unit-id 1
                  :unit-type :worker
                  :position [2 3]}))))

(deftest leaves-state-unchanged-for-unknown-event
  (is (= {:units {1 {:type :worker :position [2 3]}}}
         (evolve {:units {1 {:type :worker :position [2 3]}}}
                 {:type :unknown}))))

(deftest evolves-unit-moved-event
  (is (= {:units {1 {:type :worker :position [4 5]}}}
         (evolve {:units {1 {:type :worker :position [2 3]}}}
                 {:type :unit-moved
                  :unit-id 1
                  :position [4 5]}))))

(deftest evolves-food-gathered-event
  (is (= {:resources {:food 7}}
         (evolve {:resources {:food 4}}
                 {:type :food-gathered :amount 3}))))

(deftest evolves-food-gathered-event-from-nothing
  (is (= {:resources {:food 3}}
         (evolve {}
                 {:type :food-gathered :amount 3}))))

(deftest evolves-time-passed-event
  (is (= {:time {:tick 1}}
         (evolve {} {:type :time-passed})))
  (is (= {:time {:tick 5}}
         (evolve {:time {:tick 4}} {:type :time-passed}))))

(deftest replays-events-in-order
  (is (= {:units {1 {:type :worker :position [4 5]}}}
         (reduce evolve
                 {:units {}}
                 [{:type :unit-spawned
                   :unit-id 1
                   :unit-type :worker
                   :position [2 3]}
                  {:type :unit-moved
                   :unit-id 1
                   :position [4 5]}]))))
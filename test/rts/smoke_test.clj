(ns rts.smoke-test
  (:require [clojure.test :refer [deftest is]]))

(deftest test-runner-smoke-test
  (is (= 3 (+ 1 2))))
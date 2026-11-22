(ns rts.test-runner
  (:require [clojure.test :as test]
            [rts.adapters.render-test]
            [rts.application.commands-test]
            [rts.domain.grid-test]
            [rts.domain.state-test]
            [rts.domain.time-test]
            [rts.domain.world-test]
            [rts.smoke-test]))

(defn run-tests [_]
  (let [result (test/run-all-tests #"^rts\..*-test$")]
    (when (pos? (+ (:fail result) (:error result)))
      (throw (ex-info "Tests failed" result)))
    result))
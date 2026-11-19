(ns rts.application.commands
  (:require
   [rts.domain.grid :refer [adjacent? in-bounds?]]
   [rts.domain.state :refer [evolve]]))

(defn decide [state command]
  (let [{:keys [unit-id to at unit-type]} command
        {:keys [units grid]} state]
    (case (:cmd command)
      :tick (mapv (fn [_] {:type :food-gathered :amount 1}) (filter #(= :forager (:type %)) (vals units)))
      :spawn-unit (cond (contains? units unit-id)
                        {:error :unit-id-taken}
                        (not (in-bounds? at (:width grid) (:height grid)))
                        {:error :out-of-bounds}
                        :else
                        [{:type :unit-spawned
                          :unit-id unit-id
                          :unit-type unit-type
                          :position at}])
      :move-unit (cond (not (contains? units unit-id))
                       {:error :unit-not-found}
                       (not (in-bounds? to (:width grid) (:height grid)))
                       {:error :out-of-bounds}
                       (not (adjacent? to (get-in units [unit-id :position])))
                       {:error :too-far}
                       :else
                       [{:type :unit-moved
                         :unit-id unit-id
                         :position to}])
      :forage (cond (not (contains? units unit-id))
                    {:error :unit-not-found}
                    (not (= :forager (get-in state [:units unit-id :type])))
                    {:error :not-a-forager}
                    :else
                    [{:type :food-gathered
                      :amount 1}])
      {:error :unknown-command})))

(defn handle [state command]
  (let [result (decide state command)]
    (if (:error result)
      result
      {:state (reduce evolve state result)
       :events result})))
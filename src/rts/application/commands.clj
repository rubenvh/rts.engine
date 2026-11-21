(ns rts.application.commands
  (:require
   [rts.domain.grid :refer [adjacent? in-bounds? terrain-at]]
   [rts.domain.state :refer [evolve]]))

(defn require-unit [units unit-id]
  (when-not (contains? units unit-id)
    {:error :unit-not-found}))

(defn decide [state command]
  (let [{:keys [unit-id to at unit-type]} command
        {:keys [units grid]} state
        unit (units unit-id)]
    (case (:cmd command)
      :tick (mapv (fn [_] {:type :food-gathered :amount 1}) 
                  (filter #(and (= :forager (:type %))
                                (= :forest (terrain-at grid (:position %)))) 
                          (vals units)))
      :spawn-unit (cond (contains? units unit-id)
                        {:error :unit-id-taken}
                        (not (in-bounds? at (:width grid) (:height grid)))
                        {:error :out-of-bounds}
                        :else
                        [{:type :unit-spawned
                          :unit-id unit-id
                          :unit-type unit-type
                          :position at}])
      :move-unit (or (require-unit units unit-id)
                     (cond (not (in-bounds? to (:width grid) (:height grid)))
                           {:error :out-of-bounds}
                           (not (adjacent? to (:position unit)))
                           {:error :too-far}
                           (= :water (terrain-at grid to))
                           {:error :impassable}
                           :else
                           [{:type :unit-moved
                             :unit-id unit-id
                             :position to}]))
      :forage (or (require-unit units unit-id)
                  (cond (not= :forager (:type unit))
                        {:error :not-a-forager}
                        (not= :forest (terrain-at grid (:position unit)))
                        {:error :nothing-to-forage}
                        :else
                        [{:type :food-gathered
                          :amount 1}]))
      {:error :unknown-command})))

(defn handle [state command]
  (let [result (decide state command)]
    (if (:error result)
      result
      {:state (reduce evolve state result)
       :events result})))
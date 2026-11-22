(ns rts.domain.state)

(defn evolve [state event]
  (case (:type event)
    :time-passed 
    (update-in state [:time :tick] (fnil + 0) 1)
    :unit-spawned
    (assoc-in state [:units (:unit-id event)] {:type (:unit-type event) :position (:position event)})
    :unit-moved
    (assoc-in state [:units (:unit-id event) :position] (:position event))
    :food-gathered
    (update-in state [:resources :food] (fnil + 0) (:amount event))
    state))


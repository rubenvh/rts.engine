(ns rts.adapters.render)

(defn terrain-color [terrain]
  (case terrain
    :grass [124 246 14]
    :water [23 167 246]
    :forest [33 77 26]))
(ns rts.domain.grid)

(defn in-bounds? [position width height]
  (and (<= 0 (first position)) (< (first position) width)
       (<= 0 (second position)) (< (second position) height)))

(defn adjacent? [pos1 pos2]
  (let [[x1 y1] pos1 [x2 y2] pos2]
    (= 1 (max (Math/abs (- x1 x2)) (Math/abs (- y1 y2))))))

(defn terrain-at [grid pos]
  (get (:tiles grid) pos :grass))

(defn coords [width height]
  (for [y (range height), x (range width)] [x y]))

(def legend {\~ :water, \^ :forest, \. :grass})

(defn parse-map [rows]
  (let [height (count rows)
        width (count (first rows))
        parse-terrain (fn [x y] (legend (get-in rows [y x]) :grass))]
    {:width width
     :height height
     :tiles (into {}
                  (for [[x y] (coords width height)
                        :let [t (parse-terrain x y)]
                        :when (not= :grass t)]
                    [[x y] t]))}))
(ns com.example.clap)

(def boston {:name :boston :x 100 :y 100
  :edges [
    {:next :nyc :weight 100}
    {:next :chicago :weight 55}
    {:next :portland :weight 20}]} )
   
(def nyc {:name :nyc :x 100 :y 75
  :edges [{:next :washington :weight 100}]} )
  
(def washington {:name :washington :x 100 :y 50
  :edges [{:next :miami :weight 100}]})
  
(def chicago {:name :chicago :x 50 :y 100
  :edges [{:next :miami :weight 50}]})
  
(def portland {:name :portland :x 100 :y 110
  :edges [{:next :miami :weight 10}]} )
  
(def miami {:name :miami :x 102 :y 0 :edges []})
  
(def cities {
  :boston boston, 
  :nyc nyc, 
  :washington washington, 
  :chicago chicago, 
  :miami miami,
  :portland portland}
  )

(defn square [val]
  (* val val))
  
(defn distance [city1, city2]
  (Math/sqrt (+ (square (- (:x city1) (:x city2)))
    (square (- (:y city1) (:y city2))))))
  
(defn follow [node cities]
  (let [city (node cities)]
    (str  "\nStarting at " (:name city) ":\n"
    (loop [edges (seq (:edges city))
           text ""]
      (if (= (count edges) 0)
        text
        (recur (rest edges)
          (let [edge (first edges)] 
            (str text 
              "\tedge to " (:name ((:next edge) cities)) 
              " with " (:weight edge) 
              (follow (:next edge) cities) "\n")
            )))))))
      
(defn f_score [node]
  ;(println "f_score of: " node)
  (+ (:g_score (val node)) (:h_score (val node))))
  
(defn find-lowest-f-score [nodes]
  (loop [lowest (first nodes)
         remainder (rest nodes)]
    ;(println "find-lowest-f-score lowest: " lowest " and remainder: " remainder)
    (if (= (count remainder) 0)
      lowest
      (recur (if (< (f_score lowest) (f_score (first remainder)))
                lowest
                (first remainder))
             (rest remainder)))))

; Block to test find-lowest-f-score
;(def mylist
;[{:n 1 :g_score 3 :h_score 2}
;{:n 2 :g_score 5 :h_score 0}
;{:n 3 :g_score 1 :h_score 2}
;{:n 3 :g_score 6 :h_score 2}])
;(find-lowest-f-score mylist)

;     if came_from[current_node] is set
;         p = reconstruct_path(came_from,came_from[current_node])
;         return (p + current_node)
;     else
;         return the empty path
(defn reconstruct-path [nodes, goal, text]
  (if (= goal nil)
    text
    (recur nodes (:came_from (goal nodes)) 
      (str goal (if (= text "") "." (str "->" text))))))
        
; Block to test reconstruct_path
;(def mypath
;{:boston {:came_from :nyc}
;  :miami {}
;  :anotherplace {}
;  :nyc {:came_from :miami}})
;(reconstruct-path cities {:name :boston} "")
;(reconstruct-path mypath :nyc "") 
      
(defn check-edges [x, goal, closedset nodes]
    (loop [edges (:edges x)
          openset {}] 
        (if (= (count edges) 0)
          openset
          (let [edge (first edges)
            n ((:next edge) nodes)] 
            ;(println "Edge: " edge ", N: " n ", Openset: " openset)
            (recur (rest edges)
              (if (= ((:next edge) closedset) nil)
                (assoc openset (:name n)
                  (assoc n 
                    :g_score (+ (:g_score x) (:weight edge))
                    :h_score (distance n goal)
                    :came_from (:name x)))
                openset))))))
 
; For debugging check-edges, :paris and :chicago should be in the returned set, but not nyc.         
;(check-edges (assoc boston :g_score 0) miami { :nyc [] :washington [] } cities)

(defn a-star [start, goal, cities]
  (loop [closedset {}
         openset {(:name start)
            (assoc start 
              :g_score 0 
              :h_score (distance start goal))}]
    (let [x (val (find-lowest-f-score openset))]
      (if (= x nil)
        "failure"
      (if (= (:name x) (:name goal))
        (let [dataset (merge closedset openset)]
          ;(println "Dataset: " dataset)
          ;(println "To: " (:name goal))
          ;(println "A-Star found Path: " (reconstruct-path dataset (:name goal) "")))
          (reconstruct-path dataset (:name goal) ""))
        
        (recur (assoc closedset (:name x) x) 
               (merge (dissoc openset (:name x))
                (check-edges x goal closedset (merge cities closedset openset)))))))))

(defn run []
  (println "Boston to Boston: " (a-star boston boston cities))
  (println "Boston to New York: " (a-star boston nyc cities))
  (println "Boston to Miami: " (a-star boston miami cities)))


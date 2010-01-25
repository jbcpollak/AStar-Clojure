(ns com.jbcpollak.astar)

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
      
(defn check-edges [x, goal, closedset, nodes]
    (loop [edges (:edges x)
          openset {}] 
        (if (= (count edges) 0)
          openset
          (let [edge (first edges)
            n ((:next edge) nodes)] 
            (if (= n nil)
              (throw (new com.jbcpollak.astar.AStarException 
                (str "No node named " (:next edge) " found in " nodes))))
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

(defn a-star [start, goal, graph-nodes]
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
                (check-edges x goal closedset 
                  (merge graph-nodes closedset openset)))))))))

(defn parse-node [rawNode]
  (merge 
       {:edges (loop [e (:content (first (:content rawNode)))
                      formatted_edges []] 
                  (if (= (first e) nil)
                      formatted_edges
                      (recur (rest e)
                             (conj formatted_edges 
                              (let [edge (:attrs (first e))]
                                (assoc (assoc edge 
                                  :next (keyword (:next edge)))
                                  :weight (Integer.(:weight edge))))))))}
       (let [attrs (:attrs rawNode)]
          (assoc
            (assoc 
              (assoc attrs 
                :x (Integer.(:x attrs)))
                :y (Integer.(:y attrs)))
                :name (keyword (:name attrs))))))

(defn load-graph [filename]
  (println "Loading " filename)
  (def xml (clojure.xml/parse filename))
  ;(println "XML:" xml)
  (def nodeList (:content xml))
  (def formatted_nodes
      (loop [node nodeList
             formatted_nodes {}]
          (if (= (first node) nil)
              formatted_nodes
              (recur (rest node)
                     (let [newNode (parse-node (first node))]
                        (assoc formatted_nodes 
                            (keyword (:name newNode)) newNode))))))
  ;(println "Nodes:" formatted_nodes)
  formatted_nodes)
  
(defn a-star-from-file [start end filename]
  (def formatted_nodes (load-graph filename))
  ;(println "Nodes:" formatted_nodes)
  ;(println "Start: " ((keyword start) formatted_nodes))
  ;(println "End: " ((keyword start) formatted_nodes))
  
  (println "Quickest route from" start "to" end ": "
     (a-star ((keyword start) formatted_nodes)
             ((keyword end) formatted_nodes)
             formatted_nodes))
  ;(println "Boston to New York: " (a-star boston nyc formatted_nodes))
  ;(println "Boston to Miami: " (a-star boston miami formatted_nodes))
      )
      
(defn a-star-simple [start end graph]
  (println "Quickest route from" start "to" end ": "
     (a-star ((keyword start) graph)
             ((keyword end) graph)
             graph))
      )

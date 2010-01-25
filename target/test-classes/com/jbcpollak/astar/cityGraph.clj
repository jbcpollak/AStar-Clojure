(ns com.jbcpollak.astar)

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
  
(defn run []
  (println "Boston to Boston: " (a-star boston boston cities))
  (println "Boston to New York: " (a-star boston nyc cities))
  (println "Boston to Miami: " (a-star boston miami cities)))
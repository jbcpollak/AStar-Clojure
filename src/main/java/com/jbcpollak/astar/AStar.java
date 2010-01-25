package com.jbcpollak.astar;
 
import java.io.IOException;

import clojure.lang.RT;
import clojure.lang.Var;

import com.jbcpollak.clojure.ClojureUtil;
 
public class AStar {
    public static final String ASTARCLJ = "/com/jbcpollak/astar/astar.clj";

    final Object graph;

    private Var astarSimple;
    
    public static void init() throws IOException, Exception {
        ClojureUtil.loadScript(ASTARCLJ);           
    }
    
    AStar(String filename) throws IOException, Exception {
        init();
        
        Var loadGraph = RT.var("com.jbcpollak.astar", "load-graph");
        astarSimple = RT.var("com.jbcpollak.astar", "a-star-simple");
        
        graph = loadGraph.invoke(filename);
    }
    
    public void search(String start, String end) throws Exception{
        astarSimple.invoke(start, end, graph);
    }
    
    public static void main(String[] args) throws IOException, Exception {
        AStar astar = new AStar("src/main/resources/com/jbcpollak/astar/cities.xml");
        
        astar.search("boston", "boston");
        astar.search("boston", "nyc");
        astar.search("boston", "miami");
    }
}

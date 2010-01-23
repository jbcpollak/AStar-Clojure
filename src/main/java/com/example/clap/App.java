package com.example.clap;
 
import clojure.lang.RT;
 
public class App {
    private static final String MAINCLJ = "com/example/clap/astar.clj";

    public static void main(String[] args){
        System.out.println("Running A-Star in Clojure..." );
        try {
            RT.loadResourceScript(MAINCLJ);
            RT.var("com.example.clap", "run").invoke();
        } catch(Exception e) {
            e.printStackTrace();
        }
        
    }
}

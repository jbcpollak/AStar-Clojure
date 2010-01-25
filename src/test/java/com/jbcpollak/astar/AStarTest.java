package com.jbcpollak.astar;

import java.io.IOException;

import junit.framework.*;
import clojure.lang.RT;
import clojure.lang.Var;

import com.jbcpollak.clojure.ClojureUtil;

/**
 * Unit test for simple App.
 */
public class AStarTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AStarTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AStarTest.class );
    }

    /**
     * Rigourous Test :-)
     * @throws Exception 
     * @throws IOException 
     */
    public void testApp() throws IOException, Exception
    {
        AStar.init();
        
        ClojureUtil.loadScript("/com/jbcpollak/astar/cityGraph.clj");  
        Var cityRun = RT.var("com.jbcpollak.astar", "run");
        
        cityRun.invoke();
    }
}

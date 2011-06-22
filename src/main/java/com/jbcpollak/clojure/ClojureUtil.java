package com.jbcpollak.clojure;

import java.io.*;

/**
 * @author pardsbane
 *
 */
public class ClojureUtil {
    /**
     * @throws Exception
     * @throws IOException
     */
    public static void loadScript(String script) throws Exception, IOException {
        InputStream is = ClojureUtil.class.getResourceAsStream(script);
        
        assert is != null : "Unable to load expected resource!";
        if (is != null) {
            System.out.println("Successfully located " + script);

            try {
                int slash = script.lastIndexOf('/');
                String file = slash >= 0 ? script.substring(slash + 1) : script;
                
                clojure.lang.Compiler.load(new InputStreamReader(is), script, file);
            } finally {
                is.close();
            }
        }
    }
}

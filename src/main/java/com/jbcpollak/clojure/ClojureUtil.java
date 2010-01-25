/*
 *  Copyright (c) 2006-2009 Kiva Systems, Inc.
 *
 *  Proprietary Rights: The Kiva mobile fulfillment system and this
 *  material are confidential and proprietary to Kiva Systems, Inc. and
 *  are protected by copyright, trademark, patent, trade secret, and
 *  other proprietary rights and laws.  Except as provided by a written
 *  agreement between Kiva Systems, Inc.  and you, this material may not
 *  be copied, reproduced, modified, published, downloaded, posted,
 *  transmitted, or distributed, in any way.  Except as expressly
 *  provided in writing, Kiva Systems, Inc. and its affiliates do not
 *  grant any express or implied right to you under any patents,
 *  copyrights, trademarks, or trade secret information.
 *
 *  $Id$
 */
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

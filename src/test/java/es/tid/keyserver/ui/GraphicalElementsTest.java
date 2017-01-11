/*
 * Copyright 2017.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package es.tid.keyserver.ui;

import es.tid.keyserver.config.ConfigController;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test class for KeyServer graphical elements.
 * @author <a href="mailto:jgm1986@hotmail.com">Javier Gusano Martinez</a>
 * @since v0.4.2
 */
public class GraphicalElementsTest {
    /**
     * KeyServer Configuration controller object for tests.
     */
    private ConfigController confCtrl;
    
    /**
     * Test class constructor.
     * @since v0.4.2
     */
    public GraphicalElementsTest() {
        String [] requiredFields = {
            "ksCheckUpdates",
            "serverAddress",
            "serverPort",
            "serverKeyStoreFile",
            "serverKeyStorePassword",
            "serverKeyManagerPassword",
            "serverIdleTimeout",
            "serverIpWhiteList",
            "dbAddress",
            "dbPort",
            "dbPassword",
            "dbIndex",
            "dbCheckInterval",
        };
        confCtrl= new ConfigController("/properties/applicationtest.properties",
               "target/test-classes/properties/config.properties",
               requiredFields);
    }

    /**
     * Test of getAppName method, of class GraphicalElements.
     * @since v0.4.2
     */
    @Test
    public void testGetAppName() {
        System.out.println("getAppName");
        GraphicalElements instance = new GraphicalElements(confCtrl);
        String expResult = "Test App Name";
        String result = instance.getAppName();
        assertEquals(expResult, result);
    }

    /**
     * Test of ksTitle method, of class GraphicalElements.
     * @since v0.4.2
     */
    @Test
    public void testKsTitle() {
        System.out.println("ksTitle");
        GraphicalElements instance = new GraphicalElements(confCtrl);
        String expResult = 
                "+------------------------------------------------------------------------+\n" +
                "|             _  __          ____                                        |\n" +
                "|            | |/ /___ _   _/ ___|  ___ _ ____   _____ _ __              |\n" +
                "|            | ' // _ \\ | | \\___ \\ / _ \\ '__\\ \\ / / _ \\ '__|             |\n" +
                "|            | . \\  __/ |_| |___) |  __/ |   \\ V /  __/ |                |\n" +
                "|            |_|\\_\\___|\\__, |____/ \\___|_|    \\_/ \\___|_|                |\n" +
                "|                     |___/                                  v1.0.1.test |\n" +
                "+------------------------------------------------------------------------+\n" +
                " https://github.com/mami-project/KeyServer \n";
        String result = instance.ksTitle();
        assertEquals(expResult, result);
    }
}

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
import es.tid.keyserver.controllers.db.DataBase;
import es.tid.keyserver.core.status.KsMonitor;
import es.tid.keyserver.https.HttpsServerController;
import es.tid.keyserver.https.certificate.HttpsCert;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Scanner;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for KeyServer user interface controller.
 * @author <a href="mailto:jgm1986@hotmail.com">Javier Martinez Gusano</a>
 * @since v0.4.4
 */
public class UserInterfaceControllerTest {   
    /**
     * Database object.
     */
    private DataBase dbObj;
    
    /**
     * KeyServer monitor object.
     */
    private KsMonitor ksMonitor;
    
    /**
     * Test class constructor for user input controller.
     * @since v0.4.4
     */
    public UserInterfaceControllerTest() {
        // KeyServer Configuration Object.
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
        ConfigController softwareConfig = new ConfigController("/properties/application.properties",
               "target/test-classes/properties/configksmon.properties",
               requiredFields);
        // Database test object
        dbObj = new DataBase(softwareConfig.getDbAddress(), softwareConfig.getDbPort(), softwareConfig.getDbPassword(), softwareConfig.getDbIndex());
        // KeyServer Certificate test Object.
        HttpsCert sCert = new HttpsCert("target/test-classes/cert/ksserverkey.jks", "123456");
        // Jetty Server Object
        HttpsServerController httpsServer = new HttpsServerController(softwareConfig, dbObj);
        // KeyServer Monitor test object
        ksMonitor = new KsMonitor(dbObj, httpsServer, sCert, softwareConfig);
    }

    /**
     * Test of userInputDigest method, of class UserInterfaceController.
     * @since v0.4.4
     */
    @Test
    public void testUserInputDigest() {
        System.out.println("userInputDigest");
        InputStream stdin = System.in;
        boolean result = true;
        try {
            String [] options = {"H","I","P","D","F","L","Q","S","X"};
            for(String option : options ){
                System.out.println("[ TEST ] Show input for user input '" + option + "'");
                String data = "q\n";
                System.setIn(new ByteArrayInputStream(data.getBytes()));
                Scanner scanner = new Scanner(System.in);
                UserInterfaceController instance = new UserInterfaceController(scanner, ksMonitor, dbObj);
                instance.userInputDigest(option);
                result = instance.exitFlag();
            }
        } finally {
          System.setIn(stdin);
        }
        assertFalse(result);
    }

    /**
     * Test number 1 of exitFlag method, of class UserInterfaceController.
     * @since v0.4.4
     */
    @Test
    public void testExitFlag1() {        
        System.out.println("exitFlag1");
        String data = "q\n";
        boolean result;
        InputStream stdin = System.in;
        try {
            System.setIn(new ByteArrayInputStream(data.getBytes()));
            Scanner scanner = new Scanner(System.in);
            UserInterfaceController instance = new UserInterfaceController(scanner, ksMonitor, dbObj);
            instance.userInputDigest("H");
            result = instance.exitFlag();
        } finally {
          System.setIn(stdin);
        }
        assertFalse(result);
    }
    
    /**
     * Test number 2 of exitFlag method, of class UserInterfaceController.
     * @since v0.4.4
     */
    @Test
    public void testExitFlag2() {        
        System.out.println("exitFlag2");
        String data = "q\n";
        boolean result;
        InputStream stdin = System.in;
        try {
            System.setIn(new ByteArrayInputStream(data.getBytes()));
            Scanner scanner = new Scanner(System.in);
            UserInterfaceController instance = new UserInterfaceController(scanner, ksMonitor, dbObj);
            instance.userInputDigest("Q");
            result = instance.exitFlag();
        } finally {
          System.setIn(stdin);
        }
        assertTrue(result);
    }
}

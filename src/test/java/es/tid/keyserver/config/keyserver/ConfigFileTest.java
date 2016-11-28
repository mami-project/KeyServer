/**
 * Copyright 2016.
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
package es.tid.keyserver.config.keyserver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import org.junit.*;
import static org.junit.Assert.*;

/**
 * KeyServer configuration manager class test.
 * @author <a href="mailto:jgm1986@hotmail.com">Javier Gusano Martinez</a>
 * @since v0.3.0
 */
public class ConfigFileTest { 
	/**
	 * Configuration file test object.
	 */
    private final ConfigFile testObj;
    
    /**
     * Method with the required fields for the current tests.
     */
    public ConfigFileTest() {
        String testFileRoute = "target/test-classes/configtest.properties";
        
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
            "whiteList"
        };
        testObj = new ConfigFile(testFileRoute, requiredFields);
    }
    
    /**
     * JUnit test step.
     */
    @BeforeClass
    public static void setUpClass() {
        String testFileRoute = "target/test-classes/configtest.properties";
        Properties configFile = new Properties();
        // Default parammeters:
        configFile.setProperty("ksCheckUpdates", "3600000");
        configFile.setProperty("serverAddress", "192.168.1.2");
        configFile.setProperty("serverPort", "1443");
        configFile.setProperty("serverKeyStoreFile", "ksserverkey.jks");
        configFile.setProperty("serverKeyStorePassword", "123456");
        configFile.setProperty("serverKeyManagerPassword", "123456");
        configFile.setProperty("serverIdleTimeout", "30000");
        configFile.setProperty("serverIpWhiteList", "127.0.0.1");
        configFile.setProperty("dbAddress", "192.168.11.180");
        configFile.setProperty("dbPort", "6379");
        configFile.setProperty("dbPassword", "foobared");
        configFile.setProperty("dbIndex", "3");
        configFile.setProperty("dbCheckInterval", "1000");
        configFile.setProperty("whiteList", "IP_whitelist.txt");
        try{
            FileOutputStream newConfigFile = new FileOutputStream(testFileRoute);
            // Save parameters on file
            configFile.store(newConfigFile, null);
            // Close config file.
            newConfigFile.close();
            System.out.println("[ INFO ] Test file created: " + testFileRoute);
        } catch (FileNotFoundException ex) {
            System.err.println("[ ERROR ] Test file not found exception.");
        } catch (IOException ex) {
            System.err.println("[ ERROR ] Test file IO exception.");
        }
    }
    
    /**
     * JUnit test step.
     */
    @AfterClass
    public static void tearDownClass() {
        // Delete test file.
        String testFileRoute = "target/test-classes/configtest.properties";
        File file = new File(testFileRoute);
        file.delete();
        System.out.println("[ INFO ] Test file deleted: " + testFileRoute);
    }

    /**
     * Test of isCorrectlyInitialized method, of class ConfigFile.
     */
    @Test
    public void testIsCorrectlyInitialized() {
        System.out.println("isCorrectlyInitialized");
        boolean expResult = true;
        boolean result = testObj.isCorrectlyInitialized();
        assertEquals(expResult, result);
    }

    /**
     * Test of getServerAddress method, of class ConfigFile.
     */
    @Test
    public void testGetServerAddress() {
        System.out.println("getServerAddress");
        String expResult = "192.168.1.2";
        String result = testObj.getServerAddress();
        assertEquals(expResult, result);
    }

    /**
     * Test of getServerPort method, of class ConfigFile.
     */
    @Test
    public void testGetServerPort() {
        System.out.println("getServerPort");
        String expResult = "1443";
        String result = testObj.getServerPort();
        assertEquals(expResult, result);
    }

    /**
     * Test of getServerKeyStoreFile method, of class ConfigFile.
     */
    @Test
    public void testGetServerKeyFile() {
        System.out.println("getServerKeyFile");
        String expResult = "ksserverkey.jks";
        String result = testObj.getServerKeyStoreFile();
        assertEquals(expResult, result);
    }

    /**
     * Test of getKeyStorePassword method, of class ConfigFile.
     */
    @Test
    public void testGetServerKeyPass() {
        System.out.println("getServerKeyPass");
        String expResult = "123456";
        String result = testObj.getKeyStorePassword();
        assertEquals(expResult, result);
    }

    /**
     * Test of getDbAddress method, of class ConfigFile.
     */
    @Test
    public void testGetDbAddress() {
        System.out.println("getDbAddress");
        String expResult = "192.168.11.180";
        String result = testObj.getDbAddress();
        assertEquals(expResult, result);
    }

    /**
     * Test of getDbPort method, of class ConfigFile.
     */
    @Test
    public void testGetDbPort() {
        System.out.println("getDbPort");
        String expResult = "6379";
        String result = testObj.getDbPort();
        assertEquals(expResult, result);
    }

    /**
     * Test of getWhiteList method, of class ConfigFile.
     */
    @Test
    public void testGetWhiteList() {
        System.out.println("getWhiteList");
        String expResult = "IP_whitelist.txt";
        String result = testObj.getWhiteList();
        assertEquals(expResult, result);
    }   

    /**
     * Test of getDbPassword method, of class ConfigFile.
     */
    @Test
    public void testGetDbPassword() {
        System.out.println("getDbPassword");
        String expResult = "foobared";
        String result = testObj.getDbPassword();
        assertEquals(expResult, result);
    }

    /**
     * Test of getServerKeyStoreFile method, of class ConfigFile.
     */
    @Test
    public void testGetServerKeyStoreFile() {
        System.out.println("getServerKeyStoreFile");
        String expResult = "ksserverkey.jks";
        String result = testObj.getServerKeyStoreFile();
        assertEquals(expResult, result);
    }

    /**
     * Test of getKeyStorePassword method, of class ConfigFile.
     */
    @Test
    public void testGetKeyStorePassword() {
        System.out.println("getKeyStorePassword");
        String expResult = "123456";
        String result = testObj.getKeyStorePassword();
        assertEquals(expResult, result);
    }

    /**
     * Test of getKeyManagerPassword method, of class ConfigFile.
     */
    @Test
    public void testGetKeyManagerPassword() {
        System.out.println("getKeyManagerPassword");
        String expResult = "123456";
        String result = testObj.getKeyManagerPassword();
        assertEquals(expResult, result);
    }

    /**
     * Test of getDbIndex method, of class ConfigFile.
     */
    @Test
    public void testGetDbIndex() {
        System.out.println("getDbIndex");
        String expResult = "3";
        String result = testObj.getDbIndex();
        assertEquals(expResult, result);
    }

    /**
     * Test of getChkDbInterval method, of class ConfigFile.
     */
    @Test
    public void testGetChkDbInterval() {
        System.out.println("getChkDbInterval");
        String expResult = "1000";
        String result = testObj.getChkDbInterval();
        assertEquals(expResult, result);
    }

    /**
     * Test of getChkUpdateInterval method, of class ConfigFile.
     */
    @Test
    public void testGetChkUpdateInterval() {
        System.out.println("getChkUpdateInterval");
        String expResult = "3600000";
        String result = testObj.getChkUpdateInterval();
        assertEquals(expResult, result);
    }

    /**
     * Test of getServerIdleTimeout method, of class ConfigFile.
     */
    @Test
    public void testGetIdleTimeout() {
        System.out.println("getIdleTimeout");
        String expResult = "30000";
        String result = testObj.getServerIdleTimeout();
        assertEquals(expResult, result);
    }

    /**
     * Test of getServerIpWhiteList method, of class ConfigFile.
     */
    @Test
    public void testGetServerIpWhiteList() {
        System.out.println("getServerIpWhiteList");
        String expResult = "127.0.0.1";
        String result = this.testObj.getServerIpWhiteList();
        assertEquals(expResult, result);
    }
}

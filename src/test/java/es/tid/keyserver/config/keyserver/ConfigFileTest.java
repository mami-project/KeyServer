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

import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
     * Required fields array
     */
    private final String [] requiredFields = {
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
    
    /**
     * Method with the required fields for the current tests.
     */
    public ConfigFileTest() {
        String testFileRoute = "target/test-classes/config.properties";
        testObj = new ConfigFile(testFileRoute, requiredFields);
    }

    /**
     * Test of isCorrectlyInitialized method, of class ConfigFile.
     */
    @Test
    public void testIsCorrectlyInitialized1() {
        System.out.println("isCorrectlyInitialized1");
        boolean expResult = true;
        boolean result = testObj.isCorrectlyInitialized();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of isCorrectlyInitialized method, of class ConfigFile.
     */
    @Test
    public void testIsCorrectlyInitialized2() {
        System.out.println("isCorrectlyInitialized2");
        String testFileRoute = "target/test-classes/uncompleted.properties";
        ConfigFile notValidObj = new ConfigFile(testFileRoute, requiredFields);
        boolean expResult = false;
        boolean result = notValidObj.isCorrectlyInitialized();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of isCorrectlyInitialized method, of class ConfigFile.
     */
    @Test
    public void testIsCorrectlyInitialized3() {
        System.out.println("isCorrectlyInitialized3");
        String testFileRoute = "target/test-classes/void.properties";
        ConfigFile notValidObj = new ConfigFile(testFileRoute, requiredFields);
        boolean expResult = false;
        boolean result = notValidObj.isCorrectlyInitialized();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of isCorrectlyInitialized method, of class ConfigFile.
     */
    @Test
    public void testIsCorrectlyInitialized4() {
        System.out.println("isCorrectlyInitialized4");
        String testFileRoute = "target/test-classes/ghostfield.properties";
        ConfigFile notValidObj = new ConfigFile(testFileRoute, requiredFields);
        boolean expResult = false;
        boolean result = notValidObj.isCorrectlyInitialized();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of isCorrectlyInitialized method, of class ConfigFile.
     */
    @Test
    public void testIsCorrectlyInitialized5() {
        System.out.println("isCorrectlyInitialized5");
        String testFileRoute = "target/test-classes//.//ghostfield.properties";
        ConfigFile notValidObj = new ConfigFile(testFileRoute, requiredFields);
        boolean expResult = false;
        boolean result = notValidObj.isCorrectlyInitialized();
        assertEquals(expResult, result);
    }

    /**
     * Test of getServerAddress method, of class ConfigFile.
     */
    @Test
    public void testGetServerAddress() {
        System.out.println("getServerAddress");
        String expResult = "0.0.0.0";
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
        String expResult = "config/ksserverkey.jks";
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
        String expResult = "192.168.158.136";
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
        String expResult = "config/ksserverkey.jks";
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
        String expResult = "0";
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
        String expResult = "100000";
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
        String expResult = "192.168.2.3 & 127.0.0.2";
        String result = this.testObj.getServerIpWhiteList();
        assertEquals(expResult, result);
    }

    /**
     * Test of getServerIdleTimeout method, of class ConfigFile.
     */
    @Test
    public void testGetServerIdleTimeout() {
        System.out.println("getServerIdleTimeout");
        String expResult = "30000";
        String result = this.testObj.getServerIdleTimeout();
        assertEquals(expResult, result);
    }

    /**
     * Test of isCorrectlyInitialized method, of class ConfigFile.
     */
    @Test
    public void testIsCorrectlyInitialized() {
        System.out.println("isCorrectlyInitialized");
        boolean expResult = true;
        boolean result = this.testObj.isCorrectlyInitialized();
        assertEquals(expResult, result);
    }
}

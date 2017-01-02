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
package es.tid.keyserver.config;

import java.net.InetAddress;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for ConfigController.
 * @author <a href="mailto:jgm1986@hotmail.com">Javier Gusano Martinez</a>
 * @since v0.3.0
 */
public class ConfigControllerTest {
    
    /**
     * Configuration test object.
     */
    private final ConfigController testObj;
    
    /**
     * This class contains the fields necessaries for the tests.
     */
    public ConfigControllerTest() {
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
        this.testObj = new ConfigController("/applicationtest.properties",
               "target/test-classes/config.properties",
               requiredFields);
    }

    /**
     * Test of isCorrectlyInitialized method, of class ConfigController.
     */
    @Test
    public void testIsCorrectlyInitialized() {
        System.out.println("isCorrectlyInitialized");
        boolean expResult = true;
        boolean result = this.testObj.isCorrectlyInitialized();
        assertEquals(expResult, result);
    }

    /**
     * Test of getVersion method, of class ConfigController.
     */
    @Test
    public void testGetVersion() {
        System.out.println("getVersion");
        String expResult = "v1.0.1.test";
        String result = this.testObj.getVersion();
        assertEquals(expResult, result);
    }

    /**
     * Test of getAppName method, of class ConfigController.
     */
    @Test
    public void testGetAppName() {
        System.out.println("getAppName");
        String expResult = "Test App Name";
        String result = this.testObj.getAppName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getServerAddress method, of class ConfigController.
     */
    @Test
    public void testGetServerAddress() {
        System.out.println("getServerAddress");
        String expResult = "0.0.0.0";
        InetAddress result = this.testObj.getServerAddress();
        assertEquals(expResult, result.getHostAddress());
    }

    /**
     * Test of getServerPort method, of class ConfigController.
     */
    @Test
    public void testGetServerPort() {
        System.out.println("getServerPort");
        int expResult = 1443;
        int result = this.testObj.getServerPort();
        assertEquals(expResult, result);
    }

    /**
     * Test of getServerKeyStoreFile method, of class ConfigController.
     */
    @Test
    public void testGetServerKeyStoreFile() {
        System.out.println("getServerKeyStoreFile");
        String expResult = "config/ksserverkey.jks";
        String result = this.testObj.getServerKeyStoreFile();
        assertEquals(expResult, result);
    }

    /**
     * Test of getServerKeyStorePassword method, of class ConfigController.
     */
    @Test
    public void testGetServerKeyStorePassword() {
        System.out.println("getServerKeyPass");
        String expResult = "123456";
        String result = this.testObj.getServerKeyStorePassword();
        assertEquals(expResult, result);
    }

    /**
     * Test of getDbAddress method, of class ConfigController.
     */
    @Test
    public void testGetDbAddress() {
        System.out.println("getDbAddress");
        String expResult = "192.168.158.136";
        InetAddress result = this.testObj.getDbAddress();
        assertEquals(expResult, result.getHostAddress());
    }

    /**
     * Test of getDbPort method, of class ConfigController.
     */
    @Test
    public void testGetDbPort() {
        System.out.println("getDbPort");
        int expResult = 6379;
        int result = this.testObj.getDbPort();
        assertEquals(expResult, result);
    }

    /**
     * Test of getProjectPublicUrl method, of class ConfigController.
     */
    @Test
    public void testGetProjectPublicUrl() {
        System.out.println("getProjectPublicUrl");
        String expResult = "https://github.com/mami-project/KeyServer";
        String result = testObj.getProjectPublicUrl();
        assertEquals(expResult, result);
    }

    /**
     * Test of getDbPassword method, of class ConfigController.
     */
    @Test
    public void testGetDbPassword() {
        System.out.println("getDbPassword");
        String expResult = "foobared";
        String result = testObj.getDbPassword();
        assertEquals(expResult, result);
    }

    /**
     * Test of getServerKeyManagerPassword method, of class ConfigController.
     */
    @Test
    public void testGetServerKeyManagerPassword() {
        System.out.println("getServerKeyManagerPassword");
        String expResult = "123456";
        String result = testObj.getServerKeyManagerPassword();
        assertEquals(expResult, result);
    }

    /**
     * Test of getDbIndex method, of class ConfigController.
     */
    @Test
    public void testGetDbIndex() {
        System.out.println("getDbIndex");
        int expResult = 0;
        int result = testObj.getDbIndex();
        assertEquals(expResult, result);
    }

    /**
     * Test of getChkDbInterval method, of class ConfigController.
     */
    @Test
    public void testGetChkDbInterval() {
        System.out.println("getChkDbInterval");
        int expResult = 1000;
        int result = testObj.getChkDbInterval();
        assertEquals(expResult, result);
    }

    /**
     * Test of getChkUpdateInterval method, of class ConfigController.
     */
    @Test
    public void testGetChkUpdateInterval() {
        System.out.println("getChkUpdateInterval");
        int expResult = 100000;
        int result = testObj.getChkUpdateInterval();
        assertEquals(expResult, result);
    }

    /**
     * Test of getIdleTimeout method, of class ConfigController.
     */
    @Test
    public void testGetIdleTimeout() {
        System.out.println("getIdleTimeout");
        long expResult = 30000L;
        long result = testObj.getIdleTimeout();
        assertEquals(expResult, result);
    }

    /**
     * Test of getServerIpWhiteList method, of class ConfigController.
     */
    @Test
    public void testGetServerIpWhiteList() {
        System.out.println("getServerIpWhiteList");
        String[] expResult = {"192.168.2.3","127.0.0.2"};
        String[] result = this.testObj.getServerIpWhiteList();
        assertArrayEquals(expResult, result);
    }
}

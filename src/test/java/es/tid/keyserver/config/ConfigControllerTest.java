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

import org.junit.Test;

import java.net.InetAddress;

import static org.junit.Assert.*;

/**
 * Test class for ConfigController.
 * @author <a href="mailto:jgm1986@hotmail.com">Javier Gusano Martinez</a>
 * @since v0.3.0
 */
public class ConfigControllerTest {
    /**
     * Configuration test object 1.
     */
    private final ConfigController testObj1;
    
    /**
     * Configuration test object 2 (empty fields content).
     */
    private final ConfigController testObj2;
    
    /**
     * Configuration test object 3 (out of range fields).
     */
    private final ConfigController testObj3;
    
    /**
     * Configuration test object 4 (single IP whitelist IP value).
     */
    private final ConfigController testObj4;
    
    /**
     * Configuration test object 5 (uncompleted config file).
     */
    private final ConfigController testObj5;
    
    /**
     * Configuration test object 6 (void properties file).
     */
    private final ConfigController testObj6;
    
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
        this.testObj1 = new ConfigController("/properties/applicationtest.properties",
               "target/test-classes/properties/config.properties",
               requiredFields);
        this.testObj2 = new ConfigController("/properties/applicationtest.properties",
               "target/test-classes/properties/emptyconf.properties",
               requiredFields);
        this.testObj3 = new ConfigController("/properties/applicationtest.properties",
               "target/test-classes/properties/outofrangeconfig.properties",
               requiredFields);
        this.testObj4 = new ConfigController("/properties/applicationtest.properties",
               "target/test-classes/properties/config2.properties",
               requiredFields);
        this.testObj5 = new ConfigController("/properties/applicationtest.properties",
               "target/test-classes/properties/uncompleted.properties",
               requiredFields);
        this.testObj6 = new ConfigController("/properties/applicationtest.properties",
               "target/test-classes/properties/void.properties", new String[0]);
    }

    /**
     * Test of isCorrectlyInitialized method, of class ConfigController.
     */
    @Test
    public void testIsCorrectlyInitialized1() {
        System.out.println("isCorrectlyInitialized1");
        boolean expResult = true;
        boolean result = this.testObj1.isCorrectlyInitialized();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of isCorrectlyInitialized method, of class ConfigController.
     */
    @Test
    public void testIsCorrectlyInitialized2() {
        System.out.println("isCorrectlyInitialized2");
        String [] requiredFields = {
            "ksCheckUpdates"
        };
        ConfigController testFailObj = new ConfigController("/properties/undefinedapptest.properties",
               "target/test-classes/properties/config.properties",
               requiredFields);
        
        boolean expResult = false;
        boolean result = testFailObj.isCorrectlyInitialized();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of isCorrectlyInitialized method, of class ConfigController.
     */
    @Test
    public void testIsCorrectlyInitialized3() {
        System.out.println("isCorrectlyInitialized3");      
        boolean expResult = false;
        boolean result = this.testObj5.isCorrectlyInitialized();
        assertEquals(expResult, result);
    }

    /**
     * Test of getVersion method, of class ConfigController.
     */
    @Test
    public void testGetVersion() {
        System.out.println("getVersion");
        String expResult = "v1.0.1.test";
        String result = this.testObj1.getVersion();
        assertEquals(expResult, result);
    }

    /**
     * Test of getAppName method, of class ConfigController.
     */
    @Test
    public void testGetAppName() {
        System.out.println("getAppName");
        String expResult = "Test App Name";
        String result = this.testObj1.getAppName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getServerAddress method, of class ConfigController.
     */
    @Test
    public void testGetServerAddress1() {
        System.out.println("getServerAddress1");
        String expResult = "0.0.0.0";
        InetAddress result = this.testObj1.getServerAddress();
        assertEquals(expResult, result.getHostAddress());
    }
    
    /**
     * Test of getServerAddress method, of class ConfigController.
     */
    @Test
    public void testGetServerAddress2() {
        System.out.println("getServerAddress2");
        InetAddress result = this.testObj3.getServerAddress();
        assertNull(result);
    }
    
    /**
     * Test of getServerAddress method, of class ConfigController.
     */
    @Test
    public void testGetServerAddress3() {
        System.out.println("getServerAddress3");
        InetAddress result = this.testObj6.getServerAddress();
        assertNull(result);
    }

    /**
     * Test of getServerPort method, of class ConfigController.
     */
    @Test
    public void testGetServerPort1() {
        System.out.println("getServerPort1");
        int expResult = 1443;
        int result = this.testObj1.getServerPort();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getServerPort method, of class ConfigController.
     */
    @Test
    public void testGetServerPort2() {
        System.out.println("getServerPort2");
        int expResult = -1;
        int result = this.testObj2.getServerPort();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getServerPort method, of class ConfigController.
     */
    @Test
    public void testGetServerPort3() {
        System.out.println("getServerPort3");
        int expResult = -1;
        int result = this.testObj3.getServerPort();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getServerPort method, of class ConfigController.
     */
    @Test
    public void testGetServerPort4() {
        System.out.println("getServerPort4");
        int expResult = -1;
        int result = this.testObj6.getServerPort();
        assertEquals(expResult, result);
    }

    /**
     * Test of getServerKeyStoreFile method, of class ConfigController.
     */
    @Test
    public void testGetServerKeyStoreFile1() {
        System.out.println("getServerKeyStoreFile1");
        String expResult = "config/ksserverkey.jks";
        String result = this.testObj1.getServerKeyStoreFile();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getServerKeyStoreFile method, of class ConfigController.
     */
    @Test
    public void testGetServerKeyStoreFile2() {
        System.out.println("getServerKeyStoreFile2");
        String result = this.testObj6.getServerKeyStoreFile();
        assertNull(result);
    }

    /**
     * Test of getServerKeyStorePassword method, of class ConfigController.
     */
    @Test
    public void testGetServerKeyStorePassword1() {
        System.out.println("getServerKeyPass1");
        String expResult = "123456";
        String result = this.testObj1.getServerKeyStorePassword();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getServerKeyStorePassword method, of class ConfigController.
     */
    @Test
    public void testGetServerKeyStorePassword2() {
        System.out.println("getServerKeyPass2");
        String result = this.testObj3.getServerKeyStorePassword();
        assertNull(result);
    }
    
    
    /**
     * Test of getServerKeyStorePassword method, of class ConfigController.
     */
    @Test
    public void testGetServerKeyStorePassword3() {
        System.out.println("getServerKeyPass3");
        String result = this.testObj6.getServerKeyStorePassword();
        assertNull(result);
    }

    /**
     * Test of getDbAddress method, of class ConfigController.
     */
    @Test
    public void testGetDbAddress1() {
        System.out.println("getDbAddress1");
        String expResult = "192.168.158.136";
        InetAddress result = this.testObj1.getDbAddress();
        assertEquals(expResult, result.getHostAddress());
    }
    
    /**
     * Test of getDbAddress method, of class ConfigController.
     */
    @Test
    public void testGetDbAddress2() {
        System.out.println("getDbAddress2");
        InetAddress result = this.testObj3.getDbAddress();
        assertNull(result);
    }
    
    /**
     * Test of getDbAddress method, of class ConfigController.
     */
    @Test
    public void testGetDbAddress3() {
        System.out.println("getDbAddress3");
        InetAddress result = this.testObj6.getDbAddress();
        assertNull(result);
    }

    /**
     * Test of getDbPort method, of class ConfigController.
     */
    @Test
    public void testGetDbPort1() {
        System.out.println("getDbPort1");
        int expResult = 6379;
        int result = this.testObj1.getDbPort();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getDbPort method, of class ConfigController.
     */
    @Test
    public void testGetDbPort2() {
        System.out.println("getDbPort2");
        int expResult = -1;
        int result = this.testObj2.getDbPort();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getDbPort method, of class ConfigController.
     */
    @Test
    public void testGetDbPort3() {
        System.out.println("getDbPort3");
        int expResult = -1;
        int result = this.testObj3.getDbPort();
        assertEquals(expResult, result);
    }
    
        
    /**
     * Test of getDbPort method, of class ConfigController.
     */
    @Test
    public void testGetDbPort4() {
        System.out.println("getDbPort4");
        int expResult = -1;
        int result = this.testObj6.getDbPort();
        assertEquals(expResult, result);
    }

    /**
     * Test of getProjectPublicUrl method, of class ConfigController.
     */
    @Test
    public void testGetProjectPublicUrl() {
        System.out.println("getProjectPublicUrl");
        String expResult = "https://github.com/mami-project/KeyServer";
        String result = testObj1.getProjectPublicUrl();
        assertEquals(expResult, result);
    }

    /**
     * Test of getDbPassword method, of class ConfigController.
     */
    @Test
    public void testGetDbPassword() {
        System.out.println("getDbPassword");
        String expResult = "foobared";
        String result = testObj1.getDbPassword();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getDbPassword method, of class ConfigController.
     */
    @Test
    public void testGetDbPassword2() {
        System.out.println("getDbPassword2");
        String result = testObj2.getDbPassword();
        assertNull(result);
    }

        
    /**
     * Test of getDbPassword method, of class ConfigController.
     */
    @Test
    public void testGetDbPassword3() {
        System.out.println("getDbPassword3");
        String result = testObj6.getDbPassword();
        assertNull(result);
    }
    
    /**
     * Test of getServerKeyManagerPassword method, of class ConfigController.
     */
    @Test
    public void testGetServerKeyManagerPassword1() {
        System.out.println("getServerKeyManagerPassword1");
        String expResult = "123456";
        String result = testObj1.getServerKeyManagerPassword();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getServerKeyManagerPassword method, of class ConfigController.
     */
    @Test
    public void testGetServerKeyManagerPassword2() {
        System.out.println("getServerKeyManagerPassword2");
        String result = testObj3.getServerKeyManagerPassword();
        assertNull(result);
    }
    
    /**
     * Test of getServerKeyManagerPassword method, of class ConfigController.
     */
    @Test
    public void testGetServerKeyManagerPassword3() {
        System.out.println("getServerKeyManagerPassword3");
        String result = testObj6.getServerKeyManagerPassword();
        assertNull(result);
    }

    /**
     * Test of getDbIndex method, of class ConfigController.
     */
    @Test
    public void testGetDbIndex1() {
        System.out.println("getDbIndex1");
        int expResult = 0;
        int result = testObj1.getDbIndex();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getDbIndex method, of class ConfigController.
     */
    @Test
    public void testGetDbIndex2() {
        System.out.println("getDbIndex2");
        int expResult = 0;
        int result = testObj2.getDbIndex();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getDbIndex method, of class ConfigController.
     */
    @Test
    public void testGetDbIndex() {
        System.out.println("getDbIndex");
        int expResult = 2;
        int result = testObj4.getDbIndex();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getDbIndex method, of class ConfigController.
     */
    @Test
    public void testGetDbIndex4() {
        System.out.println("getDbIndex4");
        int expResult = 0;
        int result = testObj6.getDbIndex();
        assertEquals(expResult, result);
    }

    /**
     * Test of getChkDbInterval method, of class ConfigController.
     */
    @Test
    public void testGetChkDbInterval1() {
        System.out.println("getChkDbInterval1");
        int expResult = 1000;
        int result = testObj1.getChkDbInterval();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getChkDbInterval method, of class ConfigController.
     */
    @Test
    public void testGetChkDbInterval2() {
        System.out.println("getChkDbInterval2");
        int expResult = -1;
        int result = testObj2.getChkDbInterval();
        assertEquals(expResult, result);
    }
    /**
     * Test of getChkDbInterval method, of class ConfigController.
     */
    
    @Test
    public void testGetChkDbInterval3() {
        System.out.println("getChkDbInterval3");
        int expResult = -1;
        int result = testObj3.getChkDbInterval();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getChkDbInterval method, of class ConfigController.
     */
    
    @Test
    public void testGetChkDbInterval4() {
        System.out.println("getChkDbInterval4");
        int expResult = -1;
        int result = testObj6.getChkDbInterval();
        assertEquals(expResult, result);
    }

    /**
     * Test of getChkUpdateInterval method, of class ConfigController.
     */
    @Test
    public void testGetChkUpdateInterval() {
        System.out.println("getChkUpdateInterval");
        int expResult = 100000;
        int result = testObj1.getChkUpdateInterval();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getChkUpdateInterval method, of class ConfigController.
     */
    @Test
    public void testGetChkUpdateInterval2() {
        System.out.println("getChkUpdateInterval2");
        int expResult = -1;
        int result = testObj2.getChkUpdateInterval();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getChkUpdateInterval method, of class ConfigController.
     */
    @Test
    public void testGetChkUpdateInterval3() {
        System.out.println("getChkUpdateInterval3");
        int expResult = -1;
        int result = testObj3.getChkUpdateInterval();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getChkUpdateInterval method, of class ConfigController.
     */
    @Test
    public void testGetChkUpdateInterval4() {
        System.out.println("getChkUpdateInterval4");
        int expResult = -1;
        int result = testObj6.getChkUpdateInterval();
        assertEquals(expResult, result);
    }

    /**
     * Test of getIdleTimeout method, of class ConfigController.
     */
    @Test
    public void testGetIdleTimeout1() {
        System.out.println("getIdleTimeout1");
        long expResult = 30000L;
        long result = testObj1.getIdleTimeout();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getIdleTimeout method, of class ConfigController.
     */
    @Test
    public void testGetIdleTimeout2() {
        System.out.println("getIdleTimeout2");
        long expResult = -1L;
        long result = testObj2.getIdleTimeout();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getIdleTimeout method, of class ConfigController.
     */
    @Test
    public void testGetIdleTimeout3() {
        System.out.println("getIdleTimeout3");
        long expResult = -1L;
        long result = testObj3.getIdleTimeout();
        assertEquals(expResult, result);
    }

        /**
     * Test of getIdleTimeout method, of class ConfigController.
     */
    @Test
    public void testGetIdleTimeout4() {
        System.out.println("getIdleTimeout4");
        long expResult = -1L;
        long result = testObj6.getIdleTimeout();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getServerIpWhiteList method, of class ConfigController.
     */
    @Test
    public void testGetServerIpWhiteList1() {
        System.out.println("getServerIpWhiteList1");
        String[] expResult = {"192.168.2.3","127.0.0.2"};
        String[] result = this.testObj1.getServerIpWhiteList();
        assertArrayEquals(expResult, result);
    }
    
    /**
     * Test of getServerIpWhiteList method, of class ConfigController.
     */
    @Test
    public void testGetServerIpWhiteList2() {
        System.out.println("getServerIpWhiteList2");
        String[] expResult = {"192.168.2.3"};
        String[] result = this.testObj4.getServerIpWhiteList();
        assertArrayEquals(expResult, result);
    }
    
    /**
     * Test of getServerIpWhiteList method, of class ConfigController.
     */
    @Test
    public void testGetServerIpWhiteList3() {
        System.out.println("getServerIpWhiteList3");
        String[] result = this.testObj6.getServerIpWhiteList();
        assertNull(result);
    }
}

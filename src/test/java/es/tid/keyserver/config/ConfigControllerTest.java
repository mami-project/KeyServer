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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Properties;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

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
        String testFileRoute = "target/test-classes/configtest.properties";
        String [] requiredFields = {
            "ksCheckUpdates",
            "serverAddress",
            "serverPort",
            "serverKeyStoreFile",
            "serverKeyStorePassword",
            "serverKeyManagerPassword",
            "dbAddress",
            "dbPort",
            "dbPassword",
            "dbIndex",
            "dbCheckInterval",
            "whiteList"
        };
        this.testObj = new ConfigController("/applicationtest.properties", testFileRoute, requiredFields);
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
        String expResult = "192.168.1.2";
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
     * Test of getServerSSLContext method, of class ConfigController.
     */
    @Test
    @Ignore("Deprecated method")
    public void testGetServerSSLContext() {
        System.out.println("getServerSSLContext");
        String expResult = "TLSv1.2";
        String result = this.testObj.getServerSSLContext();
        assertEquals(expResult, result);
    }

    /**
     * Test of getServerKeyStoreFile method, of class ConfigController.
     */
    @Test
    public void testGetServerKeyStoreFile() {
        System.out.println("getServerKeyStoreFile");
        String expResult = "ksserverkey.jks";
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
     * Test of getServerBacklog method, of class ConfigController.
     */
    @Test
    @Ignore("Deprecated method")
    public void testGetServerBacklog() {
        System.out.println("getServerBacklog");
        int expResult = 5;
        int result = this.testObj.getServerBacklog();
        assertEquals(expResult, result);
    }

    /**
     * Test of getServerKeyManagerFactory method, of class ConfigController.
     */
    @Test
    @Ignore("Deprecated method")
    public void testGetServerKeyManagerFactory() {
        System.out.println("getServerKeyManagerFactory");
        String expResult = "SunX509";
        String result = this.testObj.getServerKeyManagerFactory();
        assertEquals(expResult, result);
    }

    /**
     * Test of getServerTrustManagerFactory method, of class ConfigController.
     */
    @Test
    @Ignore("Deprecated method")
    public void testGetServerTrustManagerFactory() {
        System.out.println("getServerTrustManagerFactory");
        String expResult = "SunX509";
        String result = this.testObj.getServerTrustManagerFactory();
        assertEquals(expResult, result);
    }

    /**
     * Test of getServerKeyStore method, of class ConfigController.
     */
    @Test
    @Ignore("Deprecated method")
    public void testGetServerKeyStore() {
        System.out.println("getServerKeyStore");
        String expResult = "JKS";
        String result = this.testObj.getServerKeyStore();
        assertEquals(expResult, result);
    }

    /**
     * Test of getServerCiphersSuites method, of class ConfigController.
     */
    @Test
    @Ignore("Deprecated method")
    public void testGetServerCiphersSuites() {
        System.out.println("getServerCiphersSuites");
        String expResult = "TLS_DHE_DSS_WITH_AES_128_GCM_SHA256, TLS_DHE_DSS_WITH_AES_128_CBC_SHA256, TLS_DHE_DSS_WITH_AES_128_CBC_SHA, SSL_DHE_DSS_WITH_3DES_EDE_CBC_SHA";
        String result = this.testObj.getServerCiphersSuites();
        assertEquals(expResult, result);
    }

    /**
     * Test of getDbAddress method, of class ConfigController.
     */
    @Test
    public void testGetDbAddress() {
        System.out.println("getDbAddress");
        String expResult = "192.168.11.180";
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
     * Test of getWhiteList method, of class ConfigController.
     */
    @Test
    public void testGetWhiteList() {
        System.out.println("getWhiteList");
        String expResult = "IP_whitelist.txt";
        String result = this.testObj.getWhiteList();
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
        int expResult = 3;
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
        int expResult = 3600000;
        int result = testObj.getChkUpdateInterval();
        assertEquals(expResult, result);
    }
}

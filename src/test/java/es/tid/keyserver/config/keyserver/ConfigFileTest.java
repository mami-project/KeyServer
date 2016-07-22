/*
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
    private final ConfigFile testObj;
    
    public ConfigFileTest() {
        String testFileRoute = "target/test-classes/configtest.properties";
        
        String [] requiredFields = {
            "serverAddress",
            "serverPort",
            "serverSSLContext",
            "serverKeyFile",
            "serverKeyPass",
            "serverBacklog",
            "serverKeyManagerFactory",
            "serverTrustManagerFactory",
            "serverKeyStore",
            "serverCiphersSuites",
            "dbAddress",
            "dbPort",
            "whiteList"
        };
        testObj = new ConfigFile(testFileRoute, requiredFields);
    }
    
    @BeforeClass
    public static void setUpClass() {
        String testFileRoute = "target/test-classes/configtest.properties";
        Properties configFile = new Properties();
        // Default parammeters:
        configFile.setProperty("serverAddress", "192.168.1.2");
        configFile.setProperty("serverPort", "1443");
        configFile.setProperty("serverSSLContext", "TLSv1.2");
        configFile.setProperty("serverKeyFile", "HTTPS_keystore.ks");
        configFile.setProperty("serverKeyPass", "123456");
        configFile.setProperty("serverBacklog", "5");
        configFile.setProperty("serverKeyManagerFactory", "SunX509");
        configFile.setProperty("serverTrustManagerFactory", "SunX509");
        configFile.setProperty("serverKeyStore", "JKS");
        configFile.setProperty("serverCiphersSuites", "TLS_DHE_DSS_WITH_AES_128_GCM_SHA256, TLS_DHE_DSS_WITH_AES_128_CBC_SHA256, TLS_DHE_DSS_WITH_AES_128_CBC_SHA, SSL_DHE_DSS_WITH_3DES_EDE_CBC_SHA");
        configFile.setProperty("dbAddress", "192.168.11.180");
        configFile.setProperty("dbPort", "6379");
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
     * Test of getServerSSLContext method, of class ConfigFile.
     */
    @Test
    public void testGetServerSSLContext() {
        System.out.println("getServerSSLContext");
        String expResult = "TLSv1.2";
        String result = testObj.getServerSSLContext();
        assertEquals(expResult, result);
    }

    /**
     * Test of getServerKeyFile method, of class ConfigFile.
     */
    @Test
    public void testGetServerKeyFile() {
        System.out.println("getServerKeyFile");
        String expResult = "HTTPS_keystore.ks";
        String result = testObj.getServerKeyFile();
        assertEquals(expResult, result);
    }

    /**
     * Test of getServerKeyPass method, of class ConfigFile.
     */
    @Test
    public void testGetServerKeyPass() {
        System.out.println("getServerKeyPass");
        String expResult = "123456";
        String result = testObj.getServerKeyPass();
        assertEquals(expResult, result);
    }

    /**
     * Test of getServerBacklog method, of class ConfigFile.
     */
    @Test
    public void testGetServerBacklog() {
        System.out.println("getServerBacklog");
        String expResult = "5";
        String result = testObj.getServerBacklog();
        assertEquals(expResult, result);
    }

    /**
     * Test of getServerKeyManagerFactory method, of class ConfigFile.
     */
    @Test
    public void testGetServerKeyManagerFactory() {
        System.out.println("getServerKeyManagerFactory");
        String expResult = "SunX509";
        String result = testObj.getServerKeyManagerFactory();
        assertEquals(expResult, result);
    }

    /**
     * Test of getServerTrustManagerFactory method, of class ConfigFile.
     */
    @Test
    public void testGetServerTrustManagerFactory() {
        System.out.println("getServerTrustManagerFactory");
        String expResult = "SunX509";
        String result = testObj.getServerTrustManagerFactory();
        assertEquals(expResult, result);
    }

    /**
     * Test of getServerKeyStore method, of class ConfigFile.
     */
    @Test
    public void testGetServerKeyStore() {
        System.out.println("getServerKeyStore");
        String expResult = "JKS";
        String result = testObj.getServerKeyStore();
        assertEquals(expResult, result);
    }

    /**
     * Test of getServerCiphersSuites method, of class ConfigFile.
     */
    @Test
    public void testGetServerCiphersSuites() {
        System.out.println("getServerCiphersSuites");
        String expResult = "TLS_DHE_DSS_WITH_AES_128_GCM_SHA256, "
                + "TLS_DHE_DSS_WITH_AES_128_CBC_SHA256, "
                + "TLS_DHE_DSS_WITH_AES_128_CBC_SHA, "
                + "SSL_DHE_DSS_WITH_3DES_EDE_CBC_SHA";
        String result = testObj.getServerCiphersSuites();
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
}

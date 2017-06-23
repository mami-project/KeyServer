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
package es.tid.keyserver.core.status;

import es.tid.keyserver.config.ConfigController;
import es.tid.keyserver.controllers.db.DataBase;
import es.tid.keyserver.core.lib.LastVersionAvailableTest;
import es.tid.keyserver.https.HttpsServerController;
import es.tid.keyserver.https.certificate.HttpsCert;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.jetty.server.handler.StatisticsHandler;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Assume;

/**
 * Test class for KeyServer monitor object.
 * @author <a href="mailto:jgm1986@hotmail.com">Javier Gusano Martinez</a>
 * @since v0.4.4
 */
public class KsMonitorTest {
    /**
     * Flag to skip the tests if Redis Database connection is not available.
     */
    private boolean skipTest = true;
    
    /**
     * Database connection test object.
     */
    private DataBase dbObj;
    
    /**
     * HTTPs Server Controller object.
     */
    private HttpsServerController httpsServer;
    
    /**
     * HTTPs Server Certificate object.
     */
    private HttpsCert sCert;
    
    /**
     * KeyServer Configuration Object.
     */
    private ConfigController softwareConfig;
    
    /**
     * KeyServer Monitor Test Object.
     */
    private KsMonitor ksMonitor;
    
    /**
     * GitHub Connection Available
     */
    private boolean urlConnectionAvailable;
    
    /**
     * Default KsMonitorTest class constructor.
     */
    public KsMonitorTest(){
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
        softwareConfig = new ConfigController("/properties/application.properties",
               "target/test-classes/properties/configksmon.properties",
               requiredFields);
        // Database test object
        dbObj = new DataBase(softwareConfig.getDbAddress(), softwareConfig.getDbPort(), softwareConfig.getDbPassword(), softwareConfig.getDbIndex());
        if(!dbObj.isConnected()){
            System.out.println("[ WARNING ] Connection to Redis Database is not available. JUnit tests will be skipped.");
            this.skipTest = false;
        }
        // KeyServer Certificate test Object.
        sCert = new HttpsCert("target/test-classes/cert/ksserverkey.jks", "123456");
        // Jetty Server Object
        httpsServer = new HttpsServerController(softwareConfig, dbObj);
        // KeyServer Monitor test object
        ksMonitor = new KsMonitor(dbObj, httpsServer, sCert, softwareConfig);
        // Check if GitHub connnection is available.
        try {
            URL urlServer = new URL(LastVersionAvailableTest.REPOURL);
            HttpURLConnection urlConn = (HttpURLConnection) urlServer.openConnection();
            urlConn.setConnectTimeout(10000); // 10 Seconds Timeout 
            urlConn.connect();
            urlConnectionAvailable = urlConn.getResponseCode() == 200;
        } catch (IOException e1) {
            System.out.println("[ WARNING ] Connection to GitHub is not available. JUnit tests will be skipped.");
            urlConnectionAvailable = false;
        }
    }

    /**
     * Test of stop method, of class KsMonitor.
     * @since v0.4.4
     */
    @Test
    public void testStop() {
        System.out.println("stop");
        KsMonitor instance = new KsMonitor(dbObj, httpsServer, sCert, softwareConfig);
        instance.stop();
        assertTrue(instance.isCorrectlyInitialized());
    }

    /**
     * Test of isRedisConnectionAvailable method, of class KsMonitor.
     * @since v0.4.4
     */
    @Test
    public void testIsRedisConnectionAvailable() {
        System.out.println("isRedisConnectionAvailable");
        Assume.assumeTrue(this.skipTest); // If connection is not available, skip the test.
        try{
            Thread.sleep(5000L);
        } catch (InterruptedException ex) {
            Logger.getLogger(KsMonitorTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        boolean result = this.ksMonitor.isRedisConnectionAvailable();
        assertTrue(result);
    }

    /**
     * Test of httpsServerStatus method, of class KsMonitor.
     * @since v0.4.4
     */
    @Test
    public void testHttpsServerStatus() {
        System.out.println("httpsServerStatus");
        String expResult = "STOPPED";
        String result = this.ksMonitor.httpsServerStatus();
        assertEquals(expResult, result);
    }

    /**
     * Test of getHttpsCertificateExpDate method, of class KsMonitor.
     * @since v0.4.4
     */
    @Test
    public void testGetHttpsCertificateExpDate() {
        System.out.println("getHttpsCertificateExpDate");
        Date expResult = new Date();
        expResult.setTime(4633671078000L); // Dummy certificate expiration date as long value.
        Date result = this.ksMonitor.getHttpsCertificateExpDate();
        assertEquals(expResult, result);
    }

    /**
     * Test of getHttpsCertificateRemainDays method, of class KsMonitor.
     * @since v0.4.4
     */
    @Test
    public void testGetHttpsCertificateRemainDays() {
        System.out.println("getHttpsCertificateRemainDays");
        long result = this.ksMonitor.getHttpsCertificateRemainDays();
        assertTrue(result > 0);
    }

    /**
     * Test of getCurrentKSVersion method, of class KsMonitor.
     * @since v0.4.4
     */
    @Test
    public void testGetCurrentKSVersion() {
        System.out.println("getCurrentKSVersion");
        String result = this.ksMonitor.getCurrentKSVersion();
        System.out.println("[ INFO ] KeyServer Current Version: " + result);
        assertEquals("v1.0.1.test", result);
    }

    /**
     * Test of getLastKSVersionAvailable method, of class KsMonitor.
     * @since v0.4.4
     */
    @Test
    public void testGetLastKSVersionAvailable() {
        System.out.println("getLastKSVersionAvailable");
        Assume.assumeTrue(this.urlConnectionAvailable); // If connection is not available, skip the test.
        String result = this.ksMonitor.getLastKSVersionAvailable();
        System.out.println("[ INFO ] Last KeyServer version available: " + result);
        assertTrue(result.length() > 0);
    }

    /**
     * Test of getKSProjectURL method, of class KsMonitor.
     * @since v0.4.4
     */
    @Test
    public void testGetKSProjectURL() {
        System.out.println("getKSProjectURL");
        String expResult = softwareConfig.getProjectPublicUrl();
        String result = this.ksMonitor.getKSProjectURL();
        assertEquals(expResult, result);
    }

    /**
     * Test of keyServerRunningSince method, of class KsMonitor.
     * @since v0.4.4
     */
    @Test
    public void testKeyServerRunningSince() {
        System.out.println("keyServerRunningSince");
        Date result = this.ksMonitor.keyServerRunningSince();
        assertTrue(result.getTime() > 0);
    }

    /**
     * Test of getStatistics method, of class KsMonitor.
     * @since v0.4.4
     */
    @Test
    public void testGetStatistics() {
        System.out.println("getStatistics");
        StatisticsHandler result = this.ksMonitor.getStatistics();
        assertEquals(0, result.getRequests());
    }

    /**
     * Test of isCorrectlyInitialized method, of class KsMonitor.
     * @since v0.4.4
     */
    @Test
    public void testIsCorrectlyInitialized() {
        System.out.println("isCorrectlyInitialized");
        boolean result = this.ksMonitor.isCorrectlyInitialized();
        assertTrue(result);
    }
}
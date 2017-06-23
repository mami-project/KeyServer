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
package es.tid.keyserver.ui.controls;

import es.tid.keyserver.config.ConfigController;
import es.tid.keyserver.controllers.db.DataBase;
import es.tid.keyserver.core.status.KsMonitor;
import es.tid.keyserver.https.HttpsServerController;
import es.tid.keyserver.https.certificate.HttpsCert;
import org.eclipse.jetty.server.handler.StatisticsHandler;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Test class for KeyServer Status Controller object.
 * @author <a href="mailto:jgm1986@hotmail.com">Javier Gusano Martinez</a>
 * @since v0.4.4
 */
public class StatusControllerTest {
    /**
     * KeyServer Monitor Test Object.
     */
    private KsMonitor ksMonitor;
    
    /**
     * Default test class constructor.
     * @since v0.4.4
     */
    public StatusControllerTest() {
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
        DataBase dbObj = new DataBase(softwareConfig.getDbAddress(), softwareConfig.getDbPort(), softwareConfig.getDbPassword(), softwareConfig.getDbIndex());
        // KeyServer Certificate test Object.
        HttpsCert sCert = new HttpsCert("target/test-classes/cert/ksserverkey.jks", "123456");
        // Jetty Server Object
        HttpsServerController httpsServer = new HttpsServerController(softwareConfig, dbObj);
        // KeyServer Monitor test object
        ksMonitor = new KsMonitor(dbObj, httpsServer, sCert, softwareConfig);
    }
    
    /**
     * Test of showKeyServerHelp method, of class StatusController.
     * @since v0.4.4
     */
    @Test
    public void testShowKeyServerHelp() {
        System.out.println("showKeyServerHelp");
        StatusController.showKeyServerHelp();
        assertTrue(ksMonitor.isCorrectlyInitialized());
    }

    /**
     * Test of showKsStatusDetails method, of class StatusController.
     * @since v0.4.4
     */
    @Test
    public void testShowKsStatusDetails() {
        System.out.println("showKsStatusDetails");
        StatusController.showKsStatusDetails(ksMonitor);
        assertTrue(ksMonitor.isCorrectlyInitialized());
    }

    /**
     * Test of showKsStats method, of class StatusController.
     * @since v0.4.4
     */
    @Test
    public void testShowKsStats() {
        System.out.println("showKsStats");
        StatisticsHandler statistics = ksMonitor.getStatistics();
        StatusController.showKsStats(statistics);
        assertTrue(ksMonitor.isCorrectlyInitialized());
    }
}

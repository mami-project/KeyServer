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
package es.tid.keyserver.https;

import es.tid.keyserver.config.ConfigController;
import es.tid.keyserver.controllers.db.DataBase;
import es.tid.keyserver.core.lib.CheckObject;
import es.tid.keyserver.https.jetty.KsJetty;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.eclipse.jetty.server.handler.StatisticsHandler;
import org.slf4j.LoggerFactory;

/**
 * Class for HTTP Server.
 * @author <a href="mailto:jgm1986@hotmail.com">Javier Gusano Martinez</a>
 * @since v0.1.0
 */
public class HttpsServerController implements CheckObject{
    /**
     * Server Object.
     */
    private KsJetty jettyserver;
    /**
     * Flag for check if the object is correctly initialized.
     */
    private boolean isInitializated = false;
    /**
     * Logger object.
     */
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(HttpsServerController.class);
    /**
     * Main constructor for the HTTPS Server class.
     * @param parameters Object with program parameters.
     * @param objDB REDIS database Object.
     * @see <a href="http://stackoverflow.com/questions/2308479/simple-java-https-server">More info about HttpsServer</a>
     * @since v0.1.0
     */
    public HttpsServerController(ConfigController parameters, DataBase objDB){
        jettyserver = new KsJetty(parameters, objDB);
    }
    
    /**
     * Initialize the Jetty Controller Thread.
     * @since v0.4.0
     */
    public void start(){
        Thread thjettyserver = new Thread(jettyserver, "THJettyCtrl");
        thjettyserver.start();
        while(!jettyserver.isReady()){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                // Error level.
                LOGGER.error("Jetty inititialization pause error: {}", ex.getMessage());
                // Debug level.
                StringWriter errors = new StringWriter();
                ex.printStackTrace(new PrintWriter(errors));
                LOGGER.debug(errors.toString());
            }
        }
        this.isInitializated = true;
    }
    /**
     * Stops the HTTPS KeyServer closing the listening socket and disallowing 
     *     any new exchanges from being processed.
     * 
     *     <p>The value specified as timeout for this method is 5 seconds.
     * @since v0.1.0
     */
    public void stop(){
        jettyserver.stop();
    }
    
    /**
     * This method is used to monitor the Jetty server status.
     * @return String with one of the following values: FAILED, RUNNING, 
     *     STARTED, STARTING, STOPPED, STOPPING.
     */
    public String getStatus(){
        return jettyserver.getStatus();
    }

    /**
     * This method returns a Jetty statistics object.
     * @return Statistic Jetty object.
     * @since v0.4.0
     */
    public StatisticsHandler getStatistics(){
        return this.jettyserver.getStatistics();
    }
    
    /**
     * Object initialization status.
     * @return Returns true if the object is correctly initialized or false if 
     *     not.
     * @since v0.1.0
     */
    @Override
    public boolean isCorrectlyInitialized() {
        return isInitializated;
    }
}
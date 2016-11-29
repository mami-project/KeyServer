/*
 * Copyright 2016 Telefonica.
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
package es.tid.keyserver.https.jetty;

import es.tid.keyserver.config.ConfigController;
import es.tid.keyserver.controllers.db.DataBase;
import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.server.handler.InetAccessHandler;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.slf4j.LoggerFactory;

/**
 * Jetty Server Class.
 * @author <a href="mailto:jgm1986@hotmail.com">Javier Gusano Martinez</a>
 * @since v0.4.0
 */
public class KsJetty implements Runnable{
    /**
     * Jetty Server Object.
     */
    private Server server;
    /**
     * Jetty initialization flag
     */
    private boolean ready = false;
    /**
     * Logger object.
     */
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(KsJetty.class);
    /**
     * Class constructor.
     * @param parameters Jetty HTTPS service configuration object.
     * @param objDB Redis database object.
     * @since v0.4.0
     */
    public KsJetty(ConfigController parameters, DataBase objDB){
        server = new Server(new QueuedThreadPool(128, 8));
        HttpConfiguration https = getHttpStaticConfig();
        SslContextFactory sslContextFactory = new SslContextFactory();
        sslContextFactory.setKeyStorePath(parameters.getServerKeyStoreFile());
        sslContextFactory.setSessionCachingEnabled(true);
        sslContextFactory.setKeyStorePassword(parameters.getServerKeyStorePassword());
        sslContextFactory.setKeyManagerPassword(parameters.getServerKeyManagerPassword());
        // Set the SSL configuration fields.
        ServerConnector sslConnector = new ServerConnector(server,
                new SslConnectionFactory(sslContextFactory, HttpVersion.HTTP_1_1.toString()),
                new HttpConnectionFactory(https));
        // Server listener address and port.
        sslConnector.setPort(parameters.getServerPort());
        sslConnector.setHost(parameters.getServerAddress().getHostAddress());
        sslConnector.setIdleTimeout(parameters.getIdleTimeout());
        server.setConnectors(new Connector[] {sslConnector});
        // Jetty incoming requests handler.
        KeyServerJettyHandler ksHandler = new KeyServerJettyHandler(objDB);
        // Error Handler
        KsJettyErrors ksErrors = new KsJettyErrors();
        ksErrors.setServer(server);
        server.addBean(ksErrors);
        // Security Whitelist filter
        if(parameters.getServerIpWhiteList() != null){
            InetAccessHandler accessControl = new InetAccessHandler();
            for (String tmp :parameters.getServerIpWhiteList()) {
                accessControl.include(tmp);
            }
            accessControl.setHandler(ksHandler);
            server.setHandler(accessControl);
        } else {
            server.setHandler(ksHandler);
            org.slf4j.Logger SECURITY = LoggerFactory.getLogger("security");
            org.slf4j.Logger LOGGER = LoggerFactory.getLogger(KsJetty.class);
            String msg = "There is no whitelist field defined inside config file."
                    + "\n\t\tAllowing KeyServer access to all IPs!";
            SECURITY.warn(msg);
            LOGGER.warn(msg);
        }
    }
    
    /**
     * Run method for Thread execution.
     * @since v0.4.0
     */
    @Override
    public synchronized void run() {
        try {
            server.start();
            this.ready = true;
            server.join();
        } catch (Exception ex) {
            LOGGER.error("Jetty bad initialization error: {}", ex.getMessage());
        }
    }
    
    /**
     * This method returns the Jetty server object status.
     * @return String with one of the following values: FAILED, RUNNING, 
     *     STARTED, STARTING, STOPPED, STOPPING.
     * @since v0.4.0
     */
    public String getStatus(){
        return server.getState();
    }
    
    /**
     * This method stops the Jetty server.
     * @since v0.4.0
     */
    public void stop(){
        try {
            this.ready = false;
            server.stop();
        } catch (Exception ex) {
            LOGGER.error("Jetty bad stop error: {}", ex.getMessage());
        }
    }
    
    /**
     * This method is used as semaphore during Jetty configuration and launch.
     * @return  True if the Jetty object has been initialized, false if not.
     * @since v0.4.0
     */
    public boolean isReady(){
        return this.ready;
    }

    /**
     * This class returns a HTTP configuration object with specific fields.
     * @return HttpConfiguration file with the KeyServer parameters.
     * @since v0.4.0
     */
    private HttpConfiguration getHttpStaticConfig(){
        HttpConfiguration https = new HttpConfiguration();
        // Set the configuration parameters.
        https.setPersistentConnectionsEnabled(true);
        https.setIdleTimeout(0);
        https.addCustomizer(new SecureRequestCustomizer());
        https.setSendServerVersion(false);
        https.setSendDateHeader(false);
        return https;
    }
}

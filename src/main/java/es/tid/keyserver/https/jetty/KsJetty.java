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

import es.tid.keyserver.https.KeyServerTestHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.slf4j.LoggerFactory;

/**
 * Jetty Server Class.
 * @author <a href="mailto:jgm1986@hotmail.com">Javier Gusano Martinez</a>
 * @since v0.4.0
 */
public class KsJetty implements Runnable{
    /**
     * Logger object.
     */
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(KsJetty.class);
    /**
     * Jetty Server Object.
     */
    private Server server;

    @Override
    public void run() {
        System.out.println("Lanzando Jetty");

        server = new Server();
        // Access using HTTP (Only for Debug).
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(9999);
        
        HttpConfiguration https = new HttpConfiguration();
        https.addCustomizer(new SecureRequestCustomizer());
        
        SslContextFactory sslContextFactory = new SslContextFactory();
        sslContextFactory.setKeyStorePath("keystore.jks");
        sslContextFactory.setKeyStorePassword("123456");
        sslContextFactory.setKeyManagerPassword("123456");

        ServerConnector sslConnector = new ServerConnector(server,
                new SslConnectionFactory(sslContextFactory, "http/1.1"),
                new HttpConnectionFactory(https)
        );
        sslConnector.setPort(9998);
        server.setConnectors(new Connector[] {connector, sslConnector});
        server.setHandler(new KeyServerTestHandler());
        System.out.println(server.getState());
        try {
            server.start();
            System.out.println(server.getState());
            server.dumpStdErr();
            server.join();
        } catch (Exception ex) {
            System.out.println("Error al lanzar Jetty");
            // @TODO: Implements logger side.
            Logger.getLogger(KsJetty.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String getStatus(){
        return server.getState();
    }
    
    public void stop(){
        try {
            server.stop();
        } catch (Exception ex) {
            // @TODO: Implements logger side.
            Logger.getLogger(KsJetty.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

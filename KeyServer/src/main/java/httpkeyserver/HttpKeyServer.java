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
package httpkeyserver;

import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsParameters;
import com.sun.net.httpserver.HttpsServer;
import config.ConfigFile;
import database.DataBase;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.TrustManagerFactory;
import keyserver.CheckObject;
import org.slf4j.LoggerFactory;

/**
 * Class for HTTP Server.
 * @author <a href="mailto:jgm1986@hotmail.com">Javier Gusano Martinez</a>
 */
public class HttpKeyServer implements CheckObject{
    /**
     * Logging object.
     */
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(HttpKeyServer.class);
    
    /**
     * Server Object.
     */
    private HttpsServer server;
    /**
     * Flag for check if the object is correctly initialized.
     */
    boolean isInitializated = false;
    /**
     * Main constructor for the HTTPS Server class.
     * @param parameters Object with program parameters.
     * @param objDB REDIS database Object.
     * @see <a href="http://stackoverflow.com/questions/2308479/simple-java-https-server">More info about HttpsServer</a>
     */
    public HttpKeyServer(ConfigFile parameters, DataBase objDB){
        try {
            // Getting the Server parameters from properties file.
            int port = Integer.parseInt(parameters.getParameter("serverPort"));
            int backlog = Integer.parseInt(parameters.getParameter("serverBacklog"));
            InetAddress ipaddress = InetAddress.getByName(parameters.getParameter("serverAddress"));
            InetSocketAddress address = new InetSocketAddress ( ipaddress, port );
            // Create basic Server object
            server = HttpsServer.create();
            SSLContext sslContext = SSLContext.getInstance("TLS");
            // initialise the keystore
            char[] keystorePassword = parameters.getParameter("serverKeyPass").toCharArray();
            KeyStore ks = KeyStore.getInstance("JKS");
            ks.load(new FileInputStream(parameters.getParameter("serverKeyFile")), keystorePassword);
            // setup the key manager factory
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(ks, keystorePassword);
            // setup the trust manager factory
            TrustManagerFactory tmf = TrustManagerFactory.getInstance ( "SunX509" );
            tmf.init ( ks );
            // Setup the HTTPs context and parameters.
            sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
            //HttpsConfigurator configurator = new HttpsConfigurator(sslContext);
            server.setHttpsConfigurator(new HttpsConfigurator(sslContext){
                /**
                 * A HttpsServer must have an associated HttpsConfigurator 
                 * object which is used to establish the SSL configuration for 
                 * the SSL connections. 
                 * Applications need to override the configure(HttpsParameters) 
                 * method in order to change the default configuration. 
                 * @param params The HttpsParameters to be configured.
                 */
                @Override
                public void configure(HttpsParameters params){
                    try {
                        // Initialise the SSL context
                        SSLContext c = SSLContext.getDefault();
                        SSLEngine engine = c.createSSLEngine();
                        params.setNeedClientAuth(false);
                        params.setCipherSuites(engine.getEnabledCipherSuites());
                        params.setProtocols(engine.getEnabledProtocols());
                        // Get the default parameters
                        SSLParameters defaultSSLParameters = c.getDefaultSSLParameters();
                        params.setSSLParameters(defaultSSLParameters);
                    } catch (NoSuchAlgorithmException ex) {
                        logger.error("Problem with SSL context parameters.");
                        logger.trace("Exceiption message: {}", ex.toString());
                    }
                }
            });
            // Setting config for the server and accepting only BACKLOG variable
            //  as maximum input conection (0 = System default).
            server.bind(address, backlog);
            IncomingRequestProcess processor =new IncomingRequestProcess(objDB);
            server.createContext("/", processor);
            server.setExecutor(processor);
            // Starting server
            server.start();
            isInitializated = true;
        } catch (IOException ex) {
            logger.error("HTTP incomming petitions server cannot be initialized.");
            logger.trace("Exceiption message: {}", ex.toString());
        } catch (NoSuchAlgorithmException ex) {
            logger.error("HTTP incomming petitions server cannot be initialized.");
            logger.trace("Exceiption message: {}", ex.toString());
        } catch (KeyStoreException ex) {
            logger.error("HTTP incomming petitions server cannot be initialized.");
            logger.trace("Exceiption message: {}", ex.toString());
        } catch (CertificateException ex) {
            logger.error("HTTP incomming petitions server cannot be initialized.");
            logger.trace("Exceiption message: {}", ex.toString());
        } catch (UnrecoverableKeyException ex) {
            logger.error("HTTP incomming petitions server cannot be initialized.");
            logger.trace("Exceiption message: {}", ex.toString());
        } catch (KeyManagementException ex) {
            logger.error("HTTP incomming petitions server cannot be initialized.");
            logger.trace("Exceiption message: {}", ex.toString());
        }
    }
    
    /**
     * Stops the HTTPS KeyServer closing the listening socket and disallowing any new exchanges from being processed.
     * The time specified as timeout is 5 seconds for this method.
     */
    public void stop(){
        server.stop(5);
    }

    /**
     * Return true if the object is correctly initialized or false if not.
     * @return Object initialization status.
     */
    @Override
    public boolean isCorrectlyInitialized() {
        return isInitializated;
    }
}
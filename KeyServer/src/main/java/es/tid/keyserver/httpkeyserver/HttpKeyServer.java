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
package es.tid.keyserver.httpkeyserver;

import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsParameters;
import com.sun.net.httpserver.HttpsServer;
import es.tid.keyserver.config.ConfigFile;
import es.tid.keyserver.database.DataBase;
import es.tid.keyserver.httpkeyserver.whitelist.WhiteList;
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
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.TrustManagerFactory;
import es.tid.keyserver.keyserver.CheckObject;
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
    private boolean isInitializated = false;
    /**
     * Main constructor for the HTTPS Server class.
     * @param parameters Object with program parameters.
     * @param objDB REDIS database Object.
     * @see <a href="http://stackoverflow.com/questions/2308479/simple-java-https-server">More info about HttpsServer</a>
     */
    public HttpKeyServer(final ConfigFile parameters, DataBase objDB){
        try {
            // Getting the Server parameters from properties file.
            int port = Integer.parseInt(parameters.getParameter("serverPort"));
            int backlog = Integer.parseInt(parameters.getParameter("serverBacklog"));
            InetAddress ipaddress = InetAddress.getByName(parameters.getParameter("serverAddress"));
            InetSocketAddress address = new InetSocketAddress ( ipaddress, port );
            // Create basic Server object
            server = HttpsServer.create();
            SSLContext sslContext = SSLContext.getInstance(parameters.getParameter("serverSSLContext"));
            // Initialize the key store object
            char[] keystorePassword = parameters.getParameter("serverKeyPass").toCharArray();
            KeyStore ks = KeyStore.getInstance(parameters.getParameter("serverKeyStore"));
            ks.load(new FileInputStream(parameters.getParameter("serverKeyFile")), keystorePassword);
            // setup the key manager factory
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(parameters.getParameter("serverKeyManagerFactory"));
            kmf.init(ks, keystorePassword);
            // setup the trust manager factory
            TrustManagerFactory tmf = TrustManagerFactory.getInstance (parameters.getParameter("serverTrustManagerFactory"));
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
                        // Initialize the SSL context
                        SSLContext c = SSLContext.getDefault();
                        SSLEngine engine = c.createSSLEngine();
                        params.setNeedClientAuth(false);
                        params.setCipherSuites(engine.getEnabledCipherSuites());
                        params.setProtocols(engine.getEnabledProtocols());
                        // Get the default parameters
                        SSLParameters defaultSSLParameters = c.getDefaultSSLParameters();
                        
                        if(parameters.containsKey("serverCiphersSuites") && getCiphers(parameters)!=null){
                            String [] ciphersuites = getCiphers(parameters);
                            defaultSSLParameters.setCipherSuites(ciphersuites);
                        }
                        params.setSSLParameters(defaultSSLParameters);
                    } catch (NoSuchAlgorithmException ex) {
                        logger.error("Problem with SSL context parameters.");
                        logger.trace("Exceiption message: {}", ex.toString());
                    }
                }  
                /**
                 * This method parse the field string to array of values with 
                 * the ciphers names. If the configuration label is not present
                 * inside configuration file. This, will use all available 
                 * ciphers by default.
                 * @param parameters Configuration file object.
                 * @return Array of strings with the ciphers name.
                 */
                private String[] getCiphers(ConfigFile parameters) {
                    String [] returnValue = null;
                    String cipString = parameters.getParameter("serverCiphersSuites");
                    if(!cipString.equalsIgnoreCase("")){
                        List<String> items = Arrays.asList(cipString.split("\\s*,\\s*"));
                        returnValue = (String[]) items.toArray();
                    }
                    //logger.trace("List {} of ciphers inside KeyServer configuration file: {}", returnValue.length, returnValue);
                    return returnValue;
                }
            });
            // Setting configuration for the server and accepting only BACKLOG variable
            //  as maximum input connection (0 = System default).
            server.bind(address, backlog);
            WhiteList allowedIPs = new WhiteList(parameters.getParameter("whiteList"));
            IncomingRequestProcess processor =new IncomingRequestProcess(objDB, allowedIPs);
            server.createContext("/", processor);
            server.setExecutor(new Executor(){
                /**
                 * Launch the runner for the incoming HTTP request. This method
                 * call to the handle method to process the incoming HTTP proxy
                 * request.
                 * @param r Runnable object for the incoming petition.
                 */
                @Override
                public void execute(Runnable r) {
                    r.run();
                }
            });
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
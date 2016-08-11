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

import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsParameters;
import com.sun.net.httpserver.HttpsServer;
import es.tid.keyserver.config.ConfigController;
import es.tid.keyserver.controllers.db.DataBase;
import es.tid.keyserver.controllers.security.ip.WhiteList;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.TrustManagerFactory;
import es.tid.keyserver.core.lib.CheckObject;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.slf4j.LoggerFactory;

/**
 * Class for HTTP Server.
 * @author <a href="mailto:jgm1986@hotmail.com">Javier Gusano Martinez</a>
 * @since v0.1.0
 */
public class HttpsServerController implements CheckObject{
    /**
     * Logging object.
     */
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(HttpsServerController.class);
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
     * @param ks KeyStore object for HTTPs server.
     * @param ksPass Password used to open the KeyStore object.
     * @see <a href="http://stackoverflow.com/questions/2308479/simple-java-https-server">More info about HttpsServer</a>
     * @since v0.1.0
     */
    public HttpsServerController(ConfigController parameters, DataBase objDB, KeyStore ks, String ksPass){
        try {
            // Getting the Server parameters from configuration Object.
            int port = parameters.getServerPort();
            int backlog = parameters.getServerBacklog();
            InetAddress ipaddress = parameters.getServerAddress();
            InetSocketAddress address = new InetSocketAddress ( ipaddress, port );
            // Create basic Server object
            server = HttpsServer.create();
            SSLContext sslContext = SSLContext.getInstance(parameters.getServerSSLContext());
            // Setup the key manager factory
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(parameters.getServerKeyManagerFactory());
            kmf.init(ks, ksPass.toCharArray());
            // Setup the trust manager factory
            TrustManagerFactory tmf = TrustManagerFactory.getInstance (parameters.getServerTrustManagerFactory());
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
                        
                        if((parameters.getServerCiphersSuites()!=null) && 
                                (getCiphers(parameters.getServerCiphersSuites())!=null)){
                            String [] ciphersuites = getCiphers(parameters.getServerCiphersSuites());
                            defaultSSLParameters.setCipherSuites(ciphersuites);
                        }
                        params.setSSLParameters(defaultSSLParameters);
                    } catch (NoSuchAlgorithmException ex) {
                        // Error level.
                        LOGGER.error("Problem with SSL context parameters.");
                        // Trace level.
                        StringWriter errors = new StringWriter();
                        ex.printStackTrace(new PrintWriter(errors));
                        LOGGER.trace("Exception message: {}", errors.toString());
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
                private String[] getCiphers(String cipString) {
                    String [] returnValue = null;
                    if(!cipString.equalsIgnoreCase("")){
                        List<String> items = Arrays.asList(cipString.split("\\s*,\\s*"));
                        returnValue = (String[]) items.toArray();
                    }
                    LOGGER.trace("List {} of ciphers inside KeyServer configuration file: {}", returnValue.length, returnValue);
                    return returnValue;
                }
            });
            // Setting configuration for the server and accepting only BACKLOG variable
            //  as maximum input connection (0 = System default).
            server.bind(address, backlog);
            WhiteList allowedIPs = new WhiteList(parameters.getWhiteList());
            IncomingRequestProcess processor =new IncomingRequestProcess(objDB, allowedIPs);
            server.createContext("/", processor);
            server.setExecutor(new Executor(){
                /**
                 * Launch the runner for the incoming HTTP request. This method
                 * call to the handle method to process the incoming HTTP proxy
                 * request.
                 * @param r Runnable object for the incoming request.
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
            // Error level.
            LOGGER.error("HTTP incomming requests server cannot be initialized.");
            // Trace level.
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            LOGGER.trace("Exception message: {}", errors.toString());
        } catch (NoSuchAlgorithmException ex) {
            // Error level.
            LOGGER.error("HTTP server requests has produced NoSuchAlgoritmException.");
            // Trace level.
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            LOGGER.trace("Exception message: {}", errors.toString());
        } catch (KeyStoreException ex) {
            // Error level.
            LOGGER.error("HTTP server certificate has produced a KeyStoreException.");
            // Trace level.
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            LOGGER.trace("Exception message: {}", errors.toString());
        } catch (UnrecoverableKeyException ex) {
            // Error level.
            LOGGER.error("HTTP server certificate has produced a UnrecoverableKeyException.");
            // Trace level.
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            LOGGER.trace("Exception message: {}", errors.toString());
        } catch (KeyManagementException ex) {
            // Error level.
            LOGGER.error("HTTP server certificate has produced a KeyManagementException.");
            // Trace level.
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            LOGGER.trace("Exception message: {}", errors.toString());
        }
    }
    
    /**
     * Stops the HTTPS KeyServer closing the listening socket and disallowing 
     * any new exchanges from being processed.
     * <p>
     * The value specified as timeout for this method is 5 seconds.
     * @since v0.1.0
     */
    public void stop(){
        server.stop(5);
    }

    /**
     * Object initialization status.
     * @return Returns true if the object is correctly initialized or false if 
     * not.
     * @since v0.1.0
     */
    @Override
    public boolean isCorrectlyInitialized() {
        return isInitializated;
    }
}
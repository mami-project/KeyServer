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
package es.tid.keyserver.config;

import es.tid.keyserver.config.keyserver.ConfigFile;
import es.tid.keyserver.config.maven.Maven;
import es.tid.keyserver.core.lib.CheckObject;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.slf4j.LoggerFactory;

/**
 * This class contains all necessary to extract all KeyServer configuration 
 * elements from properties files.
 * @author <a href="mailto:jgm1986@hotmail.com">Javier Gusano Martinez</a>
 * @since v0.3.0
 */
public class ConfigController implements CheckObject{
    /**
     * Logging object.
     */
    private static org.slf4j.Logger logger;
    /**
     * Maven project data object.
     */
    private final Maven mavenData;
    /**
     * KeyServer configuration data object.
     */
    private final ConfigFile keyserverConfig;
    
    /**
     * Class constructor.
     * @param mvnFileRoute String with the filename with Maven project 
     * properties.
     * @param ksFileRoute String with the filename with the KeyServer 
     * configuration file.
     * @param ksRequiredFields String array with the required fields on 
     * KeyServer configuration file.
     * @since v0.3.0
     */
    public ConfigController(String mvnFileRoute, String ksFileRoute, String [] ksRequiredFields) {
        logger = LoggerFactory.getLogger(ConfigController.class);
        // Instantiation of configuration objects.
        mavenData = new Maven(mvnFileRoute);
        keyserverConfig = new ConfigFile(ksFileRoute, ksRequiredFields); 
    }
    
    /**
     * Method used to check if the configuration file objects are correctly 
     * initialized.
     * @return True if all objects are correctly initialized. False if not.
     * @since v0.3.0
     */
    @Override
    public boolean isCorrectlyInitialized() {
        return (
                this.keyserverConfig.isCorrectlyInitialized() && 
                this.mavenData.isCorrectlyInitialized()
                );
    }
    
    /**
     * This method is used to get the current KeyServer application version.
     * @return String with the version.
     * @since v0.3.0
     */
    public String getVersion() {
        return this.mavenData.getVersion();
    }

    /**
     * This method is used to get the current KeyServer application name.
     * @return String with the name.
     * @since v0.3.0
     */
    public String getAppName() {
        return this.mavenData.getAppName();
    }
    
    /**
     * Returns the public repository URL as string.
     * @return String with the public GitHub URL.
     * @since v0.3.0
     */
    public String getProjectPublicUrl(){
        return this.mavenData.getProjectPublicUrl();
    }
    
    /**
     * This method is used to get the listener KeyServer IP address.
     * @return InetAddress object with the KeyServer IP address. If the field is not present,
     * returns 'null'.
     * @since v0.3.0
     */
    public InetAddress getServerAddress(){
        String address = this.keyserverConfig.getServerAddress();
        try {
            return InetAddress.getByName(address);
        } catch (UnknownHostException ex) {
            // Error level.
            logger.error("Unknown Host Exception with the server IP addres: {}", address);
            // Trace level.
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            logger.trace(errors.toString());
            return null;
        }
    }
    
    /**
     * This method is used to get the listener KeyServer port.
     * @return KeyServer port as integer. If the field is not present,
     * returns -1 value.
     * @since v0.3.0
     */
    public int getServerPort(){
        String port = this.keyserverConfig.getServerPort();
        if(port != null){
            return Integer.parseInt(port);
        } else {
            // Error level.
            logger.error("Not valid HTTPS port specified for the KeyServer: {}", port);
            return -1;
        }
    }

    /**
     * This method is used to get the KeyServer HTTPS SSL Context. 
     * @return String with the KeyServer HTTPS SSL Context. If the field is not 
     * present, returns 'null'.
     * <p>
     * This is an example with valid values for this field:
     * <ul>
     *  <li>SSLv2</li>
     *  <li>SSLv3</li>
     *  <li>TLS</li>
     *  <li>TLSv1</li>
     *  <li>TLSv1.1</li>
     *  <li>TLSv1.2</li>
     * </ul> 
     * @since v0.3.0
     */
    public String getServerSSLContext(){
        return this.keyserverConfig.getServerSSLContext();
    }
    
    /**
     * This method is used to get the certificate for KeyServer HTTPS server.
     * @return String with the certificate name and route (if is available). If 
     * the field is not present, returns 'null'.
     * @since v0.3.0
     */
    public String getServerKeyFile(){
        return this.keyserverConfig.getServerKeyFile();
    }
    
    /**
     * This method is used to get password of the KeyServer HTTPS certificate.
     * @return String with the KeyServer HTTPS certificate password. If the 
     * field is not present, returns 'null'.
     * @since v0.3.0
     */
    public String getServerKeyPass(){
        return this.keyserverConfig.getServerKeyPass();
    }
    
    /**
     * This method is used to get the Server Backlog value from the 
     * configuration file.
     * @return Integer with the KeyServer Backlog value. If the field is not 
     * present, it returns -1 value.
     * @since v0.3.0
     */
    public int getServerBacklog(){
        String backlog = this.keyserverConfig.getServerBacklog();
        if(backlog != null){
            return Integer.parseInt(backlog);
        } else {
            // Error level.
            logger.error("Not valid Backlog parammeter specified on KeyServer config file: {}", backlog);
            return -1;
        }
    }

    /**
     * This method is used to get the KeyServer HTTPS certificate manager 
     * factory. 
     * @return String with the KeyServer HTTPS certificate manager factory. 
     * If the field is not present, returns 'null'.
     * <p>
     * This is an example with valid values for this field:
     * <ul>
     *  <li>PKIX</li>
     *  <li>SunX509</li>
     * </ul> 
     * @since v0.3.0
     */
    public String getServerKeyManagerFactory(){
        return this.keyserverConfig.getServerKeyManagerFactory();
    }
    
    /**
     * This method is used to get the KeyServer HTTPS certificate trust manager 
     * factory. 
     * @return String with the KeyServer HTTPS certificate trust manager factory. 
     * If the field is not present, returns 'null'.
     * <p>
     * This is an example with valid values for this field:
     * <ul>
     *  <li>PKIX (X509 or SunPKIX)</li>
     *  <li>SunX509</li>
     * </ul> 
     * @since v0.3.0
     */
    public String getServerTrustManagerFactory(){
        return this.keyserverConfig.getServerTrustManagerFactory();
    }
    
    /**
     * This method is used to get the KeyServer HTTPS certificate key store. 
     * @return String with the KeyServer HTTPS certificate key store. 
     * If the field is not present, returns 'null'.
     * <p>
     * This is an example with valid values for this field:
     * <ul>
     *  <li>jceks</li>
     *  <li>jks</li>
     *  <li>pkcs12</li>
     * </ul> 
     * @since v0.3.0
     */
    public String getServerKeyStore(){
        return this.keyserverConfig.getServerKeyStore();
    }
    
    /**
     * This method is used to get the KeyServer HTTPS cipher suites. 
     * @return String with the KeyServer HTTPS ciphers suites. If the field is 
     * not present, returns 'null'. The ciphers names are separated with commas.
     * <p>
     * This is an example with valid values for this field:
     * <ul>
     *  <li>TLS_DHE_DSS_WITH_AES_128_GCM_SHA256</li>
     *  <li>TLS_DHE_DSS_WITH_AES_128_CBC_SHA256</li>
     *  <li>TLS_DHE_DSS_WITH_AES_128_CBC_SHA</li>
     *  <li>SSL_DHE_DSS_WITH_3DES_EDE_CBC_SHA</li>
     *  <li>...</li>
     * </ul> 
     * @since v0.3.0
     */
    public String getServerCiphersSuites(){
        return this.keyserverConfig.getServerCiphersSuites();
    }
    
    
    /**
     * This method is used to get the Redis Database server address.
     * @return InetAddress object with the Redis Database server address. If the 
     * field is not present, returns 'null'.
     * @since v0.3.0
     */
    public InetAddress getDbAddress(){
        String address = this.keyserverConfig.getDbAddress();
        try {
            return InetAddress.getByName(address);
        } catch (UnknownHostException ex) {
            // Error level.
            logger.error("Unnknown Host Exception with Redis Dtabase IP address: {}", address);
            // Trace level.
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            logger.trace(errors.toString());
            return null;
        }
    }
    
    /**
     * This method is used to get Redis Database server port.
     * @return Integer with the Redis Database Port. If the field is not present,
     * returns -1 value.
     * @since v0.3.0
     */
    public int getDbPort(){
        String port = this.keyserverConfig.getDbPort();
        if(port != null){
            return Integer.parseInt(port);
        } else {
            // Error level.
            logger.error("Not valid port specified for the Redis Database: {}", port);
            return -1;
        }
    }
    
    /**
     * This method is used to get Redis Database password.
     * @return Integer with the Redis Database password. If the field is not present,
     * returns `null`.
     * @since v0.3.1
     */
    public String getDbPassword(){
        String password = this.keyserverConfig.getDbPassword();
        if(password != null){
            return password;
        } else {
            // Error level.
            logger.error("Not valid password specified for the Redis Database: {}", password);
            return null;
        }
    }
    
    /**
     * This method is used to get the IP whitelist file name for KeyServer 
     * access control.
     * @return String with the whitelist file name. If the field is not present,
     * returns 'null'.
     * @since v0.3.0
     */
    public String getWhiteList(){
        return this.keyserverConfig.getWhiteList();
    }
}

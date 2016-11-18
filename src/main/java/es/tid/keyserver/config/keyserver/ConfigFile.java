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
package es.tid.keyserver.config.keyserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Properties;
import es.tid.keyserver.core.lib.CheckObject;
import org.slf4j.LoggerFactory;

/**
 * Class for KeyServer external configuration files.
 * @author <a href="mailto:jgm1986@hotmail.com">Javier Gusano Martinez</a>
 * @since v0.1.0
 */
public class ConfigFile implements CheckObject{
    /**
     * Logging object.
     */
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ConfigFile.class);
    /**
     * Property object with configuration parameters.
     */
    private Properties configFile;
    /**
     * Flag for check if the object is correctly initialized.
     */
    private boolean initStatus;
    
    /**
     * Class constructor.
     * @param fileRoute Contains the name and route to the external 
     *     configuration file.
     * @param requiredFields Fields required inside configuration file.
     * @since v0.1.0
     */
    public ConfigFile(String fileRoute, String [] requiredFields){
        File propertiesFile = new File(fileRoute);
        String fileLocation;
        if((propertiesFile.exists() && propertiesFile.canRead())){
            fileLocation = fileRoute;
        } else {
            LOGGER.warn("Can't access to the specified config file or "
                    + "doesn't exists: {}", fileRoute);
            LOGGER.info("New config file on default location...");
            fileLocation = "general.properties";
            if(!newDefaultProperties(fileLocation)){
                // If the default properties file can't be created correctly,
                //  enable error flag and exit from the class constructor.
                initStatus = false;
                return;
            }
        }
        configFile = new Properties();
        // Load the configuration file parameters.
        try {
            configFile.load(new FileInputStream(fileLocation));
            this.initStatus = checkFieldsPresent(requiredFields);
        } catch (IOException ex) {
            initStatus = false;
            // Error level.
            LOGGER.error("Can't load the KeyServer configuration file: {}", fileRoute);
            // Trace level.
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            LOGGER.trace(errors.toString());
        }
    }
    
    /**
     * Return true if the object is correctly initialized or false if not.
     * @return Object initialization status.
     * @since v0.1.0
     */
    @Override
    public boolean isCorrectlyInitialized() {
        return initStatus;
    }
    
    /**
     * This method is used to get the listener KeyServer IP address.
     * @return String with the KeyServer IP address. If the field is not present,
     *     returns 'null'.
     * @since v0.3.0
     */
    public String getServerAddress(){
        return this.getParameter("serverAddress");
    }
    
    /**
     * This method is used to get the listener KeyServer port.
     * @return String with the KeyServer port. If the field is not present,
     *     returns 'null'.
     * @since v0.3.0
     */
    public String getServerPort(){
        return this.getParameter("serverPort");
    }

    /**
     * This method is used to get the KeyServer HTTPS SSL Context. 
     * @return String with the KeyServer HTTPS SSL Context. If the field is not 
     *     present, returns 'null'.
     * 
     *     <p>This is an example with valid value for this field:
     *     <ul>
     *      <li>SSLv2</li>
     *      <li>SSLv3</li>
     *      <li>TLS</li>
     *      <li>TLSv1</li>
     *      <li>TLSv1.1</li>
     *      <li>TLSv1.2</li>
     *     </ul> 
     * @since v0.3.0
     */
    public String getServerSSLContext(){
        return this.getParameter("serverSSLContext");
    }
    
    /**
     * This method is used to get the certificate for KeyServer HTTPS server.
     * @return String with the certificate name and route (if is available). If 
     *     the field is not present, returns 'null'.
     * @since v0.3.0
     */
    public String getServerKeyFile(){
        return this.getParameter("serverKeyFile");
    }
    
    /**
     * This method is used to get password of the KeyServer HTTPS certificate.
     * @return String with the KeyServer HTTPS certificate password. If the 
     *     field is not present, returns 'null'.
     * @since v0.3.0
     */
    public String getServerKeyPass(){
        return this.getParameter("serverKeyPass");
    }
    
    /**
     * This method is used to get the Server Backlog value from the 
     *     configuration file.
     * @return String with the KeyServer Backlog value. If the field is not 
     *     present, it returns 'null'.
     * @since v0.3.0
     */
    public String getServerBacklog(){
        return this.getParameter("serverBacklog");
    }

    /**
     * This method is used to get the KeyServer HTTPS certificate manager 
     *     factory. 
     * @return String with the KeyServer HTTPS certificate manager factory. 
     *     If the field is not present, returns 'null'.
     * 
     *     <p>This is an example with valid values for this field:
     *     <ul>
     *      <li>PKIX</li>
     *      <li>SunX509</li>
     *     </ul> 
     * @since v0.3.0
     */
    public String getServerKeyManagerFactory(){
        return this.getParameter("serverKeyManagerFactory");
    }
    
    /**
     * This method is used to get the KeyServer HTTPS certificate trust manager 
     *     factory. 
     * @return String with the KeyServer HTTPS certificate trust manager factory. 
     *     If the field is not present, returns 'null'.
     * 
     *     <p>This is an example with valid value for this field:
     *     <ul>
     *      <li>PKIX (X509 or SunPKIX)</li>
     *      <li>SunX509</li>
     *     </ul> 
     * @since v0.3.0
     */
    public String getServerTrustManagerFactory(){
        return this.getParameter("serverTrustManagerFactory");
    }
    
    /**
     * This method is used to get the KeyServer HTTPS certificate key store. 
     * @return String with the KeyServer HTTPS certificate key store. 
     *     If the field is not present, returns 'null'.
     * 
     *     <p>This is an example with valid value for this field:
     *     <ul>
     *      <li>jceks</li>
     *      <li>jks</li>
     *      <li>pkcs12</li>
     *     </ul> 
     * @since v0.3.0
     */
    public String getServerKeyStore(){
        return this.getParameter("serverKeyStore");
    }
    
    /**
     * This method is used to get the KeyServer HTTPS cipher suites. 
     * @return String with the KeyServer HTTPS ciphers suites. If the field is 
     *     not present, returns 'null'. The ciphers names are separated with commas.
     * 
     *     <p>This is an example with valid value for this field:
     *     <ul>
     *      <li>TLS_DHE_DSS_WITH_AES_128_GCM_SHA256</li>
     *      <li>TLS_DHE_DSS_WITH_AES_128_CBC_SHA256</li>
     *      <li>TLS_DHE_DSS_WITH_AES_128_CBC_SHA</li>
     *      <li>SSL_DHE_DSS_WITH_3DES_EDE_CBC_SHA</li>
     *     </ul> 
     * @since v0.3.0
     */
    public String getServerCiphersSuites(){
        return this.getParameter("serverCiphersSuites");
    }
    
    
    /**
     * This method is used to get the Redis Database server address.
     * @return String with the Redis Database server address. If the 
     *     field is not present, returns 'null'.
     * @since v0.3.0
     */
    public String getDbAddress(){
        return this.getParameter("dbAddress");
    }
    
    /**
     * This method is used to get Redis Database server port.
     * @return String with the Redis Database Port. If the field is not present,
     *     returns 'null'.
     * @since v0.3.0
     */
    public String getDbPort(){
        return this.getParameter("dbPort");
    }
    
    /**
     * This method is used to get Redis Database password.
     * @return String with the Redis Database Password. If the field is not
     *     present, returns 'null'.
     * @since v0.3.1
     */
    public String getDbPassword(){
        return this.getParameter("dbPassword");
    }
    
    /**
     * This method is used to get Redis Database index. This index specified the
     * database where KeyServer will be use.
     * @return String with the Redis Database index. If the field is not
     *     present, returns 'null'.
     * @since v0.3.3
     */
    public String getDbIndex(){
        return this.getParameter("dbIndex");
    }
    
    /**
     * This method is used to get the IP whitelist file name for KeyServer 
     *     access control.
     * @return String with the whitelist file name. If the field is not present,
     *     returns 'null'.
     * @since v0.3.0
     */
    public String getWhiteList(){
        return this.getParameter("whiteList");
    }
    
    /**
     * This method is used to get the DB time interval when the PING will be 
     * send. The Redis DB connection status is monitored periodically in a 
     * parallel thread.
     * @return String with the value. If the field is not present, returns 'null'.
     * @since v0.3.0
     */
    public String getChkDbInterval(){
        return this.getParameter("dbCheckInterval");
    }
    
    /**
     * This method is used to set the interval value in milliseconds for check
     * if there are a new KeyServer version available on GitHub.
     * This interval is used to verify if the KeyServer HTTPS server certificate
     * has been expired.
     * @return String with the value. If the field is not present, returns 'null'.
     * @since v0.3.3
     */
    public String getChkUpdateInterval(){
        return this.getParameter("ksCheckUpdates");
    }
    
    /**
     * Method used to create a new configuration file on specific route with 
     *     default parameters.
     * @param fileLocation Route and name to the new configuration file. By 
     *     default use "general.properties"
     * @return True if all works correctly, false if something goes wrong.
     * @since v0.1.0
     */
    private boolean newDefaultProperties(String fileLocation) {   
        try {
            FileOutputStream newConfigFile = new FileOutputStream(fileLocation);
            Properties defaultParameters = new Properties();
            // Default parameters:
            defaultParameters.setProperty("serverAddress", "0.0.0.0");
            defaultParameters.setProperty("serverPort", "443");
            defaultParameters.setProperty("serverSSLContext", "TLSv1.2");
            defaultParameters.setProperty("serverKeyFile","HTTPS_keystore.ks");
            defaultParameters.setProperty("serverKeyPass","123456");
            defaultParameters.setProperty("serverBacklog", "0");
            defaultParameters.setProperty("serverKeyManagerFactory", "SunX509");
            defaultParameters.setProperty("serverTrustManagerFactory", "SunX509");
            defaultParameters.setProperty("serverKeyStore", "JKS");
            defaultParameters.setProperty("dbAddress","127.0.0.1");
            defaultParameters.setProperty("dbPort", "6379");
            defaultParameters.setProperty("dbPassword", "foobared"); // Default password for Redis config file.
            defaultParameters.setProperty("whiteList", "IP_whitelist.txt");
            // Save parameters on file
            defaultParameters.store(newConfigFile, null);
            // Close configuration file.
            newConfigFile.close();
        } catch (FileNotFoundException ex) {
            // Error level.
            LOGGER.error("Can't create a new config file with default parameters. File not found.");
            // Trace level.
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            LOGGER.trace(errors.toString());
            return false;
        } catch (IOException ex) {
            // Error level.
            LOGGER.error("Can't create a new config file with default parameters. IO exception.");
            // Trace level.
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            LOGGER.trace(errors.toString());
            return false;
        }
        return true;
    }
    
    /**
     * Checks if the required fields inside KeyServer configuration file are present.
     * @param fields Array with the fields name.
     * @return True if all is present, false if not.
     * @since v0.1.0
     */
    private boolean checkFieldsPresent(String [] fields){
        for (String field : fields) {
            if (!configFile.containsKey(field)) {
            	// Error level.
                LOGGER.error("A neccessary configuration field is not present. Please "
                		+ "set this field : {}  on KeyServer configuration file.", field);
                return false;
            }
        }
        return true;
    }
    
    /**
     * This method is used to get a value from a configuration key.
     * @param param Label of the configuration parameter.
     * @return Value associated to the configuration parameter specified as 
     *     input. Null if the label doesn't exists.
     * @since v0.3.0
     */
    private String getParameter(String param){
        return configFile.getProperty(param);
    }
}

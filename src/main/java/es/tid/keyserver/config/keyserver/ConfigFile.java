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

import es.tid.keyserver.core.lib.CheckObject;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;

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
            LOGGER.info("New config file on default location: {}", fileRoute);
            fileLocation = fileRoute;
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
     * This method is used to get the certificate for KeyServer HTTPS server.
     * @return String with the certificate name and route (if is available). If 
     *     the field is not present, returns 'null'.
     * @since v0.3.0
     */
    public String getServerKeyStoreFile(){
        return this.getParameter("serverKeyStoreFile");
    }
    
    /**
     * This method is used to get password of the KeyServer HTTPS certificate 
     * key store.
     * @return String with the KeyServer HTTPS certificate password. If the 
     *     field is not present, returns 'null'.
     * @since v0.3.0
     */
    public String getKeyStorePassword(){
        return this.getParameter("serverKeyStorePassword");
    }
    
    /**
     * This method is used to get password of the KeyServer HTTPS certificate 
     * key manager.
     * @return String with the KeyServer HTTPS certificate password. If the 
     *     field is not present, returns 'null'.
     * @since v0.4.0
     */
    public String getKeyManagerPassword(){
        return this.getParameter("serverKeyManagerPassword");
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
     * The time in milliseconds that the connection can be idle before it is 
     * closed.
     * @return String with the value. If the field is not present, returns 'null'.
     * @since v0.4.0
     */
    public String getServerIdleTimeout() {
        return this.getParameter("serverIdleTimeout");
    }
    
    /**
     * Jetty server authorized access IP address list.
     * @return String with the IPs authorized. If the field is not present, 
     *     all hosts will be authorized.
     * @since v0.4.0
     */
    public String getServerIpWhiteList() {
        return this.getParameter("serverIpWhiteList");
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
            File file = new File(fileLocation);
            file.getParentFile().mkdir();
            file.createNewFile();
            FileOutputStream newConfigFile = new FileOutputStream(fileLocation);
            Properties defaultParameters = new Properties();
            // Check updates interval:
            defaultParameters.setProperty("ksCheckUpdates", "3600000");
            // Default parameters:
            defaultParameters.setProperty("serverAddress", "0.0.0.0");
            defaultParameters.setProperty("serverPort", "1443");
            defaultParameters.setProperty("serverKeyStoreFile","config/ksserverkey.jks");
            defaultParameters.setProperty("serverKeyStorePassword","123456");
            defaultParameters.setProperty("serverKeyManagerPassword","123456");
            defaultParameters.setProperty("serverIdleTimeout", "30000");
            defaultParameters.setProperty("dbAddress","127.0.0.1");
            defaultParameters.setProperty("dbPort", "6379");
            defaultParameters.setProperty("dbPassword", "foobared"); // Default password for Redis config file.
            defaultParameters.setProperty("dbCheckInterval", "1000");
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
                LOGGER.error("A necessary configuration field is not present. Please "
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

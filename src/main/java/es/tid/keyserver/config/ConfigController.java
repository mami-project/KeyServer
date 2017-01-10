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
package es.tid.keyserver.config;

import es.tid.keyserver.config.keyserver.ConfigFile;
import es.tid.keyserver.config.maven.Maven;
import es.tid.keyserver.core.lib.CheckObject;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * This class contains all necessary to extract all KeyServer configuration 
 *     elements from properties files.
 * @author <a href="mailto:jgm1986@hotmail.com">Javier Gusano Martinez</a>
 * @since v0.3.0
 */
public class ConfigController implements CheckObject{
    /**
     * Logging object.
     */
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ConfigController.class);

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
     *     properties.
     * @param ksFileRoute String with the filename with the KeyServer 
     *     configuration file.
     * @param ksRequiredFields String array with the required fields on 
     *     KeyServer configuration file.
     * @since v0.3.0
     */
    public ConfigController(String mvnFileRoute, String ksFileRoute, String [] ksRequiredFields) {
        // Instantiation of configuration objects.
        mavenData = new Maven(mvnFileRoute);
        keyserverConfig = new ConfigFile(ksFileRoute, ksRequiredFields); 
    }
    
    /**
     * Method used to check if the configuration file objects are correctly 
     *     initialized.
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
     *     returns 'null'.
     * @since v0.3.0
     */
    public InetAddress getServerAddress(){
        String address = this.keyserverConfig.getServerAddress();
        try {
            if((address!= null) && (!address.isEmpty())){
                return InetAddress.getByName(address);
            } else {
                throw(new UnknownHostException("Empty Address."));
            }
        } catch (UnknownHostException ex) {
            // Error level.
            LOGGER.error("Unknown Host Exception with the server IP addres: {}", address);
            // Trace level.
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            LOGGER.trace(errors.toString());
            return null;
        }
    }
    
    /**
     * This method is used to get the listener KeyServer port.
     * @return KeyServer port as integer. If the field is not present,
     *     returns -1 value.
     * @since v0.3.0
     */
    public int getServerPort(){
        String port = this.keyserverConfig.getServerPort();
        if((port != null) && (!port.isEmpty() && (Integer.parseInt(port) > 0))){
            return Integer.parseInt(port);
        } else {
            // Error level.
            LOGGER.error("Not valid HTTPS port specified for the KeyServer: {}", port);
            return -1;
        }
    }
    
    /**
     * This method is used to get the certificate for KeyServer HTTPS server.
     * @return String with the certificate name and route (if is available). If 
     *     the field is not present, returns 'null'.
     * @since v0.3.0
     */
    public String getServerKeyStoreFile(){
        return this.keyserverConfig.getServerKeyStoreFile();
    }
    
    /**
     * This method is used to get password of the KeyServer HTTPS certificate 
     * key store.
     * @return String with the KeyServer HTTPS certificate password. If the 
     *     field is not present, returns 'null'.
     * @since v0.3.0
     */
    public String getServerKeyStorePassword(){
        String password = this.keyserverConfig.getKeyStorePassword();
        if((password != null) && (!password.isEmpty())){
            return password;
        } else {
            // Error level.
            LOGGER.error("Not valid password specified for the KS Key Store Password: {}", password);
            return null;
        }
    }
    
    /**
     * This method is used to get password of the KeyServer HTTPS certificate 
     * manager.
     * @return String with the KeyServer HTTPS certificate password. If the 
     *     field is not present, returns 'null'.
     * @since v0.4.0
     */
    public String getServerKeyManagerPassword(){
        String password = this.keyserverConfig.getKeyManagerPassword();
        if((password != null) && (!password.isEmpty())){
            return password;
        } else {
            // Error level.
            LOGGER.error("Not valid password specified for the KS Key Manager Password: {}", password);
            return null;
        }
    }
    
    /**
     * The time in milliseconds that the connection can be idle before it is 
     * closed.
     * @return The value in milliseconds or -1 if the value is not valid.
     */
    public long getIdleTimeout() {
        if((this.keyserverConfig.getServerIdleTimeout() != null) &&
                (!this.keyserverConfig.getServerIdleTimeout().isEmpty()) && 
                (Integer.valueOf(this.keyserverConfig.getServerIdleTimeout()) > 0)){
            return Integer.valueOf(this.keyserverConfig.getServerIdleTimeout());
        } else {
            // Warning level.
            LOGGER.warn("Jetty connection Idle Timeout value is not valid.");
            return -1;
        }
    }

    /**
     * This method returns an array with the authorized IPs for access to the
     *     KeyServer.
     * @return Array of strings with the IP authorized. If the field is not 
     *     present, returns 'null'.
     * @since v0.4.0
     */
    public String[] getServerIpWhiteList() {
        if((this.keyserverConfig.getServerIpWhiteList()==null) || this.keyserverConfig.getServerIpWhiteList().isEmpty()){
            // Not defined.
            return null;
        }
        String tmp = this.keyserverConfig.getServerIpWhiteList();
        if (tmp.contains("&")){
            // Contains multiples IPs.
            String [] output = tmp.split("&");
            for(int i = 0; i < output.length; i++){
                output[i]=output[i].trim();     // Remove white spaces.
            }
            return output;
        } else {
            // Only contains an IP.
            String [] value = {tmp};
            return value;
        }
    }
    
    /**
     * This method is used to get the Redis Database server address.
     * @return InetAddress object with the Redis Database server address. If the 
     *     field is not present, returns 'null'.
     * @since v0.3.0
     */
    public InetAddress getDbAddress(){
        String address = this.keyserverConfig.getDbAddress();
        try {
            if(address == null){
                throw(new UnknownHostException("Address null."));
            }
            return InetAddress.getByName(address);
        } catch (UnknownHostException ex) {
            // Error level.
            LOGGER.error("Unnknown Host Exception with Redis Dtabase IP address: {}", address);
            // Trace level.
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            LOGGER.trace(errors.toString());
            return null;
        }
    }
    
    /**
     * This method is used to get Redis Database server port.
     * @return Integer with the Redis Database Port. If the field is not present,
     *     returns -1 value.
     * @since v0.3.0
     */
    public int getDbPort(){
        String port = this.keyserverConfig.getDbPort();
        if((port != null) && (!port.isEmpty()) && (Integer.parseInt(port) > 0)){
            return Integer.parseInt(port);
        } else {
            // Error level.
            LOGGER.error("Not valid port specified for the Redis Database: {}", port);
            return -1;
        }
    }
    
    /**
     * This method is used to get Redis Database password.
     * @return String with the Redis Database password. If the field is not present,
     *     returns `null`.
     * @since v0.3.1
     */
    public String getDbPassword(){
        String password = this.keyserverConfig.getDbPassword();
        if((password != null) && (!password.isEmpty())){
            return password;
        } else {
            // Error level.
            LOGGER.error("Not valid password specified for the Redis Database: {}", password);
            return null;
        }
    }
    
    /**
     * This method is used to get Redis Database index.
     * @return Integer with the Redis Database index. If the field is not present,
     *     returns `0`.
     * @since v0.3.3
     */
    public int getDbIndex(){
        String index = this.keyserverConfig.getDbIndex();
        if((index != null) && (!index.isEmpty())&&(Integer.valueOf(index) > 0)){
            return Integer.valueOf(index);
        } else {
            // Warning level.
            LOGGER.warn("Redis DB index not present or not valid. Using default index (0).");
            return 0;
        }
    }
    
    /**
     * This method is used to get the DB time interval when the PING will be 
     * send. The Redis DB connection status is monitored periodically in a 
     * parallel thread.
     * @return Integer with the time in milliseconds. If the field is not 
     *     present or invalid (lower than 100ms), returns -1.
     * @since v0.3.3
     */
    public int getChkDbInterval(){
        if((this.keyserverConfig.getChkDbInterval()!= null) && 
                (!this.keyserverConfig.getChkDbInterval().isEmpty()) &&
                (Integer.valueOf(this.keyserverConfig.getChkDbInterval()) >= 100)){
            return Integer.valueOf(this.keyserverConfig.getChkDbInterval());
        } else {
            // Warning level.
            LOGGER.warn("DB connection check interval value is not valid. "
                    + "Must be greater than 100ms.");
            return -1;
        }
    }
    
    /**
     * This method is used to set the interval value in milliseconds for check
     * if there are a new KeyServer version available on GitHub.
     * This interval is used to verify if the KeyServer HTTPS server certificate
     * has been expired.
     * @return Integer with the time in milliseconds.
     * @since v0.3.3
     */
    public int getChkUpdateInterval(){
        if((this.keyserverConfig.getChkUpdateInterval() != null) &&
                (!this.keyserverConfig.getChkUpdateInterval().isEmpty()) && 
                (Integer.valueOf(this.keyserverConfig.getChkUpdateInterval()) >= 60000)){
            return Integer.valueOf(this.keyserverConfig.getChkUpdateInterval());
        } else {
            // Warning level.
            LOGGER.warn("KeyServer updates check interval value is not valid (must be greater than 60000).");
            return -1;
        }
    }
}

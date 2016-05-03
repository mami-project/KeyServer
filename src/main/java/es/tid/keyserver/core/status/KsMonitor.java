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
package es.tid.keyserver.core.status;

import es.tid.keyserver.controllers.db.DataBase;
import es.tid.keyserver.core.lib.LastVersionAvailable;
import es.tid.keyserver.https.certificate.HttpsCert;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import javax.swing.Timer;
import org.slf4j.LoggerFactory;

/**
 * This class analyze the status of the main services from the KeyServer tool.
 * @author <a href="mailto:jgm1986@hotmail.com">Javier Gusano Martinez</a>
 * @since 0.3.0
 */
public class KsMonitor {
    /**
     * Logging object.
     */
    private static org.slf4j.Logger logger;
    /**
     * KeyServer startup date.
     */
    private final Date startDate;
    /**
     * Current KeyServer version object.
     */
    private final String curVer;
    /**
     * KeyServer Project GitHub URL
     */
    private final String repoUrl;
    /**
     * REDIS Database Connection Object
     */
    private DataBase dataBaseObj;
    /**
     * Timer for KeyServer status refresh every second.
     */
    private final Timer t1;
    /**
     * Timer for KeyServer certificate and updates status every 12 hours.
     */
    private final Timer t2;
    /**
     * Last KeyServer version available object manager.
     */
    private LastVersionAvailable updates;
    /**
     * Data base flag status. True connected, false if not.
     */
    private boolean dbStatus;
    /**
     * HTTPS server initialization status flag.
     */
    private boolean httpsServerInit;
    /**
     * This is the controller object for the HTTPs certificate.
     */
    private HttpsCert certStatus;
    
    /**
     * Class constructor.
     * @param db Data base controller object.
     * @param httpsServerInit HTTPs server initialization status flag.
     * @param sCert HTTPs certificate controller object.
     * @param publicUrl KeyServer public repository URL.
     * @param curVer Current KeyServer version.
     * @since v0.3.0
     */
    public KsMonitor(DataBase db, boolean httpsServerInit, HttpsCert sCert, String publicUrl, String curVer){
        logger = LoggerFactory.getLogger(KsMonitor.class);
        // Get de current date when the KeyServer has started.
        startDate = new Date();
        // Set external object to class fields
        dataBaseObj = db;
        this.curVer = curVer;
        this.repoUrl = publicUrl;
        this.httpsServerInit = httpsServerInit;
        this.certStatus = sCert;
        // KeyServer updates object controller
        updates =  new LastVersionAvailable(this.repoUrl + "/releases/latest");
        // Timer 1: Checks the object status every second.
        t1 = new Timer(1000, new ActionListener() {
            /**
             * Check every second the following objects.
             * @param ae Action event object (not used)
             */
            @Override
            public void actionPerformed(ActionEvent ae) {
                // Redis Data Base status.
                dbStatus = dataBaseObj.isConnected();
                if(!dbStatus){
                    // Error level.
                    logger.error("Lost connection to Redis Database.");
                }
            }
        });
        // Timer 2: Checks KeyServer updates and certificate status every 12hours.
        t2 = new Timer(43200000, new ActionListener() {
            /**
             * Check every 12 hours the following objects.
             * @param ae Action event object (not used)
             */
            @Override
            public void actionPerformed(ActionEvent ae) {
                // Check the HTTPs certificate expiration date.
                if(!certStatus.isValid()){
                    // If the keyserver is not updated.
                    logger.error("The HTTPs certificate has expired since: {}\n\t"
                            + "All incoming requests to the KeyServer will be rejected.",
                            certStatus.certExpirDate());
                }
                // Get last version available:
                updates.refreshRepoStatus();
                if(!updates.isUpdated(curVer)){
                    // If the keyserver is not updated.
                    logger.warn("There are a new version of KeyServer tool: {} Please update!\n\t"
                            + "KeyServer GitHub project URL: {}",
                            updates.getLastVersionAvailable(), repoUrl);
                }
            }
        });
        // Start timers.
        t1.start();
        t2.start();
        logger.trace("The KeyServer Monitor object has started.");
    }
    
    /**
     * This method stops all the timers inside this object.
     * <p>
     * Please use this method before close KeyServer.
     * @since v0.3.0
     */
    public void stop(){
        t1.stop();
        t2.stop();
        logger.trace("The KeyServer Monitor object has been stopped.");
    }
    
    /**
     * Returns the status flag for the Redis Database connection.
     * @return True if the KeyServer is connected to the RedisDatabase, false 
     * otherwise.
     * @since v0.3.0
     */
    public boolean isRedisConnectionAvailable(){
        return this.dbStatus;
    }
    
    /**
     * This method is used to verify if the HTTPs object has been initialized
     * correctly.
     * @return True if is correctly initialized, false if not.
     * @since v0.3.0
     */
    public boolean isHttpsServerCorrectlyInitialized(){
        return this.httpsServerInit;
    }
    
    /**
     * This method is used to check if the current HTTPs certificate is valid.
     * @return True if the HTTPs certificate hast not overhead its expiration date.
     * @since v0.3.0
     */
    public boolean isHttpsCertificateValid(){
        return this.certStatus.isValid();
    }
    
    /**
     * This method is used to get the Date object with the HTTPs server 
     * certificate expiration date.
     * @return Date object with the certificate expiration date.
     * @since v0.3.0
     */
    public Date getHttpsCertificateExpDate(){
        return this.certStatus.certExpirDate();
    }
    
    /**
     * This method is used to get the number of days where the current HTTPs 
     * certificate is valid since today.
     * @return Long number with the number of valid days for the current HTTPs 
     * certificate.
     * @since v0.3.0
     */
    public long getHttpsCertificateRemainDays(){
        return this.certStatus.certRemainDays();
    }
    
    /**
     * This method is used to get the version of the current KeyServer instance.
     * @return String with the version label for the current instance of the 
     * KeyServer.
     * @since v0.3.0
     */
    public String getCurrentKSVersion(){
        return this.curVer;
    }
    
    /**
     * This method is used to get the last version available of the KeyServer on
     * the public repository.
     * @return String with the label of the last KeyServer version available.
     * @since v0.3.0
     */
    public String getLastKSVersionAvailable(){
        return this.updates.getLastVersionAvailable();
    }
    
    /**
     * This method returns the KeyServer GitHub project URL as String.
     * @return String with the KeyServer project URL.
     * @since v0.3.0
     */
    public String getKSProjectURL(){
        return this.repoUrl;
    }
    
    /**
     * This method is used to get the Date object when the current instance of
     * this key server was launch.
     * @return Date object when the current instance of the KeyServer was 
     * executed.
     * @since v0.3.0
     */
    public Date keyServerRunningSince(){
        return this.startDate;
    }
}

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

package es.tid.keyserver.https.certificate;

import es.tid.keyserver.core.lib.CheckObject;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Enumeration;
import java.util.concurrent.TimeUnit;

/**
 * This class is used to load and manager the HTTPs server certificate.
 * @author <a href="mailto:jgm1986@hotmail.com">Javier Gusano Martinez</a>
 * @since v0.3.0
 */
public class HttpsCert implements CheckObject{
    /**
     * Certificate object.
     */
    private KeyStore keystore;

    /**
     * Certificate expiration date object.
     */
    private Date expDate;

    /**
     * Logging object.
     */
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(HttpsCert.class);

    /**
     * Flag for check if the object is correctly initialized.
     */
    private boolean initStatus;
    
    /**
     * Class constructor
     * @param fileRoute Certificate file name and route.
     * @param password Password used to load the certificate.
     * @since v0.3.0
     */
    public HttpsCert(String fileRoute, String password){
        try {
            keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            keystore.load(new FileInputStream(fileRoute), password.toCharArray());
            Enumeration<String> aliases = keystore.aliases();
            while(aliases.hasMoreElements()){
                String alias = aliases.nextElement();
                if(keystore.getCertificate(alias).getType().equals("X.509")){
                    expDate = ((X509Certificate) keystore.getCertificate(alias)).getNotAfter();
                }
            }
        } catch (KeyStoreException ex) {
            initStatus = false;
            // Error level.
            LOGGER.error("KeyStoreException occurred when try to load the HTTPs certificate.");
            // Trace level.
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            LOGGER.trace(errors.toString());
        } catch (IOException ex) {
            initStatus = false;
            // Error level.
            LOGGER.error("IOException when try to load the HTTPs certificate.");
            // Trace level.
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            LOGGER.trace(errors.toString());
        } catch (NoSuchAlgorithmException ex) {
            initStatus = false;
            // Error level.
            LOGGER.error("NoSuchAlgorithmException occurred when try to load the HTTPs certificate.");
            // Trace level.
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            LOGGER.trace(errors.toString());
        } catch (CertificateException ex) {
            initStatus = false;
            // Error level.
            LOGGER.error("There are a problem with the HTTPs server certificate: {}", fileRoute);
            // Trace level.
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            LOGGER.trace(errors.toString());
        }
        initStatus = true;
    }
    
    /**
     * Returns the loaded certificate object.
     * @return Returns the certificate object as KeyStore object.
     */
    public KeyStore getCertificate(){
        return this.keystore;
    }
    
    /**
     * Returns a Date object with the certificate expiration date.
     * @return Certificate expiration date as 'Date' object.
     * @since v0.3.0
     */
    public Date certExpirDate(){
        return expDate;
    }
    
    /**
     * Return the number of days until the certificate expiration.
     * @return Number of days from the current day until the certificate 
     *     expiration date.
     * @since v0.3.0
     */
    public long certRemainDays(){
        long tmp = expDate.getTime() - new Date().getTime();
        return TimeUnit.MILLISECONDS.toDays(tmp);
    }

    /**
     * Return true if the object is correctly initialized or false if not.
     * @return Object initialization status.
     * @since v0.3.0
     */
    @Override
    public boolean isCorrectlyInitialized() {
        return this.initStatus && isValid();
    }
    
    /**
     * Return true if the current certificate is valid (date has not expired).
     * @return True if the certificate is valid, false if not.
     * @since v0.3.0
     */
    public boolean isValid(){
        return (expDate.getTime() - new Date().getTime()) >= 0;
    }
}

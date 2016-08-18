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
package es.tid.keyserver.core.lib;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.slf4j.LoggerFactory;

/**
 * This class is used to check if the current application version is the last
 * available from the main repository.
 * @author <a href="mailto:jgm1986@hotmail.com">Javier Gusano Martinez</a>
 * @since v0.3.0
 */
public class LastVersionAvailable implements CheckObject{
	
    /**
     * Current application URL repository.
     */
    private URL url;
    
    /**
     * Logging object.
     */
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(LastVersionAvailable.class);
    
    /**
     * Current object initialization flag
     */
    private boolean initStatus;
    
    /**
     * Contains the last version available on repository for the current application.
     */
    private String lastVersion;
    
    /**
     * Default class constructor.
     * @param url Contains the string for the URL repository.
     */
    public LastVersionAvailable(String url){
        try {
            this.url = new URL(url);
        } catch (MalformedURLException ex) {
            initStatus = false;
            // Error level.
            LOGGER.error("The current URL: " + url + " is not valid.");
            // Debug level.
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            LOGGER.trace(errors.toString());
        }
        this.refreshRepoStatus();
        this.initStatus = true;
    }
    
    /**
     * This method returns an string with the last version available on remote
     *     repository for the current software.
     * @return String with the last version available.
     */
    public String getLastVersionAvailable(){
        return this.lastVersion;
    }
    
    /**
     * Compare the current version available on remote repository with the input
     *     version specified on method parameter.
     * @param appVersion Version to be compared with the available on repository.
     * @return True if the specified 'appVersion' is equal to the remote repository,
     *     false if not.
     * @since v0.3.0
     */
    public boolean isUpdated(String appVersion){
        return lastVersion.equalsIgnoreCase(appVersion);
    }

    /**
     * This method is used to get the last version available for the specified
     *     repository.
     *     <p>WARNING: This method only works for GitHub repositories.
     * @since v0.3.0
     */
    public final void refreshRepoStatus(){
        String versionUrl;
        try {
            // Connect to the initial GitHub repository URL.
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.connect();
            // Follow the HTTP redirection.
            InputStream is = con.getInputStream();
            // Get the redirected URL.
            versionUrl = con.getURL().toString();
            is.close();
            // The last software version tag is at the end of the URL.
            this.lastVersion = versionUrl.substring(versionUrl.lastIndexOf("/")+1);
        } catch (IOException ex) {
            // Error level.
            LOGGER.error("Can't connect to the current URL: " + url + ".");
            // Debug level.
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            LOGGER.trace(errors.toString());
        }
    }
    
    /**
     * This method is used to test if the current object has been correctly 
     *     initialized.
     * @return True if has been initialized correctly, false if not.
     * @since v0.3.0
     */
    @Override
    public boolean isCorrectlyInitialized() {
        return this.initStatus;
    }
}

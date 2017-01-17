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

package es.tid.keyserver.core.lib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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
     * First time class code execution flag.
     */
    private boolean initFlag;
    
    /**
     * Contains the last version available on repository for the current application.
     */
    private String lastVersion;
    
    /**
     * Default class constructor.
     * @param url Contains the string with the GitHub API address and get last
     *     version method route for this project.
     */
    public LastVersionAvailable(String url){
        initFlag = false;
        try {
            this.url = new URL(url);
            this.refreshRepoStatus();            
            this.initStatus = true;
        } catch (MalformedURLException ex) {
            initStatus = false;
            // Error level.
            LOGGER.error("The current URL: " + url + " is not valid.");
            // Debug level.
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            LOGGER.trace(errors.toString());
        }
        initFlag=true;
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
        Version currentVersion = new Version(appVersion);
        Version lastAvailableVersion = new Version(lastVersion);
        return !lastAvailableVersion.greaterThan(currentVersion);
    }

    /**
     * This method is used to get the last version available for the specified
     *     repository.
     * 
     *     <p>WARNING: This method only works for GitHub repositories.
     * @since v0.3.0
     */
    public final void refreshRepoStatus(){
        try {
            if(initFlag && !this.isCorrectlyInitialized()){
                throw(new IOException("Check Last Version Object not correctly initialized."));
            }
            // Connect to the initial GitHub repository URL.
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("User-Agent", "KeyServer");
            con.connect();
            // Read the HTTP response.
            BufferedReader response = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String tmp;
            String data = "";
            while ((tmp = response.readLine()) != null) {
                data += tmp;
            }
            LOGGER.trace("Received GitHub data: {}", data);
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(data);
            if(json.containsKey("tag_name")){
                this.lastVersion = (String)json.get("tag_name");
                LOGGER.debug("Last KeyServer version available: {}", this.lastVersion);
            }
        }catch (IOException ex) {
            LOGGER.error("Cannot connect to GitHub.");
            LOGGER.debug(ex.getMessage());
            LOGGER.trace(Arrays.toString(ex.getStackTrace()));
        } catch (ParseException ex) {
            LOGGER.error("Cannot read the received GitHub JSON. Malformed.");
            LOGGER.debug(ex.getMessage());
            LOGGER.trace(Arrays.toString(ex.getStackTrace()));
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

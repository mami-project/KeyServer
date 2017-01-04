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

package es.tid.keyserver.config.maven;

import es.tid.keyserver.core.lib.CheckObject;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Properties;
import org.slf4j.LoggerFactory;

/**
 * Class for the OpenSource KeyServer Maven properties management.
 * @author <a href="mailto:jgm1986@hotmail.com">Javier Gusano Martinez</a>
 * @since v0.1.2
 */
public class Maven implements CheckObject{
	
    /**
     * File to be loaded with Java Application Properties
     */
    private Properties prop;
    
    /**
     * Logging object.
     */
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Maven.class);
    /**
     * Current object initialization flag
     */
    private boolean initStatus;

    /**
     * Maven properties class constructor.
     * @param fileName File name to be loaded from inside JAR. This is an 
     *     example: "/application.properties".
     * @since v0.3.0
     */
    public Maven(String fileName){
        try (InputStream resourceAsStream = this.getClass().getResourceAsStream(fileName)) {
            prop = new Properties();
            prop.load( resourceAsStream );
        } catch (NullPointerException | IOException ex) {
            initStatus = false;
            // Error level.
            LOGGER.error("The current config file: " + fileName + " can't be loaded correctly.");
            // Debug level.
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            LOGGER.trace(errors.toString());
        }
        LOGGER.debug("Maven config file: " + fileName + " correctly loaded.");
        initStatus = true;
    }
    
    /**
     * This method is used to get the current application version.
     * @return String with the version.
     * @since v0.1.2
     */
    public String getVersion() {
        return prop.getProperty("application.version");
    }

    /**
     * This method is used to get the current application name.
     * @return String with the name.
     * @since v0.1.2
     */
    public String getAppName() {
        return prop.getProperty("application.name");
    }
    
    /**
     * Returns the public repository URL as string.
     * @return String with the public GitHub URL.
     * @since v0.3.0
     */
    public String getProjectPublicUrl(){
        return prop.getProperty("application.url");
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

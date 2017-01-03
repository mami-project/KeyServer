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

package es.tid.keyserver.ui;

import es.tid.keyserver.config.ConfigController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * KeyServer graphical elements
 * @author <a href="mailto:jgm1986@hotmail.com">Javier Gusano Martinez</a>
 */
public class GraphicalElements {
    /**
     * Pom file application name field value.
     */
    private String appName;

    /**
     * Pom file application version value.
     */
    private String appVersion;

    /**
     * Logging object.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(GraphicalElements.class);

    /**
     * Default class constructor
     * @param softwareConfig KeyServer software configuration controller.
     * @since v0.4.1
     */
    public GraphicalElements(ConfigController softwareConfig){
        // Java Project properties (from MAVEN application properties).
        appName = softwareConfig.getAppName();
        appVersion = softwareConfig.getVersion();
        // Print KeyServer start box.
        LOGGER.debug("Starting {} tool {}", appName, appVersion);
    }
    
    /**
     * This method returns an ASCII title for the KeyServer tool.
     * @return String with the ASCII art text "KeyServer"
     * @since v0.4.1
     */
    public String ksTitle(){
        return  "+------------------------------------------------------------------------+\n" +
                "|             _  __          ____                                        |\n" +
                "|            | |/ /___ _   _/ ___|  ___ _ ____   _____ _ __              |\n" +
                "|            | ' // _ \\ | | \\___ \\ / _ \\ '__\\ \\ / / _ \\ '__|             |\n" + 
                "|            | . \\  __/ |_| |___) |  __/ |   \\ V /  __/ |                |\n" +
                "|            |_|\\_\\___|\\__, |____/ \\___|_|    \\_/ \\___|_|                |\n" +
                "|                     |___/" + printVersionLine(appVersion) + "|\n" + 
                "+------------------------------------------------------------------------+\n"+
                " https://github.com/mami-project/KeyServer \n";
    }
    
    /**
     * This method returns an string with the KeyServer application name.
     * @return String with the KeyServer application name.
     * @since v0.4.1
     */
    public String getAppName(){
        return this.appName;
    }
       
    /**
     * This method is used to represent the application version string correctly
     *     according to the version string length.
     * @param version String with the tool version info.
     * @return String to be printed on screen with correct position for the 
     *         version string.
     */
    private static String printVersionLine(String version) {
        String versionLine = "";
        // Length of line 46 characters
        for(int i=0; i< (46 - (version.length() + 1)); i++){
            versionLine += " ";
        }
        versionLine += version + " ";
        return versionLine;
    }
}

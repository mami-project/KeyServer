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
package es.tid.keyserver.core;

import es.tid.keyserver.config.ConfigController;
import es.tid.keyserver.core.lib.CheckObject;
import es.tid.keyserver.controllers.db.DataBase;
import es.tid.keyserver.core.status.KsMonitor;
import es.tid.keyserver.https.HttpsServerController;
import es.tid.keyserver.https.certificate.HttpsCert;
import es.tid.keyserver.ui.UserInterfaceController;
import java.util.Arrays;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main Class for the KeyServer.
 * @author <a href="mailto:jgm1986@hotmail.com">Javier Gusano Martinez</a>
 */
public class KeyServer {
    /**
     * Logging object.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(KeyServer.class);
    
    /**
     * Main function of the KeyServer program.
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Process input arguments
        String configFile = inputArgs(args);
        if(configFile.isEmpty()){
            LOGGER.debug("User has not provide an input configuration file name. Using default: 'general.properties'.");
            configFile = "general.properties";     // Default configuration file.
        }
        // Load/Create configuration file
        LOGGER.info("Loading KeyServer parameters...\n\n");
        String [] configFields = getRequiredFields();
        // KeyServer Configuration Object
        ConfigController softwareConfig = new ConfigController("/application.properties", configFile, configFields);
        // Java Project properties (from MAVEN application properties).
        String appName = softwareConfig.getAppName();
        String appVersion = softwareConfig.getVersion();
        // Print KeyServer start box.
        LOGGER.debug("Starting {} tool {}", appName, appVersion);
        System.out.println("+------------------------------------------------------------------------+");
        System.out.println("|                          " +    appName   + "                          |");
        System.out.println("|                          --------------------                          |");
        System.out.println("+------------------------------------------------------------------------+");
        System.out.println(printVersionLine(appVersion));
        
        checkObj(softwareConfig, "Configuration file correctly loaded.",
                "Can't load configuration file. Please check if the file exists and can be read.");
        
        // Connect to the Data Base.
        LOGGER.info("Connecting to database.");
        DataBase keyServerDB = new DataBase(softwareConfig.getDbAddress(), 
                softwareConfig.getDbPort(), 
                softwareConfig.getDbPassword());
        checkObj(keyServerDB, "Database connection established.",
                "Can't connect to the database. Please check 'general.properties' file values.");
        
        // HTTPs Server Certificate.
        LOGGER.info("Loading HTTPs server certificate.");
        HttpsCert ksCert = new HttpsCert(
                softwareConfig.getServerKeyFile(), 
                softwareConfig.getServerKeyPass());
        checkObj(ksCert,"HTTPs server certificate correctly loaded.", 
                "Can't load the HTTPs server certificate..");
        
        // HTTP Server.
        LOGGER.info("Starting HTTP server... ");
        HttpsServerController keyServerHttp = new HttpsServerController(
                softwareConfig, 
                keyServerDB, 
                ksCert.getCertificate(), 
                softwareConfig.getServerKeyPass());
        checkObj(keyServerHttp,"KeyServer now is ready...", 
                "Can't create HTTP server.");
        
        // KeyServer Monitor object.
        KsMonitor mon = new KsMonitor(
                keyServerDB, 
                keyServerHttp.isCorrectlyInitialized(), 
                ksCert, 
                softwareConfig.getProjectPublicUrl(),
                softwareConfig.getVersion());
        
        // Wait until the user write q/Q to close the application.
        Scanner sc = new Scanner(System.in);
        UserInterfaceController uiController = new UserInterfaceController(
                sc,
                mon, 
                keyServerDB);
        checkKeyServerUserInput(sc, uiController);
        sc.close();
        
        // Closing the application.
        System.out.println("\n+------------------------------------------------------------------------+\n");
        LOGGER.warn("Clossing KeyServer tool. Please wait...");
        mon.stop();
        keyServerHttp.stop();
        keyServerDB.stop();
        LOGGER.info("KeyServer tool closed.");
        System.exit(0);
    }
    
    /**
     * Method used to process the command line input arguments.
     * @param args String array with the user input arguments.
     * @return Return empty String or the route to the new Configuration file specified by the user.
     */
    static private String inputArgs(String args[]){
        LOGGER.trace("Command line user input: {}", Arrays.toString(args));
        if((args.length > 0)&&(args.length <= 2)){
            switch(args[0]){
                case "-c":  // Specific the new configuration file to be used.
                    if (args.length == 1){
                        LOGGER.error("You must specific the config file route as input parammeter.");
                        showHelp();
                    } else {
                        return args[1];
                    }
                    break;
                case "-h":  // Slow Help output message.
                    showHelp();
                    LOGGER.debug("Showing command help to the user.");
                    System.exit(0);
                    break;
                default:    // Invalid input parameter.
                    LOGGER.error("Not valid command line input option: {}", args[0]);
                    LOGGER.info("Please use one of the following accepted parammeters.");
                    showHelp();
                    System.exit(-1);
                    break;
            }
        }
        return "";
    }
    
    /**
     * This method only displays the help with the available input parameters. 
     */
    static private void showHelp(){
        LOGGER.info("Usage: keyserver.jar [option] <parameter>\n" +
            "options: Can set only one option or option plus parameter value (if is available).\n" +
            " -c configFile\tUse external config file.\n" +
            " -h\t\tThis help text.\n");
    }
    
    /**
     * This method check if the current object is correctly initialized. If all
     * works as expected, shows "[OK]" and continue with the execution. If not,
     * shows "[ERROR]" and close the application.
     * @param obj Object to check. Must implement "CheckObject" interface.
     * @param okMsg Message to show at INFO level if all works as expected. 
     * @param errMsg String to show if the object is not correctly initialized.
     */
    static private void checkObj(CheckObject obj, String okMsg, String errMsg){
        if(obj.isCorrectlyInitialized()){
            LOGGER.info(okMsg);
        } else {
            LOGGER.error(errMsg);
            System.out.println("\n+------------------------------------------------------------------------+\n");
            LOGGER.warn("KeyServer closed.");
            System.exit(-1);
        }
    }
    
    /**
     * This method control the user input for KeyServer management.
     * @param uiController User Interface controller for the KeyServer management.
     * @since v0.3.0
     */
    private static void checkKeyServerUserInput(Scanner sc, UserInterfaceController uiController) {
        while(!uiController.exitFlag()){
            System.out.print(">> ");
            String option = sc.next();
            if(option != null){
                uiController.userInputDigest(option.trim());
            }
        }
    }
    
    /**
     * This method is used to represent the application version string correctly
     * according to the version string length.
     * @param version String with the tool version info.
     * @return String to be printed on screen with correct position for the 
     *         version string.
     */
    private static String printVersionLine(String version) {
        String versionLine = "";
        // Length of line 74 characters
        for(int i=0; i< (74 - (version.length() + 1)); i++){
            versionLine += " ";
        }
        versionLine += version + " ";
        return versionLine;
    }

    /**
     * This method returns an array with the name of configuration file fields.
     * @return Array of string with the fields names.
     */
    private static String[] getRequiredFields() {
        String [] fields = {
            // HTTPs server configuration labels.
            "serverAddress",
            "serverPort",
            // SSL Parameters 
            "serverSSLContext",
            "serverKeyFile",
            "serverKeyPass",
            "serverBacklog",
            "serverKeyManagerFactory",
            "serverTrustManagerFactory",
            "serverKeyStore",
            // Redis Database
            "dbAddress",
            "dbPort",
            "dbPassword",
            // Access control
            "whiteList"
        };
        return fields;
    }
}

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
package es.tid.keyserver.keyserver;

import es.tid.keyserver.config.ConfigFile;
import es.tid.keyserver.database.DataBase;
import es.tid.keyserver.httpkeyserver.HttpKeyServer;
import java.util.Arrays;
import java.util.Scanner;
import es.tid.keyserver.maven.Maven;
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
    private static Logger logger;
    
    /**
     * Main function of the KeyServer program.
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        logger = LoggerFactory.getLogger(KeyServer.class);
        // Reads Java Project properties
        Maven properties = new Maven();
        String appName = properties.getAppName();
        String appVersion = properties.getVersion();
        // Print KeyServer start box.
        logger.debug("Starting {} tool {}", appName, appVersion);
        System.out.println("+------------------------------------------------------------------------+");
        System.out.println("|                          " +    appName   + "                          |");
        System.out.println("|                          --------------------                          |");
        System.out.println("+------------------------------------------------------------------------+");
        System.out.println(printVersionLine(appVersion)+"\n");
        // Process input args
        String configFile = inputArgs(args);
        if(configFile.isEmpty()){
            logger.debug("User has not provide an input configuration file name. Using default: 'general.properties'.");
            configFile = "general.properties";     // Default configuration file.
        }
        
        // Load/Create configuration file
        printLefColumn(" - Loading KeyServer parammeters...");
        ConfigFile softwareConfig = new ConfigFile(configFile);
        checkObj(softwareConfig, "Can't load configuration file. Please check if the file exists and can be read.");
        logger.debug("Configuration file correctly loaded.");
        
        // Connect to the Data Base.
        printLefColumn(" - Connecting to database...");
        DataBase keyServerDB = new DataBase(softwareConfig);
        checkObj(keyServerDB, "Can't connect to the database. Please check 'general.properties' file values.");
        logger.debug("Database connection established.");
        
        // HTTP Server.
        printLefColumn(" - Starting HTTP server... ");
        HttpKeyServer keyServerHttp = new HttpKeyServer(softwareConfig, keyServerDB);
        checkObj(keyServerHttp, "Can't create HTTP server.");
        logger.info("KeyServer now is ready...");
        
        // Wait until the user write q/Q to close the application.
        checkKeyServerUserInput();
        
        // Clossing the application.
        System.out.println("\n+------------------------------------------------------------------------+");
        System.out.print("\n\n - Clossing KeyServer tool. Please wait...");
        keyServerHttp.stop();
        keyServerDB.stop();
        System.out.println("\t\t\t [  OK  ]");
        logger.info("KeyServer tool closed.");
        System.exit(0);
    }
    
    /**
     * Method used to process the command line input arguments.
     * @param args String array with the user input arguments.
     * @return Return empty String or the route to the new Configuration file specified by the user.
     */
    static private String inputArgs(String args[]){
        logger.trace("Command line user input: {}", Arrays.toString(args));
        if((args.length > 0)&&(args.length <= 2)){
            switch(args[0]){
                case "-c":  // Specific the new config file to be used.
                    if (args.length == 1){
                        logger.error("You must specific the config file route as input parammeter.");
                        showHelp();
                    } else {
                        return args[1];
                    }
                    break;
                case "-h":  // Slow Help output message.
                    showHelp();
                    logger.debug("Showing command help to the user.");
                    System.exit(0);
                    break;
                default:    // Invalid input parameter.
                    logger.error("Not valid command line input option: {}", args[0]);
                    System.err.println("Please use one of the following accepted parammeters.");
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
        System.out.println("Usage: keyserver.jar [option] <parameter>\n" +
            "options: Can set only one option or option plus parameter value (if is available).\n" +
            " -c configFile\tUse external config file.\n" +
            " -h\t\tThis help text.\n");
    }
    
    /**
     * This method check if the current object is correctly initialized. If all
     * works as expected, shows "[OK]" and continue with the execution. If not,
     * shows "[ERROR]" and close the application.
     * @param obj Object to check. Must implement "CheckObject" interface.
     * @param errMsg String to show if the object is not correctly initialized.
     */
    static private void checkObj(CheckObject obj, String errMsg){
        if(obj.isCorrectlyInitialized()){
            System.out.println("\t\t\t [  OK  ]");
        } else {
            System.out.println("\t\t\t[ ERROR ]");
            System.out.println("\n+------------------------------------------------------------------------+");
            logger.error(errMsg);
            System.out.println("KeyServer closed.");
            System.exit(-1);
        }
    }
    
    /**
     * This method uniformize the initialization messages length to 40 characters.
     * @param str Output message.
     */
    static private void printLefColumn(String str){
        if(str.length()<40){
            for(int i = str.length(); i<=40; i++){
                str = str + " ";
            }
        }else{
            str = str.substring(0, 40);
        }
        System.out.print(str);
    }
    
    /**
     * This method control the user input for KeyServer management.
     */
    private static void checkKeyServerUserInput() {
        boolean exitFlag = false;
        while(!exitFlag){
            System.out.print(">> ");
            Scanner sc = new Scanner(System.in);
            String option = sc.next();
            if(option.equalsIgnoreCase("q")){
                exitFlag = true;
            } else if(option.equalsIgnoreCase("h")){
                showKeyServerHelp();
            } else {
                logger.warn("Not valid option. Please write 'H' for view a list with supported options.");
            }
        }
    }
    
    /**
     * This method shows a list with supported parameters for KeyServer management.
     */
    static private void showKeyServerHelp(){
        System.out.println("\n                            KeyServer Options");
        System.out.println("                          --------------------");
        System.out.println(" [ INFO ] Write the option character and press Enter to execute.\n");
        System.out.println(" Option \t Description");
        System.out.println("    H   \t Shows help menu options.");
        System.out.println("    Q   \t Close KeyServer tool.");
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
}

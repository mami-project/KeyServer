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
package es.tid.keyserver.pkprovider.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Class for use external configuration files.
 * @author <a href="mailto:jgm1986@hotmail.com">Javier Gusano Martinez</a>
 */
public class ConfigFile{
    /**
     * Property object with configuration parameters.
     */
    private Properties configFile;
    
    /**
     * Default class constructor.
     * @param fileRoute Contains the name and route to the external 
     * configuration file.
     */
    public ConfigFile(String fileRoute){
        File propertiesFile = new File(fileRoute);
        String fileLocation;
        if((propertiesFile.exists() && propertiesFile.canRead())){
            fileLocation = fileRoute;
        } else {
            System.err.println("Can't access to the specified config file or "
                    + "doesn't exists: " + fileRoute);
            System.out.print(" - New config file on default location...");
            fileLocation = "general.properties";
            newDefaultProperties(fileLocation);
        }
        configFile = new Properties();
        // Load the config file parammeters.
        try {
            configFile.load(new FileInputStream(fileLocation));
        } catch (IOException ex) {
            System.err.println("Cannot load the configuration file for KeyServer.");
            System.err.println("Exceiption message: "+ ex.toString());
        }
    }

    /**
     * This method is used to get a value from a configuration key.
     * @param param Label of the configuration parameter.
     * @return Value associated to the configuration parameter specified as 
     * input.
     */
    public String getParameter(String param){
        return configFile.getProperty(param);
    }
    
    /**
     * Method used to create a new configuration file on specific route with 
     * default parameters.
     * @param fileLocation Route and name to the new configuration file. By 
     * default use "general.properties"
     */
    private void newDefaultProperties(String fileLocation) {   
        try {
            FileOutputStream newConfigFile = new FileOutputStream(fileLocation);
            Properties defaultParameters = new Properties();
            // Default parammeters:
            defaultParameters.setProperty("dbAddress","127.0.0.1");
            defaultParameters.setProperty("dbPort", "6379");
            // Save parameters on file
            defaultParameters.store(newConfigFile, null);
            // Close config file.
            newConfigFile.close();
        } catch (FileNotFoundException ex) {
            System.err.println("Cannot create a new config file with default parameters.");
            System.err.println("Exceiption message: " + ex.toString());
            System.exit(-1);
        } catch (IOException ex) {
            System.err.println("Cannot save a new config file with default parameters.");
            System.err.println("Exceiption message: "+ ex.toString());
            System.exit(-1);
        }
    }
}

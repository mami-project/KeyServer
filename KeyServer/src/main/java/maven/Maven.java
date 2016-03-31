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
package maven;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main Class for the KeyServer.
 * @author <a href="mailto:jgm1986@hotmail.com">Javier Gusano Martinez</a>
 */
public class Maven {
    /**
     * File to be loaded with Java Application Properties
     */
    private Properties prop;

    /**
     * Default class constructor.
     */
    public Maven(){
        InputStream resourceAsStream = this.getClass().getResourceAsStream("/application.properties");
        this.prop = new Properties();
        try {
            this.prop.load( resourceAsStream );
        } catch (IOException ex) {
            Logger.getLogger(Maven.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * This method is used to get the Java application version.
     * @return String with the version.
     */
    public String getVersion() {
        return prop.getProperty("application.version");
    }

    /**
     * This method is used to get the Java application name.
     * @return String with the name.
     */
    public String getAppName() {
        return prop.getProperty("application.name");
    }
}

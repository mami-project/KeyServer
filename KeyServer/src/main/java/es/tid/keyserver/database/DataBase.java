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
package es.tid.keyserver.database;

import es.tid.keyserver.config.ConfigFile;
import java.util.Base64;
import es.tid.keyserver.keyserver.CheckObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 * Database class manipulation.
 * @author <a href="mailto:jgm1986@hotmail.com">Javier Gusano Martinez</a>
 */
public class DataBase implements CheckObject{
    /**
     * REDIS Database Connection Object
     */
    Jedis dataBaseObj;
    
    /**
     * Flag for check if the object is correctly initialization.
     */
    boolean isInitializated;
    
    /**
     * Logging object.
     */
    private static final Logger logger = LoggerFactory.getLogger(DataBase.class);
    
    /**
     * Main constructor of the class.
     * @param parameters Object with program parameters.
     */
    public DataBase(ConfigFile parameters){
        JedisPool pool = new JedisPool(parameters.getParameter("dbAddress"), Integer.parseInt(parameters.getParameter("dbPort")));
        try {
            // Reddis connected.
            dataBaseObj = pool.getResource();
            isInitializated = true;
        } catch (JedisConnectionException e) {
            logger.debug("Database initialization failed.");
            logger.trace("Database exception: {}", e.toString());
            isInitializated = false;
        }
    }
    
    /**
     * Stop and close the database connection correctly.
     */
    public void stop(){
        dataBaseObj.close();
    }
    
    /**
     * This class provides a basic mode to get the array of Bytes for the Private
     * key associated with the input hash certificate value.
     * @param certHash Contains the SHA1 hash of the certificate used to get private key.
     * @return Bytes array associates with the input hash value. Null if hash value is not found.
     */
    public byte[] getPrivateForHash(String certHash){
        String response = dataBaseObj.get(certHash);
        logger.debug("REDIS query: {} | REDIS response: {}", certHash, response);
        if (response!=null){
            // Decode from base64 to bytes and return array of values.
            return Base64.getDecoder().decode(response.trim());
        } else {
            return null;
        }
    }

    /**
     * Return true if the object is correctly initialized or false if not.
     * @return Object initialization status.
     */
    @Override
    public boolean isCorrectlyInitialized(){
        return isInitializated;
    }
}

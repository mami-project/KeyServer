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
package es.tid.keyserver.pkprovider.database;

import es.tid.keyserver.pkprovider.config.ConfigFile;
import java.util.Set;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 * Database class manipulation.
 * @author <a href="mailto:jgm1986@hotmail.com">Javier Gusano Martinez</a>
 */
public class DataBase{
    /**
     * REDIS Database Connection Object
     */
    Jedis dataBaseObj;
    /**
     * Main constructor of the class.
     * @param parameters Object with program parameters.
     */
    public DataBase(ConfigFile parameters){
        JedisPool pool = new JedisPool(parameters.getParameter("dbAddress"), Integer.parseInt(parameters.getParameter("dbPort")));
        try {
            // Reddis connected.
            dataBaseObj = pool.getResource();
        } catch (JedisConnectionException e) {
            System.err.println("Database initialization failed.");
            System.err.println("Database exception: " + e.toString());
        }
    }
    
    /**
     * Stop and close the database connection correctly.
     */
    public void stop(){
        dataBaseObj.close();
    }
    
    /**
     * This class provides a basic mode to get an string with the Private
     * key codified on base64 associated with the input hash (SHA1) certificate value.
     * @param certHash Contains the SHA1 hash of the certificate used to get private key.
     * @return String associates with the input hash value. Null if hash value is not found.
     */
    public String getPrivateKey(String certHash){
        return dataBaseObj.get(certHash);
    }
    
    /**
     * This method insert a new PK register using hash certificate as index.
     * @param certHash SHA1 certificate hash. This field is used as index.
     * @param privKey Private key string codified as base64.
     * @return True if all works correctly. False if not.
     */
    public boolean setPrivateKey(String certHash, String privKey){
        dataBaseObj.set(certHash, privKey);
        String test = this.getPrivateKey(certHash);
        return test.equalsIgnoreCase(privKey);
    }
    
    /**
     * This method is used to delete an specified database index register.
     * @param certHash SHA1 hash of the certificate used as database index.
     * @return True if all works correctly.
     */
    public boolean deletePrivateKey(String certHash){
        if(this.getPrivateKey(certHash)!=null){
            dataBaseObj.del(certHash);
            return true;
        } else {
            System.out.println("Specified register by the user has not found on database: " + certHash);
            return false;
        }
    }
    
    /**
     * Get a full list with the Redis database keys whose value is equal to the 
     * pattern.
     * @param pattern Pattern use to find on database.
     * @return Full list with the results.
     */
    public Set<String> getHashList(String pattern){
        return dataBaseObj.keys(pattern);
    }
}

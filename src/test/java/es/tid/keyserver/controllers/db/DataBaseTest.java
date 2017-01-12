/**
 * Copyright 2017.
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
package es.tid.keyserver.controllers.db;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Base64;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.Test;
import static org.junit.Assert.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Protocol;

/**
 * Database test class.
 * @author <a href="mailto:jgm1986@hotmail.com">Javier Gusano Martinez</a>
 * @since v0.1.0
 */
public class DataBaseTest {
    /**
     * Redis test server object.
     */
    private Jedis dataBaseObj;
    
    /**
     * Redis test server address (localhost).
     */
    private InetAddress dbAddress;
    
    /**
     * Redis test server port.
     */
    private int dbPort;
    
    /**
     * Redis test server password.
     */
    private String dbPassword;
    
    /**
     * Redis test server DB index.
     */
    private int dbIndex;
    
    /**
     * Flag DB available.
     */
    private boolean dbAvailable;
    
    /**
     * Test class constructor.
     * @author <a href="mailto:jgm1986@hotmail.com">Javier Gusano Martinez</a>
     * @since v0.4.3
     */
    public DataBaseTest(){
        try {
            dbAddress = InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            System.out.println("[ ERROR ] Can't load 'localhost' address.");
            Logger.getLogger(DataBaseTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        dbPort = 6379;
        dbPassword = "foobared";
        dbIndex = 0;
        // Connection DB test.
        JedisPool pool = new JedisPool(new GenericObjectPoolConfig(), 
                this.dbAddress.getHostAddress(), 
                this.dbPort, 
                Protocol.DEFAULT_TIMEOUT, 
                this.dbPassword);
        try{
            // Redis connected.
            dataBaseObj = pool.getResource();
            dataBaseObj.ping();
            dataBaseObj.close();
            this.dbAvailable = true;
        } catch (Exception e){
            this.dbAvailable = false;
            System.out.println("[ WARNING ] Redis Server is not available. JUnit tests will be skipped.");
        }
        
    }

    /**
     * Test of stop method, of class DataBase.
     */
    @Test
    public void testStop() {
        System.out.println("stop");
        // If the Redis DB is not available, skip the test.
        org.junit.Assume.assumeTrue(this.dbAvailable);
        DataBase instance = new DataBase(dbAddress, dbPort, dbPassword, dbIndex);
        boolean result1 = instance.isCorrectlyInitialized();
        instance.stop();
        boolean result2 = instance.isConnected();
        assertTrue(result1 && (!result2));
    }

    /**
     * Test of getPrivateForHash method, of class DataBase.
     */
    @Test
    public void testGetPrivateForHash1() {
        System.out.println("getPrivateForHash1");
        // If the Redis DB is not available, skip the test.
        org.junit.Assume.assumeTrue(this.dbAvailable);
        // Insert the test register inside Redis DB.
        String certHash = "TestHashKey1";
        String value = "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";
        this.dataBaseObj.set(certHash, value);
        // Test code.
        DataBase instance =new DataBase(dbAddress, dbPort, dbPassword, dbIndex);
        byte[] expResult = Base64.getDecoder().decode(value);
        byte[] result = instance.getPrivateForHash(certHash);
        // Remove test register from Redis DB.
        this.dataBaseObj.del(certHash);
        assertArrayEquals(expResult, result);
    }
    
    /**
     * Test of getPrivateForHash method, of class DataBase.
     */
    @Test
    public void testGetPrivateForHash2() {
        System.out.println("getPrivateForHash2");
        // If the Redis DB is not available, skip the test.
        org.junit.Assume.assumeTrue(this.dbAvailable);
        // Insert the test register inside Redis DB.
        String certHash = "TestHashKey1";
        String value = "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";
        this.dataBaseObj.set(certHash, value);
        // Test code.
        DataBase instance =new DataBase(dbAddress, dbPort, dbPassword, dbIndex);
        instance.stop();
        byte[] expResult = null;
        byte[] result = instance.getPrivateForHash(certHash);
        // Remove test register from Redis DB.
        this.dataBaseObj.del(certHash);
        assertArrayEquals(expResult, result);
    }
    
    /**
     * Test of getPrivateForHash method, of class DataBase.
     */
    @Test
    public void testGetPrivateForHash3() {
        System.out.println("getPrivateForHash3");
        // If the Redis DB is not available, skip the test.
        org.junit.Assume.assumeTrue(this.dbAvailable);
        // Test code.
        String certHash = "TestHashKey1";
        DataBase instance =new DataBase(dbAddress, dbPort, dbPassword, dbIndex);
        instance.stop();
        byte[] expResult = null;
        byte[] result = instance.getPrivateForHash(certHash);
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of isConnected method, of class DataBase.
     */
    @Test
    public void testIsConnected1() {
        System.out.println("isConnected1");
        org.junit.Assume.assumeTrue(this.dbAvailable);
        DataBase instance = new DataBase(dbAddress, dbPort, dbPassword, dbIndex);
        boolean expResult = true;
        boolean result = instance.isConnected();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of isConnected method, of class DataBase.
     */
    @Test
    public void testIsConnected2() {
        System.out.println("isConnected2");
        org.junit.Assume.assumeTrue(this.dbAvailable);
        DataBase instance = new DataBase(dbAddress, dbPort, dbPassword, dbIndex);
        instance.stop();
        boolean expResult = false;
        boolean result = instance.isConnected();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of isConnected method, of class DataBase.
     */
    @Test
    public void testIsConnected3() {
        System.out.println("isConnected3");
        org.junit.Assume.assumeTrue(this.dbAvailable);
        DataBase instance = new DataBase(dbAddress, dbPort, "badpassword", dbIndex);
        boolean expResult = false;
        boolean result = instance.isConnected();
        assertEquals(expResult, result);
    }

    /**
     * Test of getPrivateKey method, of class DataBase.
     */
    @Test
    public void testGetPrivateKey1() {
        System.out.println("getPrivateKey1");
        // If the Redis DB is not available, skip the test.
        org.junit.Assume.assumeTrue(this.dbAvailable);
        // Insert dummy register for this test inside Redis DB.
        String certHash = "TestKey1";
        this.dataBaseObj.set(certHash, "TestKeyValue1");
        DataBase instance = new DataBase(dbAddress, dbPort, dbPassword, dbIndex);
        String expResult = "TestKeyValue1";
        String result = instance.getPrivateKey(certHash);
        // Remove test register from Redis DB.
        this.dataBaseObj.del(certHash);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getPrivateKey method, of class DataBase.
     */
    @Test
    public void testGetPrivateKey2() {
        System.out.println("getPrivateKey2");
        // If the Redis DB is not available, skip the test.
        org.junit.Assume.assumeTrue(this.dbAvailable);
        // Test code.
        String certHash = "TestKey1";
        DataBase instance = new DataBase(dbAddress, dbPort, dbPassword, dbIndex);
        instance.stop();
        String expResult = null;
        String result = instance.getPrivateKey(certHash);
        // Remove test register from Redis DB.
        this.dataBaseObj.del(certHash);
        assertEquals(expResult, result);
    }

    /**
     * Test of setPrivateKey method, of class DataBase.
     */
    @Test
    public void testSetPrivateKey1() {
        System.out.println("setPrivateKey1");
        // If the Redis DB is not available, skip the test.
        org.junit.Assume.assumeTrue(this.dbAvailable);
        String certHash = "testHash1";
        String privKey = "testValue1";
        DataBase instance = new DataBase(dbAddress, dbPort, dbPassword, dbIndex);
        boolean result = instance.setPrivateKey(certHash, privKey);
        // Remove test register from Redis DB.
        this.dataBaseObj.del(certHash);
        assertTrue(result);
    }
    
    /**
     * Test of setPrivateKey method, of class DataBase.
     */
    @Test
    public void testSetPrivateKey2() {
        System.out.println("setPrivateKey2");
        // If the Redis DB is not available, skip the test.
        org.junit.Assume.assumeTrue(this.dbAvailable);
        String certHash = "testHash1";
        String privKey = "testValue1";
        DataBase instance = new DataBase(dbAddress, dbPort, dbPassword, dbIndex);
        instance.stop();
        boolean result = instance.setPrivateKey(certHash, privKey);
        // Remove test register from Redis DB.
        if(this.dataBaseObj.exists(certHash)){
            System.out.println("[ ERROR ] The current register shoud not be exists. Removing...");
            this.dataBaseObj.del(certHash);
        }
        assertFalse(result);
    }

    /**
     * Test of setExpPK method, of class DataBase.
     */
    @Test
    public void testSetExpPK1() {
        System.out.println("setExpPK1");
        // If the Redis DB is not available, skip the test.
        org.junit.Assume.assumeTrue(this.dbAvailable);
        // Insert dummy register for this test inside Redis DB.
        String certHash = "DummyKey1";
        this.dataBaseObj.set(certHash, "DymmyValue1");
        // Execute test.
        DataBase instance = new DataBase(dbAddress, dbPort, dbPassword, dbIndex);
        long date = 2000L;
        boolean result = instance.setExpPK(certHash, date);
        // Check if the key has expiration date.
        Long ttlKey = this.dataBaseObj.ttl(certHash);
        if((ttlKey>0L) && (ttlKey<=date)){
            result = false;
        }
        assertTrue(result);
    }
    
    /**
     * Test of setExpPK method, of class DataBase.
     */
    @Test
    public void testSetExpPK2() {
        System.out.println("setExpPK2");
        // If the Redis DB is not available, skip the test.
        org.junit.Assume.assumeTrue(this.dbAvailable);
        // Insert dummy register for this test inside Redis DB.
        String certHash = "DummyKey1";
        this.dataBaseObj.set(certHash, "DymmyValue1");
        // Execute test.
        DataBase instance = new DataBase(dbAddress, dbPort, dbPassword, dbIndex);
        instance.stop();
        long date = 2000L;
        boolean result = instance.setExpPK(certHash, date);
        // Check if the key has expiration date.
        Long ttlKey = this.dataBaseObj.ttl(certHash);
        if(ttlKey != -1L){
            System.out.println("[ ERROR ] TTL value should be -1. Current: " + result);
            result = true;
        }
        // Remove key
        this.dataBaseObj.del(certHash);
        assertFalse(result);
    }

    /**
     * Test of deletePrivateKey method, of class DataBase.
     */
    @Test
    public void testDeletePrivateKey1() {
        System.out.println("deletePrivateKey1");
        // If the Redis DB is not available, skip the test.
        org.junit.Assume.assumeTrue(this.dbAvailable);
        // Insert dummy register for this test inside Redis DB.
        String certHash = "RemoveDummyKey1";
        this.dataBaseObj.set(certHash, "RemoveDymmyValue1");
        // Test code.
        DataBase instance = new DataBase(dbAddress, dbPort, dbPassword, dbIndex);
        boolean result = instance.deletePrivateKey(certHash);
        // Check if register has been deleted
        if(this.dataBaseObj.exists(certHash)){
            result = false;
            System.out.println("[ ERROR ] The test register is not deleted from Redis DB.");
        }
        assertTrue(result);
    }
    
    /**
     * Test of deletePrivateKey method, of class DataBase.
     */
    @Test
    public void testDeletePrivateKey2() {
        System.out.println("deletePrivateKey2");
        // If the Redis DB is not available, skip the test.
        org.junit.Assume.assumeTrue(this.dbAvailable);
        // Test code.
        String certHash = "RemoveDummyKey1";
        DataBase instance = new DataBase(dbAddress, dbPort, dbPassword, dbIndex);
        boolean result = instance.deletePrivateKey(certHash);
        assertFalse(result);
    }
    
    /**
     * Test of deletePrivateKey method, of class DataBase.
     */
    @Test
    public void testDeletePrivateKey3() {
        System.out.println("deletePrivateKey3");
        // If the Redis DB is not available, skip the test.
        org.junit.Assume.assumeTrue(this.dbAvailable);
        // Test code.
        String certHash = "RemoveDummyKey1";
        DataBase instance = new DataBase(dbAddress, dbPort, dbPassword, dbIndex);
        instance.stop();
        boolean result = instance.deletePrivateKey(certHash);
        assertFalse(result);
    }

    /**
     * Test of getHashList method, of class DataBase.
     */
    @Test
    public void testGetHashList1() {
        System.out.println("getHashList1");
        // If the Redis DB is not available, skip the test.
        org.junit.Assume.assumeTrue(this.dbAvailable);
        // Insert dummy register for this test inside Redis DB.
        String certHash = "DummyListKey_";
        Set<String> expResult = new HashSet<>();
        for(int i = 0; i < 10; i++){
            this.dataBaseObj.set(certHash + i, "RemoveDymmyValueIndex" + i);
            expResult.add(certHash + i);
        }
        String pattern = certHash + "*";
        DataBase instance = new DataBase(dbAddress, dbPort, dbPassword, dbIndex);
        Set<String> result = instance.getHashList(pattern);
        // Compare the received content.
        boolean valid = true;
        Iterator it = expResult.iterator();
        while(it.hasNext()){
            String current = (String)it.next();
            System.out.print("\t" + current);
            if(!result.contains(current)){
                valid = false;
                System.out.println("\t FAIL");
                break;
            }
            System.out.println("\t OK");
        }
        // Clean Redis DB.
        for(int i = 0; i < 10; i++){
            this.dataBaseObj.del(certHash + i);
        }
        // Check test result.
        assertTrue(valid);
    }
    
    /**
     * Test of getHashList method, of class DataBase.
     */
    @Test
    public void testGetHashList2() {
        System.out.println("getHashList2");
        // If the Redis DB is not available, skip the test.
        org.junit.Assume.assumeTrue(this.dbAvailable);
        // Insert dummy register for this test inside Redis DB.
        String certHash = "DummyListKey_";
        Set<String> expResult = new HashSet<>();
        for(int i = 0; i < 10; i++){
            this.dataBaseObj.set(certHash + i, "RemoveDymmyValueIndex" + i);
            expResult.add(certHash + i);
        }
        String pattern = certHash + "*";
        DataBase instance = new DataBase(dbAddress, dbPort, dbPassword, dbIndex);
        instance.stop();
        Set<String> result = instance.getHashList(pattern);
        // Compare the received content.
        boolean valid = true;
        Iterator it = result.iterator();
        while(it.hasNext()){
            valid = false;
            System.out.println("[ ERROR ] Should be an empty list. Received: " + it.next());
        }
        // Clean Redis DB.
        for(int i = 0; i < 10; i++){
            this.dataBaseObj.del(certHash + i);
        }
        // Check test result.
        assertTrue(valid);
    }
    

    /**
     * Test of isCorrectlyInitialized method, of class DataBase.
     */
    @Test
    public void testIsCorrectlyInitialized() {
        System.out.println("isCorrectlyInitialized");
        // If the Redis DB is not available, skip the test.
        org.junit.Assume.assumeTrue(this.dbAvailable);
        DataBase instance = new DataBase(dbAddress, dbPort, dbPassword, dbIndex);
        boolean result = instance.isCorrectlyInitialized();
        assertTrue(result);
    }
}

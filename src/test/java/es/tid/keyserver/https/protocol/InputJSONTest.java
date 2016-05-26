/*
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
package es.tid.keyserver.https.protocol;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This class contains the Ericsson API Interface Description for it use on KeyServer.
 * @author <a href="mailto:jgm1986@hotmail.com">Javier Gusano Martinez</a>
 * @since v0.1.0
 */
public class InputJSONTest {
    private final String dataTest_1;
    private final String dataTest_2;
    public InputJSONTest() {
        dataTest_1 = "{ \"method\":\"ECDHE\", "
                + "\"protocol\":\"DTLS 1.0\", "
                + "\"spki\":\"405FD8A83BFB64683BAEB51D9F8D99C9D872FA63\", "
                + "\"hash\":\"SHA-256\", "
                + "\"input\":\" VW2zg9TGHpvr1wIuMlzX2VmiUlevpDggVD4MTxBy4iPLHzAZ"
                + "7JlW7DNA/hJPZoSGtfWUgKqBcvI4RfMOEA3hwQMAF0EEJx+gdxklHMZL20TA6"
                + "NAe+7MsF77txd9rp7E8LkRhOaBiNXYVwNqKFrhFOpfocmlMn4t/yCMukIo0k3"
                + "wHyZg8gA==\" }";
        dataTest_2 = "{\n"
                + "     \"protocol\":\"TLS 1.2\", "
                + "	\"method\":\"RSA\",\n"
                + "	\"spki\":\"193D57F655228025FCB8140933BE466BAB5D8E88\",\n"
                + "	\"input\":\"hNYw6/ah/SRXkvJc7WU8TQJd8wH7sUnhJ/4kewnZdGgLINtxEfMk7QeQiFH6Z8LpCgyqYeXoSMHmdAy2MMhseZl34vsFpY2ZZsB8exBzefxS4W55mTaILA6ZWkwVzCKESSRYwf+297XU7OTNxLqB02/DYR9Hr4/vXFfXg38aZQsMpHpDYzRrR9pEX5FNh/MIcBuEJDqi1ldjCREYO5I0LDb6lq9aIFCZKyb6pC6uDeTjgrQaMPWAl1S/jIHGvsRYJBktHAXBFVqWQuDl0WluxyYT++zW2/CuKH8QBDTcm/u9vS5M8RgeWqYrZxNzUwGkdHRuESL/HrdeDZiYtEFH4g==\"\n"
                + "}";
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getMethod method, of class InputJSON.
     */
    @Test
    public void testGetMethod() {
        System.out.println("getMethod ECDHE");
        InputJSON instance = new InputJSON(dataTest_1);
        String expResult = "ECDHE";
        String result = instance.getMethod();
        assertEquals(expResult, result);
        
        System.out.println("getMethod RSA");
        instance = new InputJSON(dataTest_2);
        expResult = "RSA";
        result = instance.getMethod();
        assertEquals(expResult, result);
    }

    /**
     * Test of getHash method, of class InputJSON.
     */
    @Test
    public void testGetHash() {
        System.out.println("getHash");
        InputJSON instance = new InputJSON(dataTest_1);
        String expResult = "SHA-256";
        String result = instance.getHash();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getSpki method, of class InputJSON.
     */
    @Test
    public void testGetSpki() {
        System.out.println("getSpki");
        InputJSON instance = new InputJSON(dataTest_1);
        String expResult = "405FD8A83BFB64683BAEB51D9F8D99C9D872FA63";
        String result = instance.getSpki();
        assertEquals(expResult, result);
    }

    /**
     * Test of getInput method, of class InputJSON.
     */
    @Test
    public void testGetInput() {
        System.out.println("getInput");
        InputJSON instance = new InputJSON(dataTest_1);
        String expResult = " VW2zg9TGHpvr1wIuMlzX2VmiUlevpDggVD4MTxBy4iPLHzAZ7JlW7DNA/hJPZoSGtfWUgKqBcvI4RfMOEA3hwQMAF0EEJx+gdxklHMZL20TA6NAe+7MsF77txd9rp7E8LkRhOaBiNXYVwNqKFrhFOpfocmlMn4t/yCMukIo0k3wHyZg8gA==";
        String result = instance.getInput();
        assertEquals(expResult, result);
    }

    /**
     * Test of getProtocol method, of class InputJSON.
     */
    @Test
    public void testGetProtocol() {
        System.out.println("getProtocol");
        InputJSON instance = new InputJSON(dataTest_1);
        String expResult = InputJSON.DTLS_1_0;
        String result = instance.getProtocol();
        assertEquals(expResult, result);
        
        instance = new InputJSON(dataTest_2);
        expResult = InputJSON.TLS_1_2;
        result = instance.getProtocol();
        assertEquals(expResult, result);
    }

    /**
     * Test of checkValidJSON method, of class InputJSON.
     */
    @Test
    public void testCheckValidJSON() {
        System.out.println("checkValidJSON");
        InputJSON instance = new InputJSON(dataTest_1);
        assertEquals(null, instance.checkValidJSON());
        
        instance = new InputJSON(dataTest_2);
        assertEquals(null, instance.checkValidJSON());
        
        String notValidJSON = "{ \"method\":\"ECDHE\", "
                + "\"spki\":\"405FD8A83BFB64683BAEB51D9F8D99C9D872FA63\", "
                + "\"hash\":\"SHA-256\", "
                + "\"input\":\" VW2zg9TGHpvr1wIuMlzX2VmiUlevpDggVD4MTxBy4iPLHzAZ"
                + "7JlW7DNA/hJPZoSGtfWUgKqBcvI4RfMOEA3hwQMAF0EEJx+gdxklHMZL20TA6"
                + "NAe+7MsF77txd9rp7E8LkRhOaBiNXYVwNqKFrhFOpfocmlMn4t/yCMukIo0k3"
                + "wHyZg8gA==\" }";
        instance = new InputJSON(notValidJSON);
        assertEquals(ErrorJSON.ERR_MALFORMED_REQUEST, instance.checkValidJSON());
    }
}

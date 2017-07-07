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
package es.tid.keyserver.https.protocol;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * This class contains the Ericsson API Interface Description for it use on KeyServer.
 * @author <a href="mailto:jgm1986@hotmail.com">Javier Martinez Gusano</a>
 * @since v0.1.0
 */
public class InputJSONTest {
    /**
     * Test content data type 1: (Valid)
     *     - Protocol DTLS 1.0
     *     - Method: ECDHE
     *     - Hash: SHA-256
     */
    private final String dataTest_1;
    
    /**
     * Test content data type 2: (Valid)
     *     - Protocol DTLS 1.2
     *     - Method: ECDHE
     *     - Hash: (not present SHA1 default)
     */
    private final String dataTest_2;
    
    /**
     * Test content data type 3: (Valid)
     *     - Protocol DTLS 1.0
     *     - Method: ECDHE
     *     - Hash: SHA-224
     */
    private final String dataTest_3;
    
    /**
     * Test content data type 4: (Valid)
     *     - Protocol TLS 1.1
     *     - Method: ECDHE
     *     - Hash: SHA1
     */
    private final String dataTest_4;
    
    /**
     * Test content data type 5: (Valid)
     *     - Protocol TLS 1.2
     *     - Method: ECDHE
     *     - Hash: (not present SHA1 default)
     */
    private final String dataTest_5;
    
    /**
     * Test content data type 6: (Valid)
     *     - Protocol TLS 1.0
     *     - Method: ECDHE
     *     - Hash: SHA-384
     */
    private final String dataTest_6;
    
    /**
     * Test content data type 7: (Valid)
     *     - Protocol TLS 1.0
     *     - Method: ECDHE
     *     - Hash: SHA-512
     */
    private final String dataTest_7;
    
    /**
     * Test content data type 8: : (Not Valid JSON - Malformed).
     */
    private final String dataTest_8;
    
    /**
     * Test content data type 9: (Not Valid)
     *     - Protocol TLS 1.0
     *     - Method: NOT_VALID
     *     - Hash: SHA1
     */
    private final String dataTest_9;
    
    /**
     * Test content data type 10: (Not Valid)
     *     - Protocol TLS 1.0
     *     - Method: RSA
     *     - Hash: NOT_VALID
     */
    private final String dataTest_10;
    
    /**
     * Test content data type 11: (Not Valid)
     *     - Protocol NOT_VALID
     *     - Method: RSA
     *     - Hash: 
     */
    private final String dataTest_11;
    
    /**
     * Test content data type 12: (Valid)
     *     - Protocol TLS 1.0
     *     - Method: RSA
     *     - Hash: SHA1
     */
    private final String dataTest_12;
    
    /**
     * Test content data type 13: (Not Valid - SPKI field not present)
     */
    private final String dataTest_13;
    
    /**
     * Test content data type 14: (Not Valid - INPUT field not present)
     */
    private final String dataTest_14;
    
    /**
     * Test content data type 14: (ECDHE input length not Valid)
     *     - Data input length not valid (greater than 133bytes)
     */
    private final String dataTest_15;
    
    /**
     * Test content data type 14: (RSA input field length not Valid)
     *     - Data input length not valid (lower than 10bytes)
     */
    private final String dataTest_16;

    /**
     * Initialize data test with this constructor for the class tests.
     */
    public InputJSONTest() {
        dataTest_1 = "{ \"method\":\"ECDHE\", "
                + "\"protocol\":\"DTLS 1.0\", "
                + "\"spki\":\"405FD8A83BFB64683BAEB51D9F8D99C9D872FA63\", "
                + "\"hash\":\"SHA-256\", "
                + "\"input\":\" VW2zg9TGHpvr1wIuMlzX2VmiUlevpDggVD4MTxBy4iPLHzAZ"
                + "7JlW7DNA/hJPZoSGtfWUgKqBcvI4RfMOEA3hwQMAF0EEJx+gdxklHMZL20TA6"
                + "NAe+7MsF77txd9rp7E8LkRhOaBiNXYVwNqKFrhFOpfocmlMn4t/yCMukIo0k3"
                + "wHyZg8gA==\" }";
        
        dataTest_2 = "{ \"method\":\"ECDHE\", "
                + "\"protocol\":\"DTLS 1.2\", "
                + "\"spki\":\"405FD8A83BFB64683BAEB51D9F8D99C9D872FA63\", "
                + "\"input\":\" VW2zg9TGHpvr1wIuMlzX2VmiUlevpDggVD4MTxBy4iPLHzAZ"
                + "7JlW7DNA/hJPZoSGtfWUgKqBcvI4RfMOEA3hwQMAF0EEJx+gdxklHMZL20TA6"
                + "NAe+7MsF77txd9rp7E8LkRhOaBiNXYVwNqKFrhFOpfocmlMn4t/yCMukIo0k3"
                + "wHyZg8gA==\" }";
        
        dataTest_3 = "{ \"method\":\"ECDHE\", "
                + "\"protocol\":\"TLS 1.0\", "
                + "\"spki\":\"405FD8A83BFB64683BAEB51D9F8D99C9D872FA63\", "
                + "\"hash\":\"SHA-224\", "
                + "\"input\":\" VW2zg9TGHpvr1wIuMlzX2VmiUlevpDggVD4MTxBy4iPLHzAZ"
                + "7JlW7DNA/hJPZoSGtfWUgKqBcvI4RfMOEA3hwQMAF0EEJx+gdxklHMZL20TA6"
                + "NAe+7MsF77txd9rp7E8LkRhOaBiNXYVwNqKFrhFOpfocmlMn4t/yCMukIo0k3"
                + "wHyZg8gA==\" }";
        
        dataTest_4 = "{ \"method\":\"ECDHE\", "
                + "\"protocol\":\"TLS 1.1\", "
                + "\"spki\":\"405FD8A83BFB64683BAEB51D9F8D99C9D872FA63\", "
                + "\"hash\":\"SHA1\", "
                + "\"input\":\" VW2zg9TGHpvr1wIuMlzX2VmiUlevpDggVD4MTxBy4iPLHzAZ"
                + "7JlW7DNA/hJPZoSGtfWUgKqBcvI4RfMOEA3hwQMAF0EEJx+gdxklHMZL20TA6"
                + "NAe+7MsF77txd9rp7E8LkRhOaBiNXYVwNqKFrhFOpfocmlMn4t/yCMukIo0k3"
                + "wHyZg8gA==\" }";
        
        dataTest_5 = "{ \"method\":\"ECDHE\", "
                + "\"protocol\":\"TLS 1.2\", "
                + "\"spki\":\"405FD8A83BFB64683BAEB51D9F8D99C9D872FA63\", "
                + "\"input\":\" VW2zg9TGHpvr1wIuMlzX2VmiUlevpDggVD4MTxBy4iPLHzAZ"
                + "7JlW7DNA/hJPZoSGtfWUgKqBcvI4RfMOEA3hwQMAF0EEJx+gdxklHMZL20TA6"
                + "NAe+7MsF77txd9rp7E8LkRhOaBiNXYVwNqKFrhFOpfocmlMn4t/yCMukIo0k3"
                + "wHyZg8gA==\" }";
        
        dataTest_6 = "{ \"method\":\"ECDHE\", "
                + "\"protocol\":\"TLS 1.0\", "
                + "\"spki\":\"405FD8A83BFB64683BAEB51D9F8D99C9D872FA63\", "
                + "\"hash\":\"SHA-384\", "
                + "\"input\":\" VW2zg9TGHpvr1wIuMlzX2VmiUlevpDggVD4MTxBy4iPLHzAZ"
                + "7JlW7DNA/hJPZoSGtfWUgKqBcvI4RfMOEA3hwQMAF0EEJx+gdxklHMZL20TA6"
                + "NAe+7MsF77txd9rp7E8LkRhOaBiNXYVwNqKFrhFOpfocmlMn4t/yCMukIo0k3"
                + "wHyZg8gA==\" }";
        
        dataTest_7 = "{ \"method\":\"ECDHE\", "
                + "\"protocol\":\"TLS 1.0\", "
                + "\"spki\":\"405FD8A83BFB64683BAEB51D9F8D99C9D872FA63\", "
                + "\"hash\":\"SHA-512\", "
                + "\"input\":\" VW2zg9TGHpvr1wIuMlzX2VmiUlevpDggVD4MTxBy4iPLHzAZ"
                + "7JlW7DNA/hJPZoSGtfWUgKqBcvI4RfMOEA3hwQMAF0EEJx+gdxklHMZL20TA6"
                + "NAe+7MsF77txd9rp7E8LkRhOaBiNXYVwNqKFrhFOpfocmlMn4t/yCMukIo0k3"
                + "wHyZg8gA==\" }";
        
        dataTest_8 = "{\n"
                + "     \"protocol\":\"TLS 1.0\", "
                + "	\"method\":\"RSA\",\n"
                + "     \"hash\":\"SHA1\", "
                + "	\"spki\":\"193D57F655228025FCB8140933BE466BAB5D8E88\",\n"
                + "	\"input\":\"hNYw6/ah/SRXkvJc7WU8TQJd8wH7sUnhJ/4kewnZdGgLINtxEfMk7QeQiFH6Z8LpCgyqYeXoSMHmdAy2MMhseZl34vsFpY2ZZsB8exBzefxS4W55mTaILA6ZWkwVzCKESSRYwf+297XU7OTNxLqB02/DYR9Hr4/vXFfXg38aZQsMpHpDYzRrR9pEX5FNh/MIcBuEJDqi1ldjCREYO5I0LDb6lq9aIFCZKyb6pC6uDeTjgrQaMPWAl1S/jIHGvsRYJBktHAXBFVqWQuDl0WluxyYT++zW2/CuKH8QBDTcm/u9vS5M8RgeWqYrZxNzUwGkdHRuESL/HrdeDZiYtEFH4g==\"\n";
        
        dataTest_9 = "{ \"method\":\"NOT_VALID\", "
                + "\"protocol\":\"DTLS 1.0\", "
                + "\"spki\":\"405FD8A83BFB64683BAEB51D9F8D99C9D872FA63\", "
                + "\"hash\":\"SHA-256\", "
                + "\"input\":\" VW2zg9TGHpvr1wIuMlzX2VmiUlevpDggVD4MTxBy4iPLHzAZ"
                + "7JlW7DNA/hJPZoSGtfWUgKqBcvI4RfMOEA3hwQMAF0EEJx+gdxklHMZL20TA6"
                + "NAe+7MsF77txd9rp7E8LkRhOaBiNXYVwNqKFrhFOpfocmlMn4t/yCMukIo0k3"
                + "wHyZg8gA==\" }";
        
        dataTest_10 = "{\n"
                + "     \"protocol\":\"TLS 1.0\", "
                + "	\"method\":\"ECDHE\",\n"
                + "     \"hash\":\"NOT_VALID\", "
                + "	\"spki\":\"193D57F655228025FCB8140933BE466BAB5D8E88\",\n"
                + "	\"input\":\"hNYw6/ah/SRXkvJc7WU8TQJd8wH7sUnhJ/4kewnZdGgLINtxEfMk7QeQiFH6Z8LpCgyqYeXoSMHmdAy2MMhseZl34vsFpY2ZZsB8exBzefxS4W55mTaILA6ZWkwVzCKESSRYwf+297XU7OTNxLqB02/DYR9Hr4/vXFfXg38aZQsMpHpDYzRrR9pEX5FNh/MIcBuEJDqi1ldjCREYO5I0LDb6lq9aIFCZKyb6pC6uDeTjgrQaMPWAl1S/jIHGvsRYJBktHAXBFVqWQuDl0WluxyYT++zW2/CuKH8QBDTcm/u9vS5M8RgeWqYrZxNzUwGkdHRuESL/HrdeDZiYtEFH4g==\"\n"
                + "}";
        
        dataTest_11 = "{\n"
                + "     \"protocol\":\"NOT_VALID\", "
                + "	\"method\":\"RSA\",\n"
                + "     \"hash\":\"SHA1\", "
                + "	\"spki\":\"193D57F655228025FCB8140933BE466BAB5D8E88\",\n"
                + "	\"input\":\"hNYw6/ah/SRXkvJc7WU8TQJd8wH7sUnhJ/4kewnZdGgLINtxEfMk7QeQiFH6Z8LpCgyqYeXoSMHmdAy2MMhseZl34vsFpY2ZZsB8exBzefxS4W55mTaILA6ZWkwVzCKESSRYwf+297XU7OTNxLqB02/DYR9Hr4/vXFfXg38aZQsMpHpDYzRrR9pEX5FNh/MIcBuEJDqi1ldjCREYO5I0LDb6lq9aIFCZKyb6pC6uDeTjgrQaMPWAl1S/jIHGvsRYJBktHAXBFVqWQuDl0WluxyYT++zW2/CuKH8QBDTcm/u9vS5M8RgeWqYrZxNzUwGkdHRuESL/HrdeDZiYtEFH4g==\"\n"
                + "}";
        
        dataTest_12 = "{\n"
                + "     \"protocol\":\"TLS 1.0\", "
                + "	\"method\":\"RSA\",\n"
                + "     \"hash\":\"SHA1\", "
                + "	\"spki\":\"193D57F655228025FCB8140933BE466BAB5D8E88\",\n"
                + "	\"input\":\"hNYw6/ah/SRXkvJc7WU8TQJd8wH7sUnhJ/4kewnZdGgLINtxEfMk7QeQiFH6Z8LpCgyqYeXoSMHmdAy2MMhseZl34vsFpY2ZZsB8exBzefxS4W55mTaILA6ZWkwVzCKESSRYwf+297XU7OTNxLqB02/DYR9Hr4/vXFfXg38aZQsMpHpDYzRrR9pEX5FNh/MIcBuEJDqi1ldjCREYO5I0LDb6lq9aIFCZKyb6pC6uDeTjgrQaMPWAl1S/jIHGvsRYJBktHAXBFVqWQuDl0WluxyYT++zW2/CuKH8QBDTcm/u9vS5M8RgeWqYrZxNzUwGkdHRuESL/HrdeDZiYtEFH4g==\"\n"
                + "}";
        
        dataTest_13 = "{\n"
                + "     \"protocol\":\"TLS 1.0\", "
                + "	\"method\":\"RSA\",\n"
                + "     \"hash\":\"SHA1\", "
                + "	\"input\":\"hNYw6/ah/SRXkvJc7WU8TQJd8wH7sUnhJ/4kewnZdGgLINtxEfMk7QeQiFH6Z8LpCgyqYeXoSMHmdAy2MMhseZl34vsFpY2ZZsB8exBzefxS4W55mTaILA6ZWkwVzCKESSRYwf+297XU7OTNxLqB02/DYR9Hr4/vXFfXg38aZQsMpHpDYzRrR9pEX5FNh/MIcBuEJDqi1ldjCREYO5I0LDb6lq9aIFCZKyb6pC6uDeTjgrQaMPWAl1S/jIHGvsRYJBktHAXBFVqWQuDl0WluxyYT++zW2/CuKH8QBDTcm/u9vS5M8RgeWqYrZxNzUwGkdHRuESL/HrdeDZiYtEFH4g==\"\n"
                + "}";
        
        dataTest_14 = "{\n"
                + "     \"protocol\":\"TLS 1.0\", "
                + "	\"method\":\"RSA\",\n"
                + "     \"hash\":\"SHA1\", "
                + "	\"spki\":\"193D57F655228025FCB8140933BE466BAB5D8E88\"\n"
                + "}";
        
        dataTest_15 = "{ \"method\":\"ECDHE\", "
                + "\"protocol\":\"DTLS 1.0\", "
                + "\"spki\":\"405FD8A83BFB64683BAEB51D9F8D99C9D872FA63\", "
                + "\"hash\":\"SHA-256\", "
                + "\"input\":\" VW0eAi4yXFlSVzggVD4MTxByIx8wGVYzQBJPZnI4RQ4QDQoDF0EEJx93GSUcS0QeLBdrPC5EYTliNXYVFkU6cmlMfyMuODR8Bzw=\" }";
        
        dataTest_16 = "{\n"
                + "     \"protocol\":\"TLS 1.0\", "
                + "	\"method\":\"RSA\",\n"
                + "     \"hash\":\"SHA1\", "
                + "	\"spki\":\"193D57F655228025FCB8140933BE466BAB5D8E88\",\n"
                + "	\"input\":\"VQ==\"\n"
                + "}";
    }

    /**
     * Test of getMethod method, of class InputJSON.
     */
    @Test
    public void testGetMethod1() {
        System.out.println("getMethod ECDHE");
        InputJSON instance = new InputJSON(dataTest_1);
        String expResult = "ECDHE";
        String result = instance.getMethod();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getMethod method, of class InputJSON.
     */
    @Test
    public void testGetMethod2() {
        System.out.println("getMethod RSA");
        InputJSON instance = new InputJSON(dataTest_12);
        String expResult = "RSA";
        String result = instance.getMethod();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getMethod method, of class InputJSON.
     */
    @Test
    public void testGetMethod3() {
        System.out.println("getMethod NOT_VALID");
        InputJSON instance = new InputJSON(dataTest_9);
        String expResult = null;
        String result = instance.getMethod();
        assertEquals(expResult, result);
    }

    /**
     * Test of getHash method, of class InputJSON.
     */
    @Test
    public void testGetHash1() {
        System.out.println("getHash1");
        InputJSON instance = new InputJSON(dataTest_1);
        String expResult = "SHA-256";
        String result = instance.getHash();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getHash method, of class InputJSON.
     */
    @Test
    public void testGetHash2() {
        System.out.println("getHash2");
        InputJSON instance = new InputJSON(dataTest_2);
        String expResult = "SHA1";
        String result = instance.getHash();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getHash method, of class InputJSON.
     */
    @Test
    public void testGetHash3() {
        System.out.println("getHash3");
        InputJSON instance = new InputJSON(dataTest_3);
        String expResult = "SHA-224";
        String result = instance.getHash();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getHash method, of class InputJSON.
     */
    @Test
    public void testGetHash4() {
        System.out.println("getHash4");
        InputJSON instance = new InputJSON(dataTest_4);
        String expResult = "SHA1";
        String result = instance.getHash();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getHash method, of class InputJSON.
     */
    @Test
    public void testGetHash5() {
        System.out.println("getHash5");
        InputJSON instance = new InputJSON(dataTest_7);
        String expResult = "SHA-512";
        String result = instance.getHash();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getHash method, of class InputJSON.
     */
    @Test
    public void testGetHash6() {
        System.out.println("getHash6");
        InputJSON instance = new InputJSON(dataTest_6);
        String expResult = "SHA-384";
        String result = instance.getHash();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getHash method, of class InputJSON.
     */
    @Test
    public void testGetHash7() {
        System.out.println("getHash7");
        InputJSON instance = new InputJSON(dataTest_10);
        String expResult = null;
        String result = instance.getHash();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getSpki method, of class InputJSON.
     */
    @Test
    public void testGetSpki1() {
        System.out.println("getSpki1");
        InputJSON instance = new InputJSON(dataTest_1);
        String expResult = "405FD8A83BFB64683BAEB51D9F8D99C9D872FA63";
        String result = instance.getSpki();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getSpki method, of class InputJSON.
     */
    @Test
    public void testGetSpki2() {
        System.out.println("getSpki2");
        InputJSON instance = new InputJSON(dataTest_13);
        String expResult = null;
        String result = instance.getSpki();
        assertEquals(expResult, result);
    }

    /**
     * Test of getInput method, of class InputJSON.
     */
    @Test
    public void testGetInput1() {
        System.out.println("getInput1");
        InputJSON instance = new InputJSON(dataTest_1);
        String expResult = " VW2zg9TGHpvr1wIuMlzX2VmiUlevpDggVD4MTxBy4iPLHzAZ7JlW7DNA/hJPZoSGtfWUgKqBcvI4RfMOEA3hwQMAF0EEJx+gdxklHMZL20TA6NAe+7MsF77txd9rp7E8LkRhOaBiNXYVwNqKFrhFOpfocmlMn4t/yCMukIo0k3wHyZg8gA==";
        String result = instance.getInput();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getInput method, of class InputJSON.
     */
    @Test
    public void testGetInput2() {
        System.out.println("getInput2");
        InputJSON instance = new InputJSON(dataTest_14);
        String expResult = null;
        String result = instance.getInput();
        assertEquals(expResult, result);
    }

    /**
     * Test of getProtocol method, of class InputJSON.
     */
    @Test
    public void testGetProtocol1() {
        System.out.println("getProtocol1");
        InputJSON instance = new InputJSON(dataTest_1);
        String expResult = InputJSON.DTLS_1_0;
        String result = instance.getProtocol();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getProtocol method, of class InputJSON.
     */
    @Test
    public void testGetProtocol2() {
        System.out.println("getProtocol2");
        InputJSON instance = new InputJSON(dataTest_5);
        String expResult = InputJSON.TLS_1_2;
        String result = instance.getProtocol();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getProtocol method, of class InputJSON.
     */
    @Test
    public void testGetProtocol3() {
        System.out.println("getProtocol3");
        InputJSON instance = new InputJSON(dataTest_3);
        String expResult = InputJSON.TLS_1_0;
        String result = instance.getProtocol();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getProtocol method, of class InputJSON.
     */
    @Test
    public void testGetProtocol4() {
        System.out.println("getProtocol4");
        InputJSON instance = new InputJSON(dataTest_4);
        String expResult = InputJSON.TLS_1_1;
        String result = instance.getProtocol();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getProtocol method, of class InputJSON.
     */
    @Test
    public void testGetProtocol5() {
        System.out.println("getProtocol5");
        InputJSON instance = new InputJSON(dataTest_2);
        String expResult = InputJSON.DTLS_1_2;
        String result = instance.getProtocol();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getProtocol method, of class InputJSON.
     */
    @Test
    public void testGetProtocol6() {
        System.out.println("getProtocol6");
        InputJSON instance = new InputJSON(dataTest_11);
        String expResult = null;
        String result = instance.getProtocol();
        assertEquals(expResult, result);
    }

    /**
     * Test of checkValidJSON method, of class InputJSON.
     */
    @Test
    public void testCheckValidJSON1() {
        System.out.println("checkValidJSON1");
        InputJSON instance = new InputJSON(dataTest_1);
        assertEquals(null, instance.checkValidJSON());
    }
    
    /**
     * Test of checkValidJSON method, of class InputJSON.
     */
    @Test
    public void testCheckValidJSON2() {        
        InputJSON instance = new InputJSON(dataTest_2);
        assertEquals(null, instance.checkValidJSON());
    }
    
    /**
     * Test of checkValidJSON method, of class InputJSON.
     */
    @Test
    public void testCheckValidJSON3() {        
        InputJSON instance = new InputJSON(dataTest_3);
        assertEquals(null, instance.checkValidJSON());
    }
    
    /**
     * Test of checkValidJSON method, of class InputJSON.
     */
    @Test
    public void testCheckValidJSON4() {        
        InputJSON instance = new InputJSON(dataTest_4);
        assertEquals(null, instance.checkValidJSON());
    }
    
    /**
     * Test of checkValidJSON method, of class InputJSON.
     */
    @Test
    public void testCheckValidJSON5() {        
        InputJSON instance = new InputJSON(dataTest_5);
        assertEquals(null, instance.checkValidJSON());
    }
    
    /**
     * Test of checkValidJSON method, of class InputJSON.
     */
    @Test
    public void testCheckValidJSON6() {        
        InputJSON instance = new InputJSON(dataTest_6);
        assertEquals(null, instance.checkValidJSON());
    }
    
    /**
     * Test of checkValidJSON method, of class InputJSON.
     */
    @Test
    public void testCheckValidJSON7() {        
        InputJSON instance = new InputJSON(dataTest_7);
        assertEquals(null, instance.checkValidJSON());
    }
    
    /**
     * Test of checkValidJSON method, of class InputJSON.
     */
    @Test
    public void testCheckValidJSON8() {        
        InputJSON instance = new InputJSON(dataTest_8);
        assertEquals(ErrorJSON.ERR_MALFORMED_REQUEST, instance.checkValidJSON());
    }
    
    /**
     * Test of checkValidJSON method, of class InputJSON.
     */
    @Test
    public void testCheckValidJSON9() {        
        InputJSON instance = new InputJSON(dataTest_9);
        assertEquals(ErrorJSON.ERR_MALFORMED_REQUEST, instance.checkValidJSON());
    }
    
    /**
     * Test of checkValidJSON method, of class InputJSON.
     */
    @Test
    public void testCheckValidJSON10() {        
        InputJSON instance = new InputJSON(dataTest_10);
        assertEquals(ErrorJSON.ERR_MALFORMED_REQUEST, instance.checkValidJSON());
    }
    
    /**
     * Test of checkValidJSON method, of class InputJSON.
     */
    @Test
    public void testCheckValidJSON11() {        
        InputJSON instance = new InputJSON(dataTest_11);
        assertEquals(ErrorJSON.ERR_MALFORMED_REQUEST, instance.checkValidJSON());
    }
    
    /**
     * Test of checkValidJSON method, of class InputJSON.
     */
    @Test
    public void testCheckValidJSON12() {        
        InputJSON instance = new InputJSON(dataTest_12);
        assertEquals(null, instance.checkValidJSON());
    }
    
    /**
     * Test of checkValidJSON method, of class InputJSON.
     */
    @Test
    public void testCheckValidJSON13() {
        String notValidJSON = "{ \"method\":\"ECDHE\", "
                + "\"spki\":\"405FD8A83BFB64683BAEB51D9F8D99C9D872FA63\", "
                + "\"hash\":\"SHA-256\", "
                + "\"input\":\" VW2zg9TGHpvr1wIuMlzX2VmiUlevpDggVD4MTxBy4iPLHzAZ"
                + "7JlW7DNA/hJPZoSGtfWUgKqBcvI4RfMOEA3hwQMAF0EEJx+gdxklHMZL20TA6"
                + "NAe+7MsF77txd9rp7E8LkRhOaBiNXYVwNqKFrhFOpfocmlMn4t/yCMukIo0k3"
                + "wHyZg8gA==\" }";
        InputJSON instance = new InputJSON(notValidJSON);
        assertEquals(ErrorJSON.ERR_MALFORMED_REQUEST, instance.checkValidJSON());
    }
    
    /**
     * Test of checkValidJSON method, of class InputJSON.
     */
    @Test
    public void testCheckValidJSON14() {        
        InputJSON instance = new InputJSON(dataTest_13);
        assertEquals(ErrorJSON.ERR_MALFORMED_REQUEST, instance.checkValidJSON());
    }
    
    /**
     * Test of checkValidJSON method, of class InputJSON.
     */
    @Test
    public void testCheckValidJSON15() {        
        InputJSON instance = new InputJSON(dataTest_14);
        assertEquals(ErrorJSON.ERR_MALFORMED_REQUEST, instance.checkValidJSON());
    }
    
    /**
     * Test of checkValidJSON method, of class InputJSON.
     */
    @Test
    public void testCheckValidJSON16() {        
        InputJSON instance = new InputJSON(dataTest_15);
        assertEquals(ErrorJSON.ERR_MALFORMED_REQUEST, instance.checkValidJSON());
    }
    
    /**
     * Test of checkValidJSON method, of class InputJSON.
     */
    @Test
    public void testCheckValidJSON17() {        
        InputJSON instance = new InputJSON(dataTest_16);
        assertEquals(ErrorJSON.ERR_MALFORMED_REQUEST, instance.checkValidJSON());
    }
}

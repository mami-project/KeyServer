/*
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
package es.tid.keyserver.https.protocol;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test class for output error JSON messages.
 * @author <a href="mailto:jgm1986@hotmail.com">Javier Gusano Martinez</a>
 * @since v0.4.2
 */
public class ErrorJSONTest {
    /**
     * Test of toString method, of class ErrorJSON.
     * @since v0.4.2
     */
    @Test
    public void testToString1() {
        System.out.println("toString1");
        ErrorJSON instance = new ErrorJSON(ErrorJSON.ERR_MALFORMED_REQUEST);
        String expResult = "{\"error\":\"malformed request\"}";
        String result = instance.toString();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of toString method, of class ErrorJSON.
     * @since v0.4.2
     */
    @Test
    public void testToString2() {
        System.out.println("toString2");
        ErrorJSON instance = new ErrorJSON(ErrorJSON.ERR_NOT_FOUND);
        String expResult = "{\"error\":\"spki not found\"}";
        String result = instance.toString();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of toString method, of class ErrorJSON.
     * @since v0.4.2
     */
    @Test
    public void testToString3() {
        System.out.println("toString3");
        ErrorJSON instance = new ErrorJSON(ErrorJSON.ERR_REQUEST_DENIED);
        String expResult = "{\"error\":\"request denied\"}";
        String result = instance.toString();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of toString method, of class ErrorJSON.
     * @since v0.4.2
     */
    @Test
    public void testToString4() {
        System.out.println("toString4");
        ErrorJSON instance = new ErrorJSON(ErrorJSON.ERR_UNSPECIFIED);
        String expResult = "{\"error\":\"unspecified error\"}";
        String result = instance.toString();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of toString method, of class ErrorJSON.
     * @since v0.4.2
     */
    @Test
    public void testToString5() {
        System.out.println("toString5");
        ErrorJSON instance = new ErrorJSON("NOT_VALID_ERROR");
        String expResult = "{\"error\":\"unspecified error\"}";
        String result = instance.toString();
        assertEquals(expResult, result);
    }
}

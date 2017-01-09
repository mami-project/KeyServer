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
import static org.junit.Assert.*;

/**
 * Test class for response JSON objects.
 * @author <a href="mailto:jgm1986@hotmail.com">Javier Gusano Martinez</a>
 * @since v0.4.2
 */
public class ResponseJSONTest {
    /**
     * Test of setOutputData method, of class ResponseJSON.
     */
    @Test
    public void testSetOutputData() {
        System.out.println("setOutputData");
        String label = "l1";
        String data = "d1";
        ResponseJSON instance = new ResponseJSON(label, data);
        instance.setOutputData("l2", "d2");
        String expResult = "{\"l1\":\"d1\",\"l2\":\"d2\"}";
        assertEquals(expResult, instance.toString());
    }

    /**
     * Test of toString method, of class ResponseJSON.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        ResponseJSON instance = new ResponseJSON("lab1", "data1");
        String expResult = "{\"lab1\":\"data1\"}";
        String result = instance.toString();
        assertEquals(expResult, result);
    }
}

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
 * Test class for output JSON messages.
 * @author <a href="mailto:jgm1986@hotmail.com">Javier Martinez Gusano</a>
 * @since v0.4.2
 */
public class OutputJSONTest {
    /**
     * Test of toString method, of class OutputJSON.
     * @since v0.4.2
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        OutputJSON instance = new OutputJSON("OUTPUT_FIELD");
        String expResult = "{\"output\":\"OUTPUT_FIELD\"}";
        String result = instance.toString();
        assertEquals(expResult, result);
    }
}

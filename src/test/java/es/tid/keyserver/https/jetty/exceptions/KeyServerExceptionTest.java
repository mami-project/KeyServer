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
package es.tid.keyserver.https.jetty.exceptions;

import org.junit.Test;

/**
 * Class unitary test KeyServer exception.
 * @author <a href="mailto:jgm1986@hotmail.com">Javier Gusano Martinez</a>
 * @since v0.4.3
 */
public class KeyServerExceptionTest {
    /**
     * KeyServer custom exception test.
     * @throws KeyServerException Exception message.
     */
    @Test(expected = KeyServerException.class)
    public void testSomeMethod() throws KeyServerException {
        System.out.println("throwKeyServerException");
        throw new KeyServerException("KeyServer Dummy Exception.");
    }
}

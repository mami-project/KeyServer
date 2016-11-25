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
package es.tid.keyserver.https.certificate;

import java.security.KeyStore;
import java.util.Date;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for https certificate class controller.
 * @author <a href="mailto:jgm1986@hotmail.com">Javier Gusano Martinez</a>
 * @since v0.3.0
 */
public class HttpsCertTest {
	/**
	 * HTTPs certificate object.
	 */
    private HttpsCert instance;
    
    /**
     * Class constructor object.
     */
    public HttpsCertTest() {
        instance = new HttpsCert("target/test-classes/ksserverkey.jks", "123456");
    }

    /**
     * Test of getCertificate method, of class HttpsCert.
     */
    @Test
    public void testGetCertificate() {
        System.out.println("getCertificate");
        KeyStore result = instance.getCertificate();
        System.out.println("[ TEST ] Certificate type: " + result.getType());
        assertNotNull(result);
    }

    /**
     * Test of certExpirDate method, of class HttpsCert.
     */
    @Test
    public void testCertExpirDate() {
        System.out.println("certExpirDate");
        Date result = instance.certExpirDate();
        System.out.println("[ TEST ] Certificate expration date: " + result);
        assertNotNull(result);
    }

    /**
     * Test of certRemainDays method, of class HttpsCert.
     */
    @Test
    public void testCertRemainDays() {
        System.out.println("certRemainDays");
        long result = instance.certRemainDays();
        System.out.println("[ TEST ] Certificate validity remain days: " + result);
        assertNotNull((Long)result);
    }

    /**
     * Test of isCorrectlyInitialized method, of class HttpsCert.
     */
    @Test
    public void testIsCorrectlyInitialized() {
        System.out.println("isCorrectlyInitialized");
        boolean expResult = true;
        boolean result = instance.isCorrectlyInitialized();
        assertEquals(expResult, result);
    }

    /**
     * Test of isValid method, of class HttpsCert.
     */
    @Test
    public void testIsValid() {
        System.out.println("isValid");
        boolean expResult = true;
        boolean result = instance.isValid();
        assertEquals(expResult, result);
    }
    
}

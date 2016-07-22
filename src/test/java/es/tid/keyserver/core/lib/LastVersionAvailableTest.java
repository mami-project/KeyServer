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
package es.tid.keyserver.core.lib;

import junit.framework.Assert;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for the 'LastVersionAvailble' KeyServer class.
 * @author <a href="mailto:jgm1986@hotmail.com">Javier Gusano Martinez</a>
 * @since v0.3.0
 */
public class LastVersionAvailableTest {
    /**
     * Test repository URL string.
     */
    private String repoUrl;
    
    public LastVersionAvailableTest() {
        repoUrl = "https://github.com/mami-project/KeyServer/releases/latest";
    }
  
    /**
     * Test of getLastVersionAvailable method, of class LastVersionAvailable.
     */
    @Test
    public void testGetLastVersionAvailable() {
        System.out.println("getLastVersionAvailable");
        LastVersionAvailable instance = new LastVersionAvailable(repoUrl);
        String expResult = "releases";
        String result = instance.getLastVersionAvailable();
        System.out.println("[ TEST ] Last version available: " + result);
        Assert.assertTrue((result.equalsIgnoreCase(expResult))||(!result.isEmpty()));
    }

    /**
     * Test of isUpdated method, of class LastVersionAvailable.
     */
    @Test
    public void testIsUpdated() {
        System.out.println("isUpdated");
        String appVersion = "v0.1.2";
        LastVersionAvailable instance = new LastVersionAvailable(repoUrl);
        boolean result = instance.isUpdated(appVersion);
        System.out.println("[ TEST ]: Is the current version up to date? " + result);
    }

    /**
     * Test of refreshRepoStatus method, of class LastVersionAvailable.
     */
    @Test
    public void testRefreshRepoStatus() {
        System.out.println("refreshRepoStatus");
        LastVersionAvailable instance = new LastVersionAvailable(repoUrl);
        instance.refreshRepoStatus();
        boolean expResult = true;
        boolean result = instance.isCorrectlyInitialized();
        assertEquals(expResult, result);
    }

    /**
     * Test of isCorrectlyInitialized method, of class LastVersionAvailable.
     */
    @Test
    public void testIsCorrectlyInitialized() {
        System.out.println("isCorrectlyInitialized");
        LastVersionAvailable instance = new LastVersionAvailable(repoUrl);
        boolean expResult = true;
        boolean result = instance.isCorrectlyInitialized();
        assertEquals(expResult, result);
    }
    
}

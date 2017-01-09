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

package es.tid.keyserver.core.lib;

import junit.framework.Assert;
import org.junit.Test;
import static junit.framework.Assert.assertEquals;

/**
 * Test class for the 'LastVersionAvailble' KeyServer class.
 * @author <a href="mailto:jgm1986@hotmail.com">Javier Gusano Martinez</a>
 * @since v0.3.0
 */
public class LastVersionAvailableTest {
    /**
     * Test repository URL string.
     */
    private static final String REPOURL = "https://github.com/mami-project/KeyServer/releases/latest";
      
    /**
     * Test of getLastVersionAvailable method, of class LastVersionAvailable.
     */
    @Test
    public void testGetLastVersionAvailable1() {
        System.out.println("getLastVersionAvailable1");
        LastVersionAvailable instance = new LastVersionAvailable(REPOURL);
        Version appVer = new Version(instance.getLastVersionAvailable());
        Assert.assertTrue((!instance.getLastVersionAvailable().isEmpty()) && 
                (appVer.getMajor() >= 0) &&
                (appVer.getMinor() >= 0) &&
                (appVer.getPatch() >= 0));
    }
    
    /**
     * Test of getLastVersionAvailable method, of class LastVersionAvailable.
     */
    @Test
    public void testGetLastVersionAvailable2() {
        System.out.println("getLastVersionAvailable2");
        String notValidURL = "@notvalid";
        LastVersionAvailable instance = new LastVersionAvailable(notValidURL);
        boolean expResult = false;
        boolean result = instance.isCorrectlyInitialized();
        assertEquals(expResult, result);
    }

    /**
     * Test of isUpdated method, of class LastVersionAvailable.
     */
    @Test
    public void testIsUpdated() {
        System.out.println("isUpdated");
        String appVersion = "v99999.99.99";
        LastVersionAvailable instance = new LastVersionAvailable(REPOURL);
        boolean result = instance.isUpdated(appVersion);
        System.out.println("[ TEST ]: Is the current version up to date? " + result);
        Assert.assertTrue(result);
    }

    /**
     * Test of refreshRepoStatus method, of class LastVersionAvailable.
     */
    @Test
    public void testRefreshRepoStatus1() {
        System.out.println("refreshRepoStatus1");
        LastVersionAvailable instance = new LastVersionAvailable(REPOURL);
        instance.refreshRepoStatus();
        boolean expResult = true;
        boolean result = instance.isCorrectlyInitialized();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of refreshRepoStatus method, of class LastVersionAvailable.
     */
    @Test
    public void testRefreshRepoStatus2() {
        System.out.println("refreshRepoStatus2");
        LastVersionAvailable instance = new LastVersionAvailable("@notvalid");
        instance.refreshRepoStatus();
        boolean expResult = false;
        boolean result = instance.isCorrectlyInitialized();
        assertEquals(expResult, result);
    }

    /**
     * Test of isCorrectlyInitialized method, of class LastVersionAvailable.
     */
    @Test
    public void testIsCorrectlyInitialized() {
        System.out.println("isCorrectlyInitialized");
        LastVersionAvailable instance = new LastVersionAvailable(REPOURL);
        boolean expResult = true;
        boolean result = instance.isCorrectlyInitialized();
        assertEquals(expResult, result);
    }
}

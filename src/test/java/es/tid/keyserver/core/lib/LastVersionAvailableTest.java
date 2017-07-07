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

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Assume;

/**
 * Test class for the 'LastVersionAvailble' KeyServer class.
 * @author <a href="mailto:jgm1986@hotmail.com">Javier Martinez Gusano</a>
 * @since v0.3.0
 */
public class LastVersionAvailableTest {
    /**
     * Test repository URL string.
     */
    public static final String REPOURL = "https://api.github.com/repos/mami-project/KeyServer/releases/latest";
    
    /**
     * Flag GitHub URL connection available.
     */
    private boolean urlConnectionAvailable;
    
    /**
     * GitHub test object.
     */
    private LastVersionAvailable instance;
    
    /**
     * Test class constructor.
     * @author <a href="mailto:jgm1986@hotmail.com">Javier Martinez Gusano</a>
     * @since v0.4.4
     */
    public LastVersionAvailableTest(){
        try {
            URL urlServer = new URL(REPOURL);
            HttpURLConnection urlConn = (HttpURLConnection) urlServer.openConnection();
            urlConn.setConnectTimeout(10000); // 10 Seconds Timeout 
            urlConn.connect();
            urlConnectionAvailable = urlConn.getResponseCode() == 200;
            if(urlConnectionAvailable){
                instance = new LastVersionAvailable(REPOURL);
            }
        } catch (IOException e1) {
            System.out.println("[ WARNING ] Connection to GitHub is not available. JUnit tests will be skipped.");
            urlConnectionAvailable = false;
        }
    }

    /**
     * Test of getLastVersionAvailable method, of class LastVersionAvailable.
     */
    @Test
    public void testGetLastVersionAvailable1() {
        System.out.println("getLastVersionAvailable1");
        Assume.assumeTrue(this.urlConnectionAvailable); // If URL connection is not available, skip the test.
        System.out.println("[ INFO ] Last version available: " + instance.getLastVersionAvailable());
        Version appVer = new Version(instance.getLastVersionAvailable());
        assertTrue((!instance.getLastVersionAvailable().isEmpty()) && 
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
        Assume.assumeTrue(this.urlConnectionAvailable); // If URL connection is not available, skip the test.
        String notValidURL = "@notvalid";
        LastVersionAvailable invalidInstance = new LastVersionAvailable(notValidURL);
        boolean result = invalidInstance.isCorrectlyInitialized();
        assertFalse(result);
    }

    /**
     * Test of isUpdated method, of class LastVersionAvailable.
     */
    @Test
    public void testIsUpdated() {
        System.out.println("isUpdated");
        Assume.assumeTrue(this.urlConnectionAvailable); // If URL connection is not available, skip the test.
        String appVersion = "v99999.99.99";
        boolean result = instance.isUpdated(appVersion);
        System.out.println("[ TEST ]: Is the current version up to date? " + result);
        assertTrue(result);
    }

    /**
     * Test of refreshRepoStatus method, of class LastVersionAvailable.
     */
    @Test
    public void testRefreshRepoStatus1() {
        System.out.println("refreshRepoStatus1");
        Assume.assumeTrue(this.urlConnectionAvailable); // If URL connection is not available, skip the test.
        instance.refreshRepoStatus();
        boolean result = instance.isCorrectlyInitialized();
        assertTrue(result);
    }
    
    /**
     * Test of refreshRepoStatus method, of class LastVersionAvailable.
     */
    @Test
    public void testRefreshRepoStatus2() {
        System.out.println("refreshRepoStatus2");
        Assume.assumeTrue(this.urlConnectionAvailable); // If URL connection is not available, skip the test.
        LastVersionAvailable invalidInstance = new LastVersionAvailable("@notvalid");
        invalidInstance.refreshRepoStatus();
        boolean result = invalidInstance.isCorrectlyInitialized();
        assertFalse(result);
    }

    /**
     * Test of isCorrectlyInitialized method, of class LastVersionAvailable.
     */
    @Test
    public void testIsCorrectlyInitialized() {
        System.out.println("isCorrectlyInitialized");
        Assume.assumeTrue(this.urlConnectionAvailable); // If URL connection is not available, skip the test.
        boolean result = instance.isCorrectlyInitialized();
        assertTrue(result);
    }
}

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
package es.tid.keyserver.controllers.security.ip;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * WhiteList test class.
 * @author <a href="mailto:jgm1986@hotmail.com">Javier Gusano Martinez</a>
 * @since v0.3.0
 */
public class WhiteListTest {    
    @BeforeClass
    public static void setUpClass() {
        String testFileRoute = "target/test-classes/whitelisttest.lst";
        String iplist ="192.168.1.1\n192.168.1.2\n192.168.1.3\n";
        try{
            FileOutputStream newConfigFile = new FileOutputStream(testFileRoute);
            // Save parameters on file
            newConfigFile.write(iplist.getBytes());
            // Close config file.
            newConfigFile.close();
            System.out.println("[ INFO ] Test file created: " + testFileRoute);
        } catch (FileNotFoundException ex) {
            System.err.println("[ ERROR ] Test file not found exception.");
        } catch (IOException ex) {
            System.err.println("[ ERROR ] Test file IO exception.");
        }
    }
    
    @AfterClass
    public static void tearDownClass() {
        // Delete test file.
        String testFileRoute = "target/test-classes/whitelisttest.lst";
        File file = new File(testFileRoute);
        file.delete();
        System.out.println("[ INFO ] White lis test file deleted: " + testFileRoute);
    }

    /**
     * Test of iPAuthorized method, of class WhiteList.
     * @throws java.net.UnknownHostException
     * @since v0.3.0
     */
    @Test
    public void testIPAuthorized() throws UnknownHostException {
        System.out.println("iPAuthorized");
        InetAddress hostIp = InetAddress.getByName("192.168.1.2");
        WhiteList instance = new WhiteList("target/test-classes/whitelisttest.lst");
        boolean expResult = true;
        boolean result = instance.iPAuthorized(hostIp);
        assertEquals(expResult, result);
        hostIp = InetAddress.getByName("192.168.1.10");
        expResult = false;
        result = instance.iPAuthorized(hostIp);
        assertEquals(expResult, result);
    }
}

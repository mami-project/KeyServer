
package es.tid.keyserver.httpkeyserver.protocol;

import es.tid.keyserver.protocol.InputJSON;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This class contains the KeyServer API Interface Description for it use on KeyServer.
 * @author <a href="mailto:jgm1986@hotmail.com">Javier Gusano Martinez</a>
 */
public class InputJSONTest {
    private final String dataTest_1 = "{ \"method\":\"ECDHE\", \"spki\":\"405FD8A83BFB64683BAEB51D9F8D99C9D872FA63\", \"hash\":\"SHA-256\", \"input\":\" VW2zg9TGHpvr1wIuMlzX2VmiUlevpDggVD4MTxBy4iPLHzAZ7JlW7DNA/hJPZoSGtfWUgKqBcvI4RfMOEA3hwQMAF0EEJx+gdxklHMZL20TA6NAe+7MsF77txd9rp7E8LkRhOaBiNXYVwNqKFrhFOpfocmlMn4t/yCMukIo0k3wHyZg8gA==\" }";
    public InputJSONTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getMethod method, of class InputJSON.
     */
    @Test
    public void testGetMethod() {
        System.out.println("getMethod");
        InputJSON instance = new InputJSON(dataTest_1);
        String expResult = "ECDHE";
        String result = instance.getMethod();
        assertEquals(expResult, result);
    }

    /**
     * Test of getHash method, of class InputJSON.
     */
    @Test
    public void testGetHash() {
        System.out.println("getHash");
        InputJSON instance = new InputJSON(dataTest_1);
        String expResult = "SHA-256";
        String result = instance.getHash();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getSpki method, of class InputJSON.
     */
    @Test
    public void testGetSpki() {
        System.out.println("getSpki");
        InputJSON instance = new InputJSON(dataTest_1);
        String expResult = "405FD8A83BFB64683BAEB51D9F8D99C9D872FA63";
        String result = instance.getSpki();
        assertEquals(expResult, result);
    }

    /**
     * Test of getInput method, of class InputJSON.
     */
    @Test
    public void testGetInput() {
        System.out.println("getInput");
        InputJSON instance = new InputJSON(dataTest_1);
        String expResult = " VW2zg9TGHpvr1wIuMlzX2VmiUlevpDggVD4MTxBy4iPLHzAZ7JlW7DNA/hJPZoSGtfWUgKqBcvI4RfMOEA3hwQMAF0EEJx+gdxklHMZL20TA6NAe+7MsF77txd9rp7E8LkRhOaBiNXYVwNqKFrhFOpfocmlMn4t/yCMukIo0k3wHyZg8gA==";
        String result = instance.getInput();
        assertEquals(expResult, result);
    }
    
}

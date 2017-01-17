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
package es.tid.keyserver.https.keyprocess;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import org.junit.Test;
import static org.junit.Assert.*;
import org.slf4j.LoggerFactory;

/**
 * Unitary test class for RSA decryption.
 * @author <a href="mailto:jgm1986@hotmail.com">Javier Gusano Martinez</a>
 * @since v0.4.3
 */
public class RsaTest {
    /**
     * Private key value base64 encoded.
     */
    private final String privKey;

    /**
     * Encoded text message in base64.
     */
    private final String encMsg;

    /**
     * Decoded text message in base64.
     */
    private final String decMsg;
    
    /**
     * Logging object.
     */
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(RsaTest.class);
    
    /**
     * Test class constructor.
     * @see v0.4.3
     */
    public RsaTest() {
        privKey = "MIIJQgIBADANBgkqhkiG9w0BAQEFAASCCSwwggkoAgEAAoICAQCjLGz782zGpCux4hCRgQBg2FHi"
                + "PYLdmK79A8SINXWnUVSGoVM/fAFygtNILkT18Rv62WZUaK+cwN/s8F5o4jeODrNaVmlo3TG+mmF4"
                + "WI3tYLTJAT5UOS6oh9xKC04PCnHSEtz4okI5lCmLw6ngw0+wb/SaP+zT3RtPdSploEAaA/7G0gE0"
                + "lnNztLHrPxT6KrwwXwhb2bgMzzY8x1ZHNKjw7HlagOHi3qELopvhaGoBZAmCR2KIwO+9ll8ojJMM"
                + "2ymlMCjz2Nl7J6fMRB9LTb+Qpm96+w8XDfNbmgHYjzIHGt9X7lueadiVfIfGQiTi3Yt9QcydR4uk"
                + "Bm5XMlMU+rkGq3Q5UzyR/WJMOT9dDFzE6SOwqgpnb+bwPt67DCZUAGta3u24eR1VKID1FNmewF/p"
                + "GmDnPc9/XYz1V691LKR3nudPH9/IQoC3OB1zEqdKa48Gwwyr4S10G9fBl2ogdeqs0McU5CPp2T98"
                + "9DkfJCksnF3szaHqeluqbM29X5vLPEH/mFsvo3+rHmI7Q32nsBTy7UaS0xOrzoNDIrzO7ya+IVRu"
                + "fKoPeXdRlmR+IPyXhyRUvUeX8p/B5FEO7W8hJwwokT3UBT3SbGkFxiDFkqWDD0qdr2yiUOoPudMK"
                + "TI9HiJN58GGyCDHDU4BvRABBdYno9Jef1lHSaruCRINnJfvBdwIDAQABAoICADjKy5vdm6x/k120"
                + "SwP5nEIYyFcwBY5PCAiz+QFXaf20RvEc0Ta1WGOcuSDUSdg39P8YD4+taAHs94MmHhXvNlrRLwdw"
                + "L/v5kMjAPtB+Mde+4u1a63Cw/lZWAwwaZy7A+eCeBB7diqMzuEoW7dd24xOLw9y0Bx+uQ0UiiIEa"
                + "h3UGkvRsP1MoUkKhT5OLPyfbG+jSCry9SFvc+wmob5UU9P/1rj8bwIMP7Yl5LTwE9XBKeyzG37qt"
                + "PEQGRFR+qOe+l/4QhxBoFznQEYz84M963gPgiciJ/TSO/HaTYbUYEdd7OOUH5wd525Ib573PY7s6"
                + "VMRh1G+haS7LtjfPIi+ZHDBxvEyfRaG4y2z3+YFvsWL/NPUBoka+1gO4/7iZp6uzI1M3Hw6NhRgi"
                + "vOKRJDPIcIqdeyEA/RzQmhyUl3F13MeiZ6vafKVHSFLC4E384dmwO8NDqPFBbl6KlKZBYwrh4SnD"
                + "DZBZyoG0li7j6Ne3ukq+cg/YIlvSqEk2aPk61VRrhW+Lcw/IkqWQhf9VUMw3PSbtNsaiQjwA9qpe"
                + "iBzYSMOSYG65NYboJFzygyCoNslg6MoxarrQ4aGj0ZE+JPo81tizkcWmbMa9s+29QvZtJartVFnm"
                + "tNPZO0y4PBNNZzTnnm+LC6jRwa4tDZMhRYiF6gTqVY00nYk/lWV3ggcFi++xAoIBAQDTrLIOb14n"
                + "Ipknc9EXLoPOe/UAh8g3XFyvFUOsDVwg7BCqvqUq+2jmm4CMGz0ce+2wHAJf7m2SRJh3XQNd+pF7"
                + "HkdbCTjJMXbdp7KiyY27Q7L9wIUhd6yelBsjyjwAsyzaMrsMJBR/Weyg0fhKfPnX0tqmhEfzn1wf"
                + "R+CuOm3BszD2LePzz6BZHNbWbT8oYLItvrsNE0PWoWIqDdUz5NJldEAIzcVTDnUD4YaUMneRFlFQ"
                + "zhaDHnd84QLOoEQhNl8yyxoEBf3IfG6fHZRIi/S8q97L0mNOdF37uODX5PHKx/7ewVkP2K8zmOo7"
                + "F29EgcOIAmbgQDUzBuT9cY8bko/jAoIBAQDFV7gpXo21A/M6TisWqUwS3eqAPVNzvwEzUIH6Hvf5"
                + "568BUUmHa177eIf46+/gCwEdwKhOJ3xZjzCmL5W6qUxvy9VOz1Xa41n++ra/0lmzFXB2qnAfIiw3"
                + "tldlONDllRrc11ZfLwsqRFaAytcGXPUk9qcaraDal/09zvOF34WO6xJpUExL4vie8A2WylBVI8Om"
                + "nluzfwASybYs419GKNmVoI5qHbJk9AQz4I4PTG4fYGEAAPdlLLrGwOjQyEKG6V1rh25Ki5wMPz50"
                + "JwrzL8ylGIxCxEJl+UZo3VwwsTj0unQSSUel62giBGIwS5ZJ37BLoGIrtQZSL1bDfsUUDVRdAoIB"
                + "AQChawupmGHrZCX5AQgNLoqTFtIiImmbVhLT7VpaxSFhdcPMAXBBE9HOMf1YWplCtHrwlpt0/oSO"
                + "S0Mdm0+rBh6VDeUKVkD4pIDpzS2bmCo+CQVhHsQnXnTDxVvg8iLJwdlNV+xt+MGs0C10Akreuulo"
                + "HUJiQ3P/I6KDtDNxidsTIIwQ37P6xHWaHzTx9gN4zixyWAWalftgnFcYjM3uMewENB901z5H9Tlk"
                + "yUHwl3+4o5VFErP2L3CdSZXzbiImValJObrQIKWEkudk7ZCbtibfDg3rzreuJ21YPYcw/EUrB1Kk"
                + "GjvJqssIhO7yOw+95VrvyC0di7lF/Vb4svWX6C9TAoIBAFotp55f8umREIjIR7BnVLgaaJqJa6Us"
                + "12OXmo2tsSulz7cOakdTrWfEwH3jcw4yeSQbcc2lBZH3eQe4HvfZfXBo+OS2okkQM4PtFa26dfr8"
                + "BRR09h+EAijDTlSXDf6bkFbyoCmG2ddKuyzHhFMID44AmIP8a9D4O2UPOQEoD2Qrz5IIe5UB9xLK"
                + "J6uMcAE4AqQVIVp44pOzUWhZzqPE7wkNAYdEYsz3JSR/rOsYxgnYhPr3dhXpxsAkTz95A82avnHw"
                + "xVmTSo/eJq/1V2+hnCalfRLVlHH840vwO7yUf651tazVh8QuOruwvR3MrLS9X1Zvx+ypIGZV85QK"
                + "TgzHupECggEAZ42TQ2lnTjJRJnr63pZ1U/9mmvEWfdF+4Q7kyhHJ37VtyOk1TAF+RJXFBwWcoQqu"
                + "I6NR4Rew+iBj5sggZaQWxfpdyHzUIuhUN06nHyYKbecgPQLAN5C2nYUH7QT+U/nFv2LBToAK2tfj"
                + "Z7hreItojJb/dH00VQWzDK/TgNMSUwkEqSf2XmHjblQn2K8bR9y5JlpAd11SuU/Kcc/Q/qYI7zzp"
                + "EK7T5rRRltWFFFGwLfjMoYaA6nnubJkEFyUD1N05hLjc/jFgvOp+xE6+UKjBeH4f/18L0bcyNWfh"
                + "KG+45wyAAkDXq6pkdFq1b1gBhOuVI4/C/mGHAJS4GOVtdPww3A==";
        decMsg = "VGVzdCBtZXNzYWdlLgo=";
        encMsg = "Ckrr6CasS+L7DLShSkcn3uK81MM/coFWE/YK70WkWINlWMWGPOTAZSQ6nRc/1hKsLWyg8t9q9pbN"
                + "tZigwqGjRRhvYqTgjXhIhdEXyEs3P+AQigNedCbmA2no0OjvhqKnAxuhTJEjJK7Uf0jUCT+RPG5e"
                + "aMMmAT+x/q+XH3W8/cAoHTB8CRMKbCWAmyQd6sf9JErB1st/xKUaPUYuEp0XYQF476psDvCn3lrs"
                + "R4aqOpszEDF9n5jaQA+81L6KtIiBPg4HrpYlFYpUjYu5SsKEyudoZw1TnJX2CokTOgL9v52ztKz2"
                + "dEDpilEE0kRVY3RN0EbQotM++9JT8e+cXjDIPTvE3DX4IbEMsNZTFfgmUOQ60W7schu5JSs/016Z"
                + "a8Lfeq9FQ3jVOdI49MfaYmVlUjM/i4CIRI4IxgJYbRWk60QeySq7vCfiOLRpCWLB6QJ9fKtPql5w"
                + "Q4RrSu0Jri9aWUT09kOnmNupA7x3+rvgQUyFUh1+/XTzdx65tEx3nnlSNkozTjZE6b5ZToSTbEMB"
                + "BKnGcu2KRgw3Z0lmnWrqBlFm2/C8rmpD0JmKEboAkaiIh0VKaoARhBpr8shuDXvS0eWi97fZj3nu"
                + "CwDvPG4btz1PqF/pBkIzI4ZMVtATUqIC42oyE3fFu/yTvOPFC6QZzSGWq6Wl1uCXc32XzOjsk9E=";
    }

    /**
     * Test of calcDecodedOutput method, of class Rsa.
     * @throws java.lang.Exception Generate an exception if something goes wrong.
     */
    @Test
    public void testCalcDecodedOutput() throws Exception {
        System.out.println("calcDecodedOutput");
        PrivateKey key = getPrivKey(this.privKey);
        String result = Rsa.calcDecodedOutput(encMsg, key);
        assertEquals(decMsg, result);
    }

    /**
     * Get test private key.
     * @param encPk Base64 encoded private key.
     * @return Private key object.
     * @since v0.4.3
     */
    public static PrivateKey getPrivKey(String encPk) {
        // Generate Private Key object.
        PrivateKey pKey = null;
        try {
            // Decode private key.
            byte[] encodePrivateKey = Base64.getDecoder().decode(encPk.trim());
            // Load private key object.
            java.security.KeyFactory keyFactory = java.security.KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec privatekeySpec = new PKCS8EncodedKeySpec(encodePrivateKey);
            pKey = keyFactory.generatePrivate(privatekeySpec);
        } catch (NoSuchAlgorithmException ex) {
            // Error level.
            LOGGER.error("RSA Invalid Algorithm exception: {}", ex.getMessage());
            // Debug level.
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            LOGGER.debug(errors.toString());
        } catch (InvalidKeySpecException ex) {
            // Error level.
            LOGGER.error("RSA Invalid Key exception: {}",ex.getMessage());
            // Debug level.
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            LOGGER.debug(errors.toString());
        }
        return pKey;
    }
}

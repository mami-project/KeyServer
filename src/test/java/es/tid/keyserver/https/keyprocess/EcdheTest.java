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

import es.tid.keyserver.https.protocol.InputJSON;
import java.security.PrivateKey;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.slf4j.LoggerFactory;

/**
 * Unitary test class for ECDH.
 * @author <a href="mailto:jgm1986@hotmail.com">Javier Gusano Martinez</a>
 * @since v0.4.3
 */
public class EcdheTest {
    /**
     * Private key value base64 encoded.
     */
    private final String privKey;
    
    /**
     * Logging object.
     */
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(EcdheTest.class);
    
    public EcdheTest() {
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
    }
    
    /**
     * Test of calcOutput method, of class Ecdhe.
     * @throws java.lang.Exception If something goes wrong during the test.
     */
    @Test
    public void testCalcOutput1() throws Exception {
        System.out.println("calcOutput1");
        String incomingData = "BJmr+S9gFYd/MbcwRb2UnmOIw+JluM6Hr/nuxXhubAu8YXMgIsD3OuG/xW92g/YmBvzs6IdpFNIDdm8AZoHaJgMAF0EEpIIa2HyIZpSlcd3OVi7Lt8YZ8bVTQQxuQU+icNwgjKinxOoMvCsLMsfILRE05b4r2uCW+xY6ks0UNXY3gu99tQ==";
        PrivateKey key = RsaTest.getPrivKey(privKey);
        String hash = InputJSON.SHA1;
        String expResult = "QEFzMI7E25ikSPV5p/Ps2xbh2yLIRDti4ubCzNPRECTprgfQ2P2YshYlrCRomjZBp7qPVTBkGJ5mW00yz96znAvn3qchIuOpzpc9wPqf3sCoYFGYlsVxIODccF7Vc3FkOw8zannM2DWOhvCuvF0Bv6C1AhaJzlKyX298ahym/lCGTSvLkvtJo1g8epENEtAqNPpCY8M15sr+ZO3HjQBlPP4GLYNCrFAL/4Cc1nxb6zFLJTZ3Iu5RkZWPcuj9gQ8MbjXzOFPP0x+uVZ37KY+cGDu6hQapjwcf+QaAaKUeOTsK27pDVwbjeGL+0VVleynZN99Mpu/U/OOdmKUKx358Ma0GYUouS0BuV8I425GYPT/lvveKnJ+S9UUsXbPzYGDHuItNgGryUxB6IsAfnu83gTDwYfFUF8H4E/BMcnZyJimpiCEttXdkvUcZenG7YrVyYS/tCGBwBzdE3EMEE4cXw6qNznzz3qbHvYXVAu9kgeV0zMazGiTT/ZgcxmOY5quXcoUHeFi6z/LGZh4IYg8954xEaRf0IE541YOvE/tw5bgUbVcD2LsnT+z2AP3COL43p5qdW/vmHLN/I2FVPmn6I1XAPXnRNa+OaBgMmYhp+OrSPGbAarX59FOKKzd00Uww4z8z0M+1nvubPP8ldv0DcABnyid4ZF9GJhLcgWOa4Gs=";
        String result = Ecdhe.calcOutput(incomingData, key, hash);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of calcOutput method, of class Ecdhe.
     * @throws java.lang.Exception If something goes wrong during the test.
     */
    @Test
    public void testCalcOutput2() throws Exception {
        System.out.println("calcOutput2");
        String incomingData = "BJmr+S9gFYd/MbcwRb2UnmOIw+JluM6Hr/nuxXhubAu8YXMgIsD3OuG/xW92g/YmBvzs6IdpFNIDdm8AZoHaJgMAF0EEpIIa2HyIZpSlcd3OVi7Lt8YZ8bVTQQxuQU+icNwgjKinxOoMvCsLMsfILRE05b4r2uCW+xY6ks0UNXY3gu99tQ==";
        PrivateKey key = RsaTest.getPrivKey(privKey);
        String hash = InputJSON.SHA_224;
        String expResult = "IKsRVRRZdEYbotgq0rBb2VNzOd5rd80yySTZesis/B7uVaxXZFgazMQ6Xl2dK4dq+ZLsA1kFHvmJw7667uIh1xj5S6MjP5sLoceHuYPrOYFuQOEpvKee40jGkMPHo7lrso9lj3KAY6SGon3n1rH2J5pLNrmZPpISVJErSefiCnogAa6ZY+G77v6pxrZ0AZFwmNeB/tWrg4gJJQ3QdnLfS62FuaKRnamFDTP8QVukcifOANH9y8OXW7WYcKu/r8+A31LDCCFy2FEgqormEWFmd+3FxwmFvZbUYDLt01nqbYaoxko9kUmR2WoFw4x6uTwSoDAqGokHWaExNFydnE5CJXKhW9nVC3NladnoP6CRlXQ3Gd+XrQ7cWFT0mGctPP3VTeDrSH666C18aweEf1WhNl044eBiPShqGc3Mci9j3epQKEkonU/Ec8AJVCPm2O7bp5jhzLkUkIK7lpAv4BOtT86d9geqFRa2kBk6Y0yPiGP7A02gZdJ119D3gFOlAjRBDYqxLkOVMJu31WQDG+0KfSRI+4qhCHsyhCTc67eu2MpwxMwrNuNofsHle541k0GiHx+7WGqACpK8kp4TARjd6Opm/oUCY9uixBR8q02LHB3PqAQ2qeiO46IMrdhcJdO3pgr0D1MRNz6Pv4fP1xhv4sKy9z4T5R4tmAAeUpz3XYc=";
        String result = Ecdhe.calcOutput(incomingData, key, hash);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of calcOutput method, of class Ecdhe.
     * @throws java.lang.Exception If something goes wrong during the test.
     */
    @Test
    public void testCalcOutput3() throws Exception {
        System.out.println("calcOutput3");
        String incomingData = "BJmr+S9gFYd/MbcwRb2UnmOIw+JluM6Hr/nuxXhubAu8YXMgIsD3OuG/xW92g/YmBvzs6IdpFNIDdm8AZoHaJgMAF0EEpIIa2HyIZpSlcd3OVi7Lt8YZ8bVTQQxuQU+icNwgjKinxOoMvCsLMsfILRE05b4r2uCW+xY6ks0UNXY3gu99tQ==";
        PrivateKey key = RsaTest.getPrivKey(privKey);
        String hash = InputJSON.SHA_256;
        String expResult = "Foo1VIDzTz7Q85nDJ6NE2/cb8AhcTJPE1XR4UYgr8CKgGTPwZeeLYdrqw7Gmd8Uejcl2oudS2xZYKBP9nEtuCjlGR9VALCtfLV9DeBEh0OI1oT1OqofyX0ul4s/l/Z0hkID7DRfa6bZmcGmGtVLNwk2VPj+19YQJqtxh9zn82m0NyoEmj+DL6+cICIBHUz/UEID4D/nUm0GY+Pj02/LE70194UonW8ln8IW97PRe4NUOMEqM8hmc/gZ/eFev/uMi/gkF4qznDF514utDZ4+Y0+dFLJsHrRBv5ujzNonR8odTgk+BIbASHhyPhqK7ZwM0tS4aivPj5ZQSjGgRCS9lvZCS6OCVYgJAjXKmRyz0Id74cDx4xwKHO4djBsp4X0sy97GcWCctkcc0dOqtzUNTE7zEzSJSBmKZ94OInPLtWpYLCx3Tc3IQjjwSD8zVSLKnDYMN98f0EV28pGVIy2EHc02wJSKd/bJjfNbFksZjYoX3invcAKe8zNow2y7rfWf6KmClvQS9Q0ZEtxvcVvUID24SFTox6Y4pcUtKebZ8alfi85dDHnuVleLKqmYBd6ZYZ8eJyuabUjagY98+wwjIqnMOE2jaS8S2+E7nbo8Aut2yJxz6NeAe+KrPepcTtOB4dceYFAmE5K77T5pL58wxI+m9aku9vuIJrxnGWX6ijsc=";
        String result = Ecdhe.calcOutput(incomingData, key, hash);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of calcOutput method, of class Ecdhe.
     * @throws java.lang.Exception If something goes wrong during the test.
     */
    @Test
    public void testCalcOutput4() throws Exception {
        System.out.println("calcOutput4");
        String incomingData = "BJmr+S9gFYd/MbcwRb2UnmOIw+JluM6Hr/nuxXhubAu8YXMgIsD3OuG/xW92g/YmBvzs6IdpFNIDdm8AZoHaJgMAF0EEpIIa2HyIZpSlcd3OVi7Lt8YZ8bVTQQxuQU+icNwgjKinxOoMvCsLMsfILRE05b4r2uCW+xY6ks0UNXY3gu99tQ==";
        PrivateKey key = RsaTest.getPrivKey(privKey);
        String hash = InputJSON.SHA_384;
        String expResult = "kuJc1zBJ4WESopIsI5woxk5goEuvk2r8QhG8kb+05ZUFMIlz+gybI6yuAAT0LaaUnzU2lxmw2Z/EGt0CbtGoeLlLwo2J/KjweGblkY7vVlQykMX0u936Ei5o9GF2/Asc0WabuiV1P1/9hWu1iDhcwpOqTgB56M3oYZoVnQXr8LvxTAXnAkzKcicuhN+Tkd/nVT4A/ns3GZ6/Dhw+bV1bMfdjq9uvaB8NYm/UPsrFWEx51KbX8dlPjGVjcMrQJ+hdD1/XPL22lZwhiv1GhY8+VbrhuOhFVUNIG+7u5Jn+yI0VATR6gArk6DSRoykj/A4tYC5IWf2DtjH1at+bq1ZZMaGp0uYLDP45/FIQvPrk/Bb4TagSAOFPlNk+8gftRpTfcVAzGA1lDq/AZBNv21URlksAPPfDEgRCha3cnFhhMs6znLm/8/s9i6wIh+6uOwHCCCFwcWAdTMypa1gx5DssfXG0Cc9xJahRQSfzbBLw/PoAcYbyBy9E79RuBiLoF8Zar7sTC/G072FU3KdzJoyGHvsBQ0BSS7hd9QL5WQpPG/rpsL9vpSxSotve6S8prVQ7PNMSPeReR+OyjxEMEWJSereGgY3yGJ+1z4nSufblVfVo4ynCVo/0XZ+Gtp63gEsSvbOjE8lRr55GlFnyR5pFCLv5+Vk51kdvB74sHxOjkgw=";
        String result = Ecdhe.calcOutput(incomingData, key, hash);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of calcOutput method, of class Ecdhe.
     * @throws java.lang.Exception If something goes wrong during the test.
     */
    @Test
    public void testCalcOutput5() throws Exception {
        System.out.println("calcOutput5");
        String incomingData = "BJmr+S9gFYd/MbcwRb2UnmOIw+JluM6Hr/nuxXhubAu8YXMgIsD3OuG/xW92g/YmBvzs6IdpFNIDdm8AZoHaJgMAF0EEpIIa2HyIZpSlcd3OVi7Lt8YZ8bVTQQxuQU+icNwgjKinxOoMvCsLMsfILRE05b4r2uCW+xY6ks0UNXY3gu99tQ==";
        PrivateKey key = RsaTest.getPrivKey(privKey);
        String hash = InputJSON.SHA_512;
        String expResult = "TBlHDTDl68oYFXiQYjTmVUTGeBr0KUr2ME+BVZk3j0b74/Ln0ey7xthD2akcnEHGGr8t+hNzwYVZLVwYQEqe9jfLs2D9Yi6UQLAg61fGDempl90Cj2JN04/4YdCJeqINEfhMOstbem+8MsdICKOsL81o3KgtpUpfX/6Jinvf7q/JAYbMLwLlxBbCw52+k3rDy/xmEPuTXMGOztbe3QQNtaFrgl4UA9K4cPhbneC0rHAgCg3ASAm6cLYgSUuRqyfYAJXFvHG2JDcIxgUB5mlX+XMlWsrNUG8nF4kcOzZcGXtl7JPpitEJE89gRdjwJX346gUPoo7ZH6sxsSOqU+OAS1SlzbJg4uXPtFXoQBsWKkebwuCDlNCbNu1bSHZXyPs4qP+cGfhOauYbvpFve37TzbnNE1t3RznRQr+pHMcuit3Ynp29awek+Og1MZwkCFqtbgeuRk6v7lsbAvNtSXgZRcYDjHfdkG5/iASCbbqrN6iRB6n+RbfAiNKU/MS22CI8Q9Rqptd4LiYFymAjxI55jWrOv2Y4aXBITLW4X0O8OieTGXsT5We5x70hcwky+eOtn44NsrqTvsjDZObDDFvczkKmjFyc1SbxMS03gaYJbuNZYzX5ORS5VOIg5MoGefgXMkMPqz7ia+cryXtZK3NydglL5rQvFhXHrWkbiKW9i1A=";
        String result = Ecdhe.calcOutput(incomingData, key, hash);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of calcOutput method, of class Ecdhe.
     * @throws java.lang.Exception If something goes wrong during the test.
     */
    @Test
    public void testCalcOutput6() throws Exception {
        System.out.println("calcOutput6");
        String incomingData = "BJmr+S9gFYd/MbcwRb2UnmOIw+JluM6Hr/nuxXhubAu8YXMgIsD3OuG/xW92g/YmBvzs6IdpFNIDdm8AZoHaJgMAF0EEpIIa2HyIZpSlcd3OVi7Lt8YZ8bVTQQxuQU+icNwgjKinxOoMvCsLMsfILRE05b4r2uCW+xY6ks0UNXY3gu99tQ==";
        PrivateKey key = RsaTest.getPrivKey(privKey);
        String hash = null;
        String expResult = "BJmr+S9gFYd/MbcwRb2UnmOIw+JluM6Hr/nuxXhubAu8YXMgIsD3OuG/xW92g/YmBvzs6IdpFNIDdm8AZoHaJgMAF0EEpIIa2HyIZpSlcd3OVi7Lt8YZ8bVTQQxuQU+icNwgjKinxOoMvCsLMsfILRE05b4r2uCW+xY6ks0UNXY3gu99tQ==";
        String result = Ecdhe.calcOutput(incomingData, key, hash);
        assertNull(expResult, result);
    }
    
    /**
     * Test of calcOutput method, of class Ecdhe.
     * @throws java.lang.Exception If something goes wrong during the test.
     */
    @Test
    public void testCalcOutput7() throws Exception {
        System.out.println("calcOutput7");
        String incomingData = "BJmr+S9gFYd/MbcwRb2UnmOIw+JluM6Hr/nuxXhubAu8YXMgIsD3OuG/xW92g/YmBvzs6IdpFNIDdm8AZoHaJgMAF0EEpIIa2HyIZpSlcd3OVi7Lt8YZ8bVTQQxuQU+icNwgjKinxOoMvCsLMsfILRE05b4r2uCW+xY6ks0UNXY3gu99tQ==";
        PrivateKey key = RsaTest.getPrivKey(privKey);
        String hash = "NOT_VALID";
        String result = Ecdhe.calcOutput(incomingData, key, hash);
        System.out.println("[ JAVI ] " + result);
        assertNull(result);
    }
}

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
package es.tid.keyserver.https.keyprocess;

import es.tid.keyserver.https.protocol.InputJSON;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import org.slf4j.LoggerFactory;

/**
 * Class for sign ECDHE (ECDH) Key Exchange.
 * @author <a href="mailto:jgm1986@hotmail.com">Javier Gusano Martinez</a>
 */
public class Ecdhe {
    /**
     * Logging object.
     */
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(Ecdhe.class);
    /**
     * This method provide a Sign to an incoming string data.
     * @param incomingData Data to sign codified as base64.
     * @param key PrivateKey object with the private key used to sign incoming data.
     * @param hash Hash type (SHA1, SHA224, SHA256, SHA512).
     * @return Signed data or null if hash incoming parameter is not defined.
     * @throws NoSuchAlgorithmException The specified algorithm is not valid.
     * @throws InvalidKeySpecException Key specification not valid
     * @throws java.security.InvalidKeyException Invalid Key.
     * @throws java.security.SignatureException Not valid signature name.
     */
    static public String calcOutput(String incomingData, PrivateKey key, String hash) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException{
        // Decode incoming from base64 (base64 decoder).
        byte[] data = Base64.getDecoder().decode(incomingData.trim());
        // Select signature object and create a new with correct hash
        logger.debug("Key used to sign: {} | Hash: {} | Data Bytes to sign: {}", key.getAlgorithm(), hash, incomingData);
        Signature dsa = getSignature(hash);
        // Set the private key used to sing data
        dsa.initSign(key);
        // Calculate MD5 if hash is SHA1
        if(hash.equalsIgnoreCase(InputJSON.SHA1)){
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(data);
            data = messageDigest.digest();
            logger.debug("ECDH SHA1: Calculated MD5 for base64 decoded data.");
        }
        // Set the data to sign.
        dsa.update(data);
        // Sign and return data
        byte signedData[] = dsa.sign();
        // Check if something goes wrong when decrypts.
        if (signedData == null){
            return null;
        }
        // Encode signed data using base64.
        return Base64.getEncoder().encodeToString(signedData);
    }

    /**
     * This method receive an string with the incoming hash type and returns a
     * signature object.
     * @param hash String with the HASH tag.
     * @see <a href="http://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#Signature">Java Standard Names: Signature Algorithms</a>
     * @return Signature object or null if the specified signature algorithm is not specified.
     * @throws NoSuchAlgorithmException 
     */
    private static Signature getSignature(String hash) throws NoSuchAlgorithmException {
        // If JSON "hash" field is not present.
        if(hash==null){
           return Signature.getInstance("SHA1withRSA");
        }
        // If JSON "hash" field is present.
        Signature dsa = null;
        switch(hash){
            case InputJSON.SHA1:
                dsa = Signature.getInstance("SHA1withRSA");
                break;
            case InputJSON.SHA_224:
                dsa = Signature.getInstance("SHA224withRSA");
                break;
            case InputJSON.SHA_256:
                dsa = Signature.getInstance("SHA256withRSA");
                break;
            case InputJSON.SHA_384:
                dsa = Signature.getInstance("SHA384withRSA"); 
                break;
            case InputJSON.SHA_512:
                dsa = Signature.getInstance("SHA512withRSA");
                break;    
            default:
                logger.error("ECDHE: not valid Hash value ({})", hash);
        } 
        return dsa;
    }
    
}

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

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Class for RSA PreMaster secret decrypts.
 * @author <a href="mailto:jgm1986@hotmail.com">Javier Gusano Martinez</a>
 */
public class Rsa {
    /**
     * This static method provides a simple way to decode the PremasterSecret from
     * an input string using a PrivateKey object.
     * @param data String with base64 encoded PremasterSecret.
     * @param key Private key used for extract the PremasterSecret.
     * @return String with the PremasterSecret decoded. This output is base64 encoded.
     * @throws NoSuchAlgorithmException Algorithm not valid.
     * @throws NoSuchPaddingException Problem with padding.
     * @throws InvalidKeyException Key not valid.
     * @throws IllegalBlockSizeException Block size not valid.
     * @throws BadPaddingException Bad padding.
     */
    static public String calcDecodedOutput(String data, PrivateKey key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
        // Decode incoming "data" from base64 (base64 decoder).
        byte[] codifiedData = Base64.getDecoder().decode(data.trim());
        // Decoding incoming data using PrivateKey and gets the PremasterSecret decoded.
        byte[] decodifiedData = decrypt(codifiedData, key);
        // Check if something goes wrong when decrypts.
        if (decodifiedData == null){
            return null;
        }
        // Encode the PremasterSecret using base64.
        return Base64.getEncoder().encodeToString(decodifiedData);
    }
    
    /**
     * This method is used to decrypts using RSA an array of bytes using the private key object.
     * @param text Array of bytes with RSA encoded data.
     * @param key Private key used to get the decoded data.
     * @return Decoded data as array of bytes.
     */
    private static byte[] decrypt(byte[] text, PrivateKey key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        byte[] dectyptedText;
        // Get RSA cipher object
        Cipher cipher = Cipher.getInstance("RSA");
        // Decrypts text using the private key
        cipher.init(Cipher.DECRYPT_MODE, key);
        dectyptedText = cipher.doFinal(text);
        return dectyptedText;
    }
}

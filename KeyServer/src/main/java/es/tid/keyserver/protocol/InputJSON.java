/**
 * Copyright 2015 Javier Gusano Martínez.
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
package es.tid.keyserver.protocol;

import java.util.Base64;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.LoggerFactory;

/**
 * InputJSON Secure Key Server API for input data from Proxy server.
 * @author <a href="mailto:jgm1986@hotmail.com">Javier Gusano Martinez</a>
 */
public class InputJSON {
    /**
     * Logging object.
     */
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(InputJSON.class);
    /**
     * Contains the string for sign JSON incoming SIGN method.
     */
    public final static String ECDHE = "ECDHE";
    /**
     * Contains the string for sign JSON incoming DECRYPT method.
     */
    public final static String RSA = "RSA";
    
    /**
     * Definition of HASH field right values.
     */
    public final static String SHA1 = "SHA1";
    public final static String SHA_224 = "SHA-224";
    public final static String SHA_256 = "SHA-256";
    public final static String SHA_384 = "SHA-384";
    public final static String SHA_512 = "SHA-512";
    
    /**
     * Contains the JSON object for the input JSON String.
     */
    private JSONObject inputData;
    
    /**
     * Constructor using a String with the JSON definition.
     * @param stringData Contains the JSON as String.
     */
    public InputJSON(String stringData){
        inputData = new JSONObject();  
        try {
            JSONParser parser = new JSONParser();
            inputData = (JSONObject) parser.parse(stringData);
        } catch (ParseException ex) {
            logger.debug("Not valid input JSON: {}.", ex.toString());
            //Logger.getLogger(InputJSON.class.getName()).log(Level.SEVERE, null, ex);
        }    
    }
    
    /**
     * Get "method" field from an input JSON message.
     * @return Method string ECDHE or RSA. Null is returned if is not defined.
     */
    public String getMethod(){
        String readedMethod;
        switch ((String) inputData.get("method")){
            case InputJSON.ECDHE:
                readedMethod = InputJSON.ECDHE;
                break;
            case InputJSON.RSA:
                readedMethod = InputJSON.RSA;
                break;
            default:
                readedMethod = null;
        }
        return readedMethod;
    }
    
    /**
     * Get "spki" field from an input JSON message HEX encoded.
     * @return Spki field value with HEX encoding.
     */
    public String getSpki(){
        return (String) inputData.get("spki");
    }
    
    /**
     * Get "hash" field from the input JSON message. This field is only present
     * if the JSON method is ECDHE. If you call this method using "RSA", you'll
     * receive "null" ignoring JSON hash field if is present.
     * If an incoming ECDHE JSON doesn't contains the "hash" field, this method
     * use SHA1 as default.
     * @return String value with the specific names. If the read field is not
     *          valid, this method returns null value.
     */
    public String getHash(){
        String readedHash = null;
        if(this.getMethod().equalsIgnoreCase(ECDHE)){
            if(inputData.containsKey("hash")){
                switch ((String) inputData.get("hash")){
                    case InputJSON.SHA1:
                        readedHash = InputJSON.SHA1;
                        break;
                    case InputJSON.SHA_224:
                        readedHash = InputJSON.SHA_224;
                        break;
                    case InputJSON.SHA_256:
                        readedHash = InputJSON.SHA_256;
                        break;
                    case InputJSON.SHA_384:
                        readedHash = InputJSON.SHA_384;
                        break;
                    case InputJSON.SHA_512:
                        readedHash = InputJSON.SHA_512;
                        break;
                    default:
                        logger.error("ECDHE not valid 'hash' field {}.", (String) inputData.get("hash"));
                        readedHash = null;
                }
            } else {
                readedHash = SHA1;
            }
        }
        return readedHash;
    }
    
    /**
     * Get the "data" field from an input JSON message base64 encoded.
     * @return String with the data content.
     */
    public String getInput(){
        return (String) inputData.get("input");
    }
    
    /**
     * This method is used to verify the integrity of the JSON
     * @return Null if all fields are correct, Otherwise this method returns the error name.
     */
    public String checkValidJSON(){
        // Extracting JSON fields
        String method = this.getMethod();
        String hash = this.getHash();
        String spki = this.getSpki();
        String input = this.getInput();
        // Loger trace output
        logger.trace("Method CheckJSON (fields): method='{}', hash='{}', spki='{}', input='{}'",
                method, hash, spki, input);
        // Check if the JSON contains all fields "method", "hash", "spki"* and 
        //  "input" fields.
        //  Hash only is defined if the JSON contains ECDHE.
        if((method == null) || (spki == null) || (input == null)){
            logger.debug("Input JSON: Some required fields are not present.");
            return ProtocolJSON.ERR_MALFORMED_REQUEST;
        } else {
            logger.debug("Input JSON: 'Mehtod', 'spky' and 'input' fields are present.");
        }
        // Parsing input field from base64 to array of bytes
        logger.debug("JSON Input data to decode from base64: {}", input);
        byte inputDataB[] = Base64.getDecoder().decode(input.trim());
        logger.trace("JSON Input Field number of Bytes: {}", inputDataB);
        // Check the length of input
        switch(method){
            case InputJSON.RSA:
                if(inputDataB.length<10){ // If it has some bytes (less than 10 for example)
                    logger.debug("Input JSON: RSA length too short ({} bytes).", inputDataB.length);
                    return ProtocolJSON.ERR_MALFORMED_REQUEST;
                }
                break;
            case InputJSON.ECDHE:
                // Check if HASH field is present (not null) and valid.
                if (hash == null){
                    logger.debug("Input JSON: Hash field not valid ('{}').", hash);
                    return ProtocolJSON.ERR_MALFORMED_REQUEST;
                }
                // Check JSON infput lenght.
                if(inputDataB.length!=133){ // 32+32+69 = 133 bytes 
                    logger.debug("Input JSON: ECDH length not valid ({} bytes).", inputDataB.length);
                    return ProtocolJSON.ERR_MALFORMED_REQUEST;
                }
                break;
            default:
                logger.debug("Input JSON: Not valid 'method' field for this input JSON ('{}').", method);
                return ProtocolJSON.ERR_MALFORMED_REQUEST;
        }
        return null;
    }
}
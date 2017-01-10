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
package es.tid.keyserver.https.protocol;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.LoggerFactory;

import java.util.Base64;

/**
 * InputJSON Secure Key Server API for input data from Proxy server.
 * @author <a href="mailto:jgm1986@hotmail.com">Javier Gusano Martinez</a>
 */
public class InputJSON {
    /**
     * Logging object.
     */
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(InputJSON.class);

    /**
     * Contains the string for sign JSON incoming SIGN method.
     */
    public final static String ECDHE = "ECDHE";

    /**
     * Contains the string for sign JSON incoming DECRYPT method.
     */
    public final static String RSA = "RSA";
    
    /**
     * TLS 1.0 - Definition of PROTOCOL field values.
     */
    public final static String TLS_1_0 = "TLS 1.0";

    /**
     * TLS 1.1 - Definition of PROTOCOL field values.
     */
    public final static String TLS_1_1 = "TLS 1.1";

    /**
     * TLS 1.2 - Definition of PROTOCOL field values.
     */
    public final static String TLS_1_2 = "TLS 1.2";

    /**
     * DTLS 1.0 - Definition of PROTOCOL field values.
     */
    public final static String DTLS_1_0 = "DTLS 1.0";

    /**
     * DTLS 1.2 - Definition of PROTOCOL field values.
     */
    public final static String DTLS_1_2 = "DTLS 1.2";
    
    /**
     * SHA1 - Definition of HASH field right values.
     */
    public final static String SHA1 = "SHA1";

    /**
     * SHA-224 - Definition of HASH field right values.
     */
    public final static String SHA_224 = "SHA-224";

    /**
     * SHA-256 - Definition of HASH field right values.
     */
    public final static String SHA_256 = "SHA-256";

    /**
     * SHA-384 - Definition of HASH field right values.
     */
    public final static String SHA_384 = "SHA-384";

    /**
     * SHA-512 - Definition of HASH field right values.
     */
    public final static String SHA_512 = "SHA-512";
    
    /**
     * Contains the JSON object for the input JSON String.
     */
    private JSONObject inputData;
    
    /**
     * Protocol JSON field value.
     */
    private String protocol = null;
    
    /**
     * SPKI JSON field value.
     */
    private String spki = null;
    
    /**
     * Method JSON field value.
     */
    private String method = null;
    
    /**
     * Hash JSON field value.
     */
    private String hash = null;
    
    /**
     * Input JSON field value.
     */
    private String input = null;
    
    /**
     * Constructor using a String with the JSON definition.
     * @param stringData Contains the JSON as String.
     */
    public InputJSON(String stringData){
        inputData = new JSONObject();  
        try {
            JSONParser parser = new JSONParser();
            inputData = (JSONObject) parser.parse(stringData);
            readFields();
        } catch (ParseException ex) {
            LOGGER.debug("Not valid input JSON: {}.", ex.toString());
        }
    }
    
    /**
     * Get "method" field from an input JSON message.
     * @return Method string ECDHE or RSA. Null is returned if is not defined.
     */
    public String getMethod(){
        return this.method;
    }
    
    /**
     * Get "spki" field from an input JSON message HEX encoded.
     * @return Spki field value with HEX encoding. Returns null if the field is
     *     not present.
     */
    public String getSpki(){
        return this.spki;
    }
    
    /**
     * Get "hash" field from the input JSON message. This field is only present
     *     if the JSON method is ECDHE. If you call this method using "RSA", you'll
     *     receive "null" ignoring JSON hash field if is present.
     *     If an incoming ECDHE JSON doesn't contains the "hash" field, this method
     *     use SHA1 as default.
     * @return String value with the specific names. If the read field is not
     *     valid, this method returns null value.
     */
    public String getHash(){
        return this.hash;
    }
    
    /**
     * Get "protocol" field from the input JSON message.
     *     This field is required in SKI request. Specifies the protocol version 
     *     negotiated in the handshake between Client and Edge Server.
     * @return String value with the specific protocol. If the read field is not
     *     valid, this method returns 'null' value.
     */
    public String getProtocol(){
        return this.protocol;
    }
    
    /**
     * Get the "data" field from an input JSON message base64 encoded.
     * @return String with the data content.
     */
    public String getInput(){
        return this.input;
    }
    
    /**
     * This method is used to verify the integrity of the JSON
     * @return Null if all fields are correct, Otherwise this method returns the
     *     error name.
     */
    public String checkValidJSON(){
        // Logger trace output
        LOGGER.trace("Method CheckJSON (fields): protocol='{}', method='{}', hash='{}', spki='{}', input='{}'",
                protocol, method, hash, spki, input);
        // Check if the JSON contains all fields "protocol", "method", "hash", "spki" and "input" fields.
        if((protocol == null) || (method == null) || (spki == null) || (input == null)){
            LOGGER.debug("Input JSON: Some required fields are not present.");
            return ErrorJSON.ERR_MALFORMED_REQUEST;
        } else {
            LOGGER.debug("Input JSON: 'Mehtod', 'spky' and 'input' fields are present.");
        }
        // Parsing input field from base64 to array of bytes
        LOGGER.debug("JSON Input data to decode from base64: {}", input);
        byte inputDataB[] = Base64.getDecoder().decode(input.trim());
        LOGGER.trace("JSON Input Field number of Bytes: {}", inputDataB);
        // Check the length of input
        switch(method){
            case InputJSON.RSA:
                if(inputDataB.length<10){ // If it has some bytes (less than 10 for example)
                    LOGGER.debug("Input JSON: RSA length too short ({} bytes).", inputDataB.length);
                    return ErrorJSON.ERR_MALFORMED_REQUEST;
                }
                break;
            case InputJSON.ECDHE:
                // Check if HASH field is present (not null) and valid.
                if (hash == null){
                    LOGGER.debug("Input JSON: Hash field not valid ('{}').", hash);
                    return ErrorJSON.ERR_MALFORMED_REQUEST;
                }
                // Check JSON input length.
                if(inputDataB.length!=133){ // 32+32+69 = 133 bytes 
                    LOGGER.debug("Input JSON: ECDH length not valid ({} bytes).", inputDataB.length);
                    return ErrorJSON.ERR_MALFORMED_REQUEST;
                }
                break;
            default:
                LOGGER.debug("Input JSON: Not valid 'method' field for this input JSON ('{}').", method);
                return ErrorJSON.ERR_MALFORMED_REQUEST;
        }
        return null;
    }

    /**
     * This method is used for extract information included inside JSON.
     * @since v0.4.2
     */
    private void readFields() {
        this.protocol = readProtocolField();
        this.spki = readSpkiField();
        this.method = readMethodField();
        this.hash = readHashField();
        this.input = readInputField();
        this.inputData=null;    // Clean JSON variable.
    }

    /**
     * Method used for read JSON "protocol" field.
     * @return Protocol field value or null if it's not present.
     * @since v0.4.2
     */
    private String readProtocolField() {
        String readedProtocol = null;
        if(inputData.containsKey("protocol")){
            switch ((String) inputData.get("protocol")){
                case InputJSON.TLS_1_0:
                    readedProtocol = InputJSON.TLS_1_0;
                    break;
                case InputJSON.TLS_1_1:
                    readedProtocol = InputJSON.TLS_1_1;
                    break;
                case InputJSON.TLS_1_2:
                    readedProtocol = InputJSON.TLS_1_2;
                    break;
                case InputJSON.DTLS_1_0:
                    readedProtocol = InputJSON.DTLS_1_0;
                    break;
                case InputJSON.DTLS_1_2:
                    readedProtocol = InputJSON.DTLS_1_2;
                    break;
                default:
                    LOGGER.error("Not valid 'protocol' field {}.", inputData.get("protocol"));
                    readedProtocol = null;
                    break;
            }
        }
        return readedProtocol;
    }

    /**
     * Method used for read JSON "spki" field.
     * @return Spki field value or null if it's not present.
     * @since v0.4.2
     */
    private String readSpkiField() {
        if(inputData.containsKey("spki")){
            return (String) inputData.get("spki");
        }
        return null;
    }

    /**
     * Method used for read JSON "method" field.
     * @return Method field value or null if it's not present.
     * @since v0.4.2
     */
    private String readMethodField() {
        String readedMethod = null;
        if(inputData.containsKey("method")){
            switch ((String) inputData.get("method")){
                case InputJSON.ECDHE:
                    readedMethod = InputJSON.ECDHE;
                    break;
                case InputJSON.RSA:
                    readedMethod = InputJSON.RSA;
                    break;
                default:
                    readedMethod = null;
                    break;
            }
        }
        return readedMethod;
    }

    /**
     * Method used for read JSON "hash" field.
     * @return Hash field value or null if it's not present.
     * @since v0.4.2
     */
    private String readHashField() {
        if(this.method==null){
            return null;
        }
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
                        LOGGER.error("ECDHE not valid 'hash' field {}.", inputData.get("hash"));
                        readedHash = null;
                        break;
                }
            } else {
                readedHash = SHA1;
            }
        }
        return readedHash;
    }

    /**
     * Method used for read JSON "input" field.
     * @return Input field value or null if it's not present.
     * @since v0.4.2
     */
    private String readInputField() {
        if(inputData.containsKey("input")){
            return (String) inputData.get("input");
        }
        return null;
    }
}

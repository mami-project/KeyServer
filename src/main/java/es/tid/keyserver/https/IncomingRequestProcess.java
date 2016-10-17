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
package es.tid.keyserver.https;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import es.tid.keyserver.controllers.db.DataBase;
import es.tid.keyserver.https.protocol.ErrorJSON;
import es.tid.keyserver.https.protocol.InputJSON;
import es.tid.keyserver.https.protocol.OutputJSON;
import es.tid.keyserver.https.keyprocess.Ecdhe;
import es.tid.keyserver.https.keyprocess.Rsa;
import es.tid.keyserver.controllers.security.ip.WhiteList;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.slf4j.LoggerFactory;

/**
 * This class provide a centralized mode to get, process and make response to the
 *     proxy.
 * @author <a href="mailto:jgm1986@hotmail.com">Javier Gusano Martinez</a>
 */
public class IncomingRequestProcess implements HttpHandler{
    /**
     * Logging object.
     */
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(IncomingRequestProcess.class);
    /**
     * Database Object
     */
    private final DataBase keyServerDB;
    /**
     * IP WhiteList for KeyServer access control.
     */
    private final WhiteList ipwl;
    /**
     * Flag used to storage if the current IP request is authorized (true) or 
     * not (false).
     */
    private boolean ipAuthorized;
    
    /**
     * Default class constructor.
     * @param db Data base connection object.
     * @param wl White list for IP access control.
     */
    public IncomingRequestProcess(DataBase db, WhiteList wl){
        keyServerDB = db;
        ipwl = wl;
    }

    /**
     * This method provides a control function for process the incoming HTTPS
     *     request and send to the Proxy the response.
     * @param he HTTP exchange object with headers and data from the Proxy.
     */
    @Override
    public void handle(HttpExchange he) {
        // JSON incoming data Object.
        InputJSON jsonData;
        // Response String object for send to the client.
        String responseString;
        // Process only POST requests from client.
        String requestMethod = he.getRequestMethod();
        if("POST".equals(requestMethod)){
            LOGGER.trace("Inside HTTP handle: {} | Type: {}", he.getRemoteAddress(), requestMethod);
            // Reading POST body for get the JSON string.
            String jsonString = readHttpBodyInputStream(he.getRequestBody());
            // Creating JSON Object for incoming data.
            jsonData = new InputJSON(jsonString);
            // Check if the current IP is authorized to use KeyServer.
            ipAuthorized = ipwl.iPAuthorized(he.getRemoteAddress().getAddress());
            // Process the JSON for the correct type
            responseString = processIncommingJson(jsonData);
            LOGGER.trace("Response String: {}", responseString);
            // Send response to the client
            sendKeyServerResponse(he, responseString);
        } else {
            // If not POST request (Nothing to do).
            LOGGER.trace("HTTP IncomingRequest not valid: {} from IP: {}",requestMethod, he.getRemoteAddress().getHostString());
        }
        he.close();
    }
    
    /**
     * Check if the current IP is authorized for use KeyServer.
     * @return True if is authorized, false if not.
     */
    public boolean isIpAuthoriced(){
        return ipAuthorized;
    }
    
    /**
     * This method provides the main functionality to send to the client the
     *     request data.
     * @param he HTTP exchange object with headers and data from the Proxy.
     * @param responseString String for send to the client.
     */
    private void sendKeyServerResponse(HttpExchange he, String responseString){
        Headers httpResponseHeaders = he.getResponseHeaders();
        // Preparing response Header.
        httpResponseHeaders.add("Content-Type", "application/json");  
        try {
            // Send response Header.
            he.sendResponseHeaders(200, responseString.getBytes().length);
            // Preparing and send response body.
            OutputStream responseBody = he.getResponseBody();
            responseBody.write(responseString.getBytes());
        } catch (IOException ex) {
                LOGGER.error("Can't send the response to the client...");
        }
    }
    
    /**
     * This method provide an easy way to extract the body content from HTTP 
     *     package.
     * @param bodyStream Input Stream of HTTP body.
     * @return String with the body content.
     */
    private String readHttpBodyInputStream(InputStream bodyStream){
        String jsonString = "";
        try {
            while(bodyStream.available()>0){
                jsonString += (char)bodyStream.read();
            }
            bodyStream.close();
        } catch (IOException ex) {
            LOGGER.error("IO exception for incomming HTTP bodyStream: {}",ex.getMessage());
        }
        return jsonString;
    }
    
    /**
     * This function call to the correct method processor for the incoming JSON
     *     petition.
     * @param jsonObj JSON Object with the proxy data.
     * @return Returns a String with the response to the client.
     */
    private String processIncommingJson(InputJSON jsonObj){
        // Check if the IP is Authorized to use KeyServer 
        if(!this.ipAuthorized){
            LOGGER.error("Not access allowed to the KeyServer from this host.");
            return new ErrorJSON(ErrorJSON.ERR_REQUEST_DENIED).toString();
        }
        // Check first if JSON is valid.
        if(jsonObj.checkValidJSON()!=null){ // If not is valid
            // Generate JSON Output error object and return it as string.
            LOGGER.debug("IncommingJSON Processor: Not valid JSON received. Returns error to the HTTP IncommingProcessor thread.");
            return new ErrorJSON(jsonObj.checkValidJSON()).toString();
        }
        LOGGER.trace("IncommingJSON Processor: Input JSON valid.");
        // If JSON is valid, process response.
        String responseString=null;
        switch (jsonObj.getMethod()){
            case InputJSON.ECDHE: // ECDHE Mode
                LOGGER.debug("Response from KeyServer for ECDH.");
                responseString = modeECDH(jsonObj.getHash(), jsonObj.getSpki(), jsonObj.getInput());
                break;
            case InputJSON.RSA: // RSA Mode
                LOGGER.debug("Response from KeyServer for RSA.");
                responseString = modeRSA(jsonObj.getSpki(), jsonObj.getInput());
                if(responseString == null){
                    responseString = ErrorJSON.ERR_UNSPECIFIED;
                }
                break;
            default:
                // Not valid method.
                LOGGER.error("HTTP Incomming Request Processor: Not valid 'method' value={}.", jsonObj.getMethod());
                responseString = ErrorJSON.ERR_MALFORMED_REQUEST;
                break;
        }
        // Debug logger info:
        LOGGER.debug("HTTP Incomming Request Processor: Valid={}, Method={}, Hash={}, Spki={}, Input={}",
                jsonObj.checkValidJSON(), jsonObj.getMethod(), jsonObj.getHash(), jsonObj.getSpki(), jsonObj.getInput());
        // Check if responseString is an error and returns the correct object as JSON string.
        if(responseString.equalsIgnoreCase(ErrorJSON.ERR_MALFORMED_REQUEST) || 
                responseString.equalsIgnoreCase(ErrorJSON.ERR_NOT_FOUND) ||
                responseString.equalsIgnoreCase(ErrorJSON.ERR_REQUEST_DENIED) || 
                responseString.equalsIgnoreCase(ErrorJSON.ERR_UNSPECIFIED)){
            return new ErrorJSON(responseString).toString();
        } else {
            return new OutputJSON(responseString).toString();
        }
    }

    /**
     * This method is used to sing the 'input' data.
     * @param hash Hash algorithm used for sign data.
     * @param spki SHA1 hash of the private certificate.
     * @param input Data to be signed.
     * @return String with the data signed and encoded using base64.
     */
    private String modeECDH(String hash, String spki, String input) {
        // Execute REDIS query trying to found private key for the incoming SKI.
        byte[] encodePrivateKey = this.keyServerDB.getPrivateForHash(spki);
        if(encodePrivateKey == null){
            return ErrorJSON.ERR_NOT_FOUND;
        }
        // Generate Private Key object.
        PrivateKey privKey = null;
        try {
            privKey = loadPrivateKey(encodePrivateKey);
        } catch (NoSuchAlgorithmException ex) {
            LOGGER.error("RSA Invalid Algorithm exception: {}",ex.getMessage());
        } catch (InvalidKeySpecException ex) {
            LOGGER.error("RSA Invalid Key exception: {}",ex.getMessage());
        }
        // Check if privKey is valid.
        if(privKey == null){
            return ErrorJSON.ERR_UNSPECIFIED;
        }
        try {
            // Sign data
            return Ecdhe.calcOutput(input, privKey, hash);
        } catch (NoSuchAlgorithmException ex) {
            LOGGER.error("ECDH No Such Algorithm Exception: {}", ex.getMessage());
        } catch (InvalidKeySpecException ex) {
            LOGGER.error("ECDH Invalid Key Espec Exception: {}", ex.getMessage());
        } catch (InvalidKeyException ex) {
            LOGGER.error("ECDH Invalid Key Exception: {}", ex.getMessage());
        } catch (SignatureException ex) {
            LOGGER.error("ECDH Signature Exception: {}", ex.getMessage());
        }
        return ErrorJSON.ERR_UNSPECIFIED;
    }

    /**
     * This method provide an easy way to decode a PreMaster secret codified
     *     using RSA.
     * @param spki SHA1 of the certificate to find the PrivateKey.
     * @param input Codified PremasterSecret with public key on base64.
     * @return PremasterSecret decoded using private key and encoded using base64.
     */
    private String modeRSA(String spki, String input) {
        // Execute REDIS query trying to found private key for the incomming SKI.
        byte[] encodePrivateKey = this.keyServerDB.getPrivateForHash(spki);
        if(encodePrivateKey == null){
            return ErrorJSON.ERR_NOT_FOUND;
        }
        // Generate Private Key object.
        PrivateKey privKey = null;
        try {
            privKey = loadPrivateKey(encodePrivateKey);
        } catch (NoSuchAlgorithmException ex) {
            LOGGER.error("RSA Invalid Algorithm exception: {}",ex.getMessage());
        } catch (InvalidKeySpecException ex) {
            LOGGER.error("RSA Invalid Key exception: {}",ex.getMessage());
        }
        // Check if privKey is valid.
        if(privKey == null){
            return ErrorJSON.ERR_UNSPECIFIED;
        }
        // RSA decode and return result
        try {
            return Rsa.calcDecodedOutput(input, privKey);
        } catch (NoSuchAlgorithmException ex) {
            LOGGER.error("RSA No Such Algorithm Exception: {}", ex.getMessage());
        } catch (NoSuchPaddingException ex) {
            LOGGER.error("RSA No Such Padding Exception: {}", ex.getMessage());
        } catch (InvalidKeyException ex) {
            LOGGER.error("RSA Invalid Key Exception: {}", ex.getMessage());
        } catch (IllegalBlockSizeException ex) {
            LOGGER.error("RSA Illegal Blocks Size Exception: {}", ex.getMessage());
        } catch (BadPaddingException ex) {
            LOGGER.error("RSA Bad Padding Exception: {}", ex.getMessage());
        }
        return ErrorJSON.ERR_UNSPECIFIED;
    }
    
    /**
     * Load private key from byte array.
     * @param encodePrivateKey Array with private key values.
     * @return Private key object.
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException 
     */
    private PrivateKey loadPrivateKey(byte[] encodePrivateKey) throws NoSuchAlgorithmException, InvalidKeySpecException{
        java.security.KeyFactory keyFactory = java.security.KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec privatekeySpec = new PKCS8EncodedKeySpec(encodePrivateKey);
        PrivateKey prikey = (PrivateKey) keyFactory.generatePrivate(privatekeySpec);
        return prikey;
    }
}
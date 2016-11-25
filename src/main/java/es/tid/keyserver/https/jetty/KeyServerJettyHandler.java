/*
 * Copyright 2016 Telefonica.
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
package es.tid.keyserver.https.jetty;

import es.tid.keyserver.controllers.db.DataBase;
import es.tid.keyserver.https.keyprocess.Ecdhe;
import es.tid.keyserver.https.keyprocess.Rsa;
import es.tid.keyserver.https.protocol.ErrorJSON;
import es.tid.keyserver.https.protocol.InputJSON;
import es.tid.keyserver.https.protocol.OutputJSON;
import java.io.BufferedReader;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.slf4j.LoggerFactory;

/**
 * Test Class for generate a Jetty test response with a message..
 * @author <a href="mailto:jgm1986@hotmail.com">Javier Gusano Martinez</a>
 * @since v0.4.0
 */
public class KeyServerJettyHandler extends AbstractHandler{
    /**
     * Logger object.
     */
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(KeyServerJettyHandler.class);
    /**
     * Security logger object
     */
    private static final org.slf4j.Logger SECURITY = LoggerFactory.getLogger("security");
    /**
     * Redis database object.
     */
    private DataBase keyServerDB;
    /**
     * Jetty handler class constructor.
     * @param objDB Redis database object.
     * @since v0.4.0
     */
    public KeyServerJettyHandler(DataBase objDB){
        this.keyServerDB = objDB;
    }
    
    /**
     * Handler process method.
     * @param target Target for the request (this is the main directory)
     * @param baseRequest This is the base request.
     * @param request Request from the client.
     * @param response Response to the client.
     * @throws IOException Exception if something goes wrong during communication.
     * @throws ServletException  Problem with the servlet.
     * @since v0.4.0
     */
    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        Thread.currentThread().setName("THHTTPS_" + request.getRemoteAddr());
        // JSON incoming data Object.
        InputJSON jsonData;
        if("POST".equals(request.getMethod())){
            LOGGER.trace("Inside HTTP handle: {} | Type: {}", request.getRemoteAddr(), request.getMethod());
            // Reading POST body for get the JSON string.
            String jsonString = readHttpBody(request);
            LOGGER.trace("POST data received: {}", jsonString);
            // Creating JSON Object for incoming data.
            jsonData = new InputJSON(jsonString);
            // Check if the current IP is authorized to use KeyServer.
            //ipAuthorized = ipwl.iPAuthorized(he.getRemoteAddress().getAddress());
            // Process the JSON for the correct type
            String responseString = processIncommingJson(jsonData);
            LOGGER.trace("Response String: {}", responseString);
            // Send response to the client
            sendKeyServerResponse(baseRequest, response, responseString);
            // Security log entry
            SECURITY.info("Remote IP address: {} | Authorized: {} | Certificate Fingerprint: {}", request.getRemoteAddr(), request.getMethod(), jsonData.getSpki());
        } else {
            // If not POST request (Nothing to do).
            LOGGER.trace("HTTP IncomingRequest not valid: {} from IP: {}", request.getMethod(), request.getRemoteAddr());
            SECURITY.warn("Not valid HTTPS request: {} | Remote address: {} | Body content: {}", request.getMethod(), request.getRemoteAddr(), readHttpBody(request));
        }
    }
    
    /**
     * This method reads the HTTP request body data.
     * @param request Jetty HTTP request object.
     * @return String with the data content.
     * @throws IOException Exception if can't read.
     */
    private String readHttpBody(HttpServletRequest request) throws IOException{
        BufferedReader reader = request.getReader();
        String data = "";
        String line;
        while ((line = reader.readLine()) != null){
            data+=(line);
        }
        return data;
    }
    /**
     * This method provides the main functionality to send to the client the
     *     request data.
     * @param he HTTP exchange object with headers and data from the Proxy.
     * @param responseString String for send to the client.
     * @since v0.4.0
     */
    private void sendKeyServerResponse(Request baseRequest, HttpServletResponse response, String responseString) throws IOException{
        response.setContentType("application/json;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);
        response.getWriter().println(responseString);
    }
    
    /**
     * This function call to the correct method processor for the incoming JSON
     *     petition.
     * @param jsonObj JSON Object with the proxy data.
     * @return Returns a String with the response to the client.
     */
    private String processIncommingJson(InputJSON jsonObj){
        // @TODO: Check if the IP is Authorized to use KeyServer 
        //if(!this.ipAuthorized){
        //    SECURITY.error("Access denied from this host.");
        //    return new ErrorJSON(ErrorJSON.ERR_REQUEST_DENIED).toString();
        //}
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

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
package es.tid.keyserver.controllers.security.ip;

import es.tid.keyserver.https.IncomingRequestProcess;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;
import org.slf4j.LoggerFactory;

/**
 * Main Class for IP White List used by KeyServer.
 * @author <a href="mailto:jgm1986@hotmail.com">Javier Gusano Martinez</a>
 * @since 0.2.1
 */
public class WhiteList {
    /**
     * Logging object.
     */
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(IncomingRequestProcess.class);
    /**
     * Contains the list with the specified IPs.
     */
    private HashSet<InetAddress> ipList;
    /**
     * Flag used to allow all IPs for incoming connections.
     */
    private final boolean allowAll;
    
    /**
     * Default constructor
     * @param iPFileName Name of WhiteList file.
     * @since 0.2.1
     */
    public WhiteList(String iPFileName){
        // Check if file exist
        File list = new File(iPFileName);
        if(list.exists()){
            // Check if can be read
            if(list.canRead()){
                // Load IPs
                ipList = loadFromFile(list);
            } else {
                // Shows error.
                logger.error("Can't read the file {} with the authorized IP "
                        + "address. No connections allowed to the KeyServer.",
                        iPFileName);
            }
            allowAll = false;
        } else {
            // Create empty list
            logger.warn("There are no whitelist file. Allowing all IPs.");
            allowAll = true;
            ipList = new HashSet<>();
        }
    }
    
    /**
     * Check if the specified IP address is inside white list.
     * @param hostIp IP address to be checked.
     * @return True if the specified address is authorized or false if not.
     * @since 0.2.1
     * 
     */
    public boolean iPAuthorized(InetAddress hostIp){
        if(allowAll){
            return true;
        } else {
            return this.ipList.contains(hostIp);
        }
    }

    /**
     * This method is used to read the file and load the values inside HashSet.
     * @param list File object to the IP list.
     * @return Object with file content used to check if the IP is allowed.
     * @since 0.2.1
     */
    private HashSet<InetAddress> loadFromFile(File list) {
        HashSet<InetAddress> tmp = new HashSet<>();
        FileReader is;
        try {
            is = new FileReader(list);
            BufferedReader br = new BufferedReader(is);
            // Read an load the content
            String line;
            while ((line = br.readLine()) != null) {
                if(!line.equalsIgnoreCase("")){
                    InetAddress ip_tmp = giveMeIP4(line);
                    // Add element to the white list.
                    if(ip_tmp!=null){
                        tmp.add(ip_tmp);
                    } else {
                        logger.warn("IP on whitelist not valid '{}' please fix this "
                                + "error. This IP has been omitted.", line);
                    }
                }
            }
            br.close();
            is.close();
        } catch (FileNotFoundException ex) {
            // Error level.
            logger.error("Can't load the KeyServer  IP whitelist file.");
            // Debug level.
            logger.debug("IP white list file route: {}", list.getAbsolutePath());
            // Trace level
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            logger.trace(errors.toString());
        } catch (IOException ex) {
            // Error level.
            logger.error("IO Exception with the KeyServer IP address white list file: {}", list.getAbsolutePath());
            // Trace level.
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            logger.trace(errors.toString());
        } 
        return tmp;
    }

    /**
     * This method returns an InetAddress object from specific String if it's 
     * valid.
     * @param tmp Incoming sting with the IP address.
     * @return The InetAddress object if it's valid. Null if not.
     * @since 0.2.1
     */
    private InetAddress giveMeIP4(String tmp) {
        // Check if the value is not null
        if(tmp == null){
            // Warning level.
            logger.warn("Null value on IP address from IP White List file.");
            return null;
        }
        // Creating InetAddress object and return it.
        InetAddress addr;
        try {
            addr = InetAddress.getByName(tmp);
        } catch (UnknownHostException ex) {
            // Error level.
            logger.error("Unknown Host Exception with the current address on whitelist file: ", tmp);
            // Trace level.
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            logger.trace(errors.toString());
            return null;
        }
       return addr;
    }
}

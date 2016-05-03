/*
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
package es.tid.keyserver.ui.pkmanager;

import es.tid.keyserver.controllers.db.DataBase;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Scanner;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is used to provide and manager private keys on the KeyServer.
 * @author <a href="mailto:jgm1986@hotmail.com">Javier Gusano Martinez</a>
 * @since v0.3.0
 */
public class ProvisionController {
    /**
     * Redis Database object.
     */
    private DataBase dbObj;
    /**
     * User input shell scanner object.
     */
    private Scanner sc;
    /**
     * Logging object.
     */
    private static Logger logger = LoggerFactory.getLogger(ProvisionController.class);
    
    /**
     * Class constructor.
     * @param sc Shell user interface scanner object.
     * @param keyServerDB Data base object.
     * @since v0.3.0
     */
    public ProvisionController(Scanner sc, DataBase keyServerDB){
        this.dbObj = keyServerDB;
        this.sc = sc;
    }
    
    /**
     * This method is used to provisioning a private key on DataBase.
     * @since v0.3.0
     */
    public void menuInsertRegisters() {
        String sha1;
        System.out.print("\n"
                + "                   - Private Key Provision Manager -\n"
                + "                   ---------------------------------\n"
                + "\n"
                + " Action : Insert Private Key to database.\n"
                + "\n"
                + " Description\n"
                + " ------------------------------------------------------------------------\n"
                + " Insert a new private key to KeyServer database. This command use the\n"
                + " certificate SHA1 and the certificate full qualified name to load and\n"
                + " storage the register.\n"
                + "\n"
                + " Note: If you write Q and press ENTER, the private key provision will\n"
                + " be cancelled.\n"
                + "\n"
                + "*************************************************************************\n"
                + " WARNING: Your private key should be in PCKS8 format. If not, please use\n"
                + "          this command to export it previously using OpenSSL (replace\n"
                + "          INPUT.key and OUTPUT.key with your own file names):\n"
                + "\n"
                + " openssl pkcs8 -topk8 -inform PEM -outform DER -in INPUT.key  -nocrypt > OUTPUT.key\n"
                + "*************************************************************************\n"
                + "\n"
                + " Write the certificate SHA1 and press ENTER: ");
        String input = sc.next().trim();
        if(input.equalsIgnoreCase("Q")){
            System.out.println("Private Key provision cancelled by the user.");
            return;
        }
        sha1 = input;
        System.out.println(" Write the certificate file full qualified name:\n ");    
        input = sc.next().trim();
        if(input.equalsIgnoreCase("Q")){
            logger.info("Private Key provision cancelled by the user.");
            return;
        }
        // Certificate provision step to KeyServer database.
        if(this.insertRegiser(sha1, input)){
            logger.info("The certificate has been included on database.");
        } else {
            logger.error("There is a problem with the provision for the current certificate.");
        }
    }

    /**
     * This method is used to show the tool for delete private keys on Redis
     * Database.
     * @since v0.3.0
     */
    public void menuRemoveRegisters() {
        System.out.print("\n"
                + "                   - Private Key Provision Manager -\n"
                + "                   ---------------------------------\n"
                + "\n"
                + " Action : Delete Private Key from database.\n"
                + "\n"
                + " Description\n"
                + " ------------------------------------------------------------------------\n"
                + " Delete a private key from KeyServer database. This command use the\n"
                + " certificate SHA1 to locate and delete the register.\n"
                + "\n"
                + " Note: If you write Q and press ENTER, the private key provision will\n"
                + " be cancelled.\n"
                + "\n"
                + " Write the certificate SHA1 and press ENTER: ");
        String input = sc.next().trim();
        if(input.equalsIgnoreCase("Q")){
            logger.info("Private Key deletion process cancelled by the user.");
            return;
        }
        // Check if the specified SHA1 is on KeyServer Redis Database.
        if(!this.containsRegiser(input)){
            // Error level.
            logger.error("The following SHA1 has not found on database:\n\t\t{}\n", input);
            return;
        } else {
            // Delete the specified register
            if(this.deleteRegiser(input)){
                logger.info("The following SHA1 has been deleted from KeyServer database:\n\t\t{}\n", input);
            } else {
                logger.error("Can't delete the following SHA1 from KeyServer database:\n\t\t{}\n", input);
            }
        }
    }

    /**
     * This method is used to find a register on KeyServer database and show its
     * private key to the user.
     * @since v0.3.0
     */
    public void menuFindRegisters() {
        System.out.print("\n"
                + "                   - Private Key Provision Manager -\n"
                + "                   ---------------------------------\n"
                + "\n"
                + " Action : Find Private Key on database using certificate SHA1.\n"
                + "\n"
                + " Description\n"
                + " ------------------------------------------------------------------------\n"
                + " Find a private key from KeyServer database. This command use the"
                + " certificate SHA1 to locate the register on database.\n"
                + "\n"
                + " Note: If you write Q and press ENTER, the private key provision will\n"
                + " be cancelled.\n"
                + "\n"
                + " Write the certificate SHA1 and press ENTER: ");
        String input = sc.next().trim();
        if(input.equalsIgnoreCase("Q")){
            logger.info("Private Key deletion process cancelled by the user.");
            return;
        }
        // Check if the specified SHA1 is on KeyServer Redis Database.
        if(!this.containsRegiser(input)){
            // Error level.
            logger.info("The following SHA1 has not found on database:\n\t\t{}\n", input);
            return;
        } else {
            // Shows the SHA1 and certificate private key.
            String value = this.dbObj.getPrivateKey(input);
            System.out.println("\n\nRegister found on KeyServer database:\n" +
                    "·SHA1 index: " + input +"\n"+
                    "·PrivateKey (base64):\n"+
                    value+ "\n\n");
        }
    }

    /**
     * This method is used to show a list with the full SHA1 elements present on
     * KeyServer database.
     * @since v0.3.0
     */
    public void menuShowRegistersMen() {
        System.out.print("\n"
                + "                   - Private Key Provision Manager -\n"
                + "                   ---------------------------------\n"
                + "\n"
                + " Action : Shows all SHA1 registers present on KeyServer database.\n"
                + "\n"
                + " Description\n"
                + " ------------------------------------------------------------------------\n"
                + " Full list with the certificate SHA1 present on KeyServer Database.\n"
                + "\n");
        for(String reg : this.getDatabaseRegisers()){
            System.out.println("\t" + reg);
        }
        System.out.println(" ------------------------------------------------------------------------\n");
    }
    
    /**
     * This method provides the main functionality to save a new Private key into 
     * KeyServer database.
     * @param sha1 Hash of the certificate as string.
     * @param fileName String with the file name with content is the PrivateKey.
     * @return True if the register has been saved inside Database.
     * @since v0.3.0
     */
    private boolean insertRegiser(String sha1, String fileName) {
        // Parsing filename from binary to base64 using Linux Base64 tool.
        String privKeyAsString = "";
        Process p;
        try {
            p = Runtime.getRuntime().exec("base64 "+ fileName, null);
            try (Scanner scInputStream = new Scanner(p.getInputStream())) {
                while (scInputStream.hasNext()){
                    privKeyAsString += (scInputStream.nextLine());
                }
            }
            logger.debug("Base64 Private key: " + privKeyAsString);
        } catch (IOException ex) {
            // Error level.
            logger.error("Database IO error exception during insert register action.");
            // Trace level.
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            logger.trace(errors.toString());
        }
        // Save private key into database
        return dbObj.setPrivateKey(sha1, privKeyAsString);
    }
    
    /**
     * This method is used to delete a register from the database.
     * @param sha1 SHA1 fingerprint of the certificate used as database index.
     * @return True if all works as expected. False if something goes wrong.
     * @since v0.3.0
     */
    private boolean deleteRegiser(String sha1) {
        return dbObj.deletePrivateKey(sha1);
    }
    
    /**
     * This method is used to find and show the result on shell.
     * @param sha1 SHA1 fingerprint of the certificate used as database index.
     * @return True if all works correctly. False if not.
     * @since v0.3.0
     */
    private boolean containsRegiser(String sha1) {
        String value = dbObj.getPrivateKey(sha1);
        return value != null;
    }
    
    /**
     * Returns an array with all registers with the database content.
     * @return String array with the full redis database content.
     * @since v0.3.0
     */
    private String[] getDatabaseRegisers() {
        Set<String> list = dbObj.getHashList("*");
        return list.toArray(new String[list.size()]);
    }
}

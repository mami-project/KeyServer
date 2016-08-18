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
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    private final DataBase dbObj;
    /**
     * User input shell scanner object.
     */
    private final Scanner sc;
    /**
     * Logging object.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ProvisionController.class);
    
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
        provisionSteps();
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
            LOGGER.info("Private Key deletion process cancelled by the user.");
            return;
        }
        // Check if the specified SHA1 is on KeyServer Redis Database.
        if(!this.containsRegiser(input)){
            // Error level.
            LOGGER.error("The following SHA1 has not found on database:\n\t\t{}\n", input);
        } else {
            // Delete the specified register
            if(this.deleteRegiser(input)){
                LOGGER.info("The following SHA1 has been deleted from KeyServer database:\n\t\t{}\n", input);
            } else {
                LOGGER.error("Can't delete the following SHA1 from KeyServer database:\n\t\t{}\n", input);
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
                + " Find a private key from KeyServer database. This command use the\n"
                + " certificate SHA1 to locate the register on database.\n"
                + "\n"
                + " Note: If you write Q and press ENTER, the private key provision will\n"
                + " be cancelled.\n"
                + "\n"
                + " Write the certificate SHA1 and press ENTER: ");
        String input = sc.next().trim();
        if(input.equalsIgnoreCase("Q")){
            LOGGER.info("Private Key deletion process cancelled by the user.");
            return;
        }
        // Check if the specified SHA1 is on KeyServer Redis Database.
        if(!this.containsRegiser(input)){
            // Error level.
            LOGGER.info("The following SHA1 has not found on database:\n\t\t{}\n", input);
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
     * @param fileName Filename with the private key.
     * @param expDat String with the certificate expiration date.
     * @return True if the register has been saved inside Database.
     * @since v0.3.0
     */
    private boolean insertRegiser(String sha1, String fileName, String expDat) {
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
            LOGGER.debug("Base64 Private key: " + privKeyAsString);
        } catch (IOException ex) {
            // Error level.
            LOGGER.error("Database IO error exception during insert register action.");
            // Trace level.
            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            LOGGER.trace(errors.toString());
        }
        // Save private key into database
        if(expDat == null){
            return dbObj.setPrivateKey(sha1, privKeyAsString);
        } else {
            dbObj.setPrivateKey(sha1, privKeyAsString);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd-HH:mm:ss");
            try {
                return dbObj.setExpPK(sha1, dateFormat.parse(expDat).getTime()/1000);
            } catch (ParseException ex) {
                LOGGER.error("Parser exception during PK expiration date conversion: {}", expDat);
                return false;
            }
        }
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

    /**
     * This method is used to define the steps for Private Key data base 
     * provisioning.
     * @since v0.3.1
     */
    private void provisionSteps() {
        String sha1;
        String pkfile;
        // Private key fingerprint SHA1.
        String input = sc.next().trim();
        if(input.equalsIgnoreCase("Q")){
            System.out.println("Private Key provision cancelled by the user.");
            return;
        }
        sha1 = input;
        // Private key file
        System.out.print(" Write the certificate file full qualified name:\n>> ");    
        input = sc.next().trim();
        if(input.equalsIgnoreCase("Q")){
            LOGGER.info("Private Key provision cancelled by the user.");
            return;
        }
        pkfile = input;
        // Ask to the user if the current private key should be removed automatically.
        boolean autoRemoveKey = false;
        do{
            System.out.print(" Do you want remove automatically this current private key\n"
                    + " in a specific date? (y/n):");   
            input = sc.next().trim();
            if(input.equalsIgnoreCase("Q")){
                LOGGER.info("Private Key provision cancelled by the user.");
                return;
            } else if("y".equalsIgnoreCase(input)){
                autoRemoveKey = true;
            } else if("n".equalsIgnoreCase(input)){
                autoRemoveKey = false;
            } else {
                LOGGER.error("Not valid selection (y/n): {}: ", input);
            }
        } while(!("y".equalsIgnoreCase(input) || "n".equalsIgnoreCase(input)));
        input = null;
        // Auto remove certificate when exceeds the expiration date.
        if(autoRemoveKey){
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd-HH:mm:ss");
            boolean validDate;
            do{
                System.out.println(" Write the certificate expiration date. If you don't provide\n"
                        + " the date, the private key will be present inside database\n"
                        + " ulimited. You must remove it manually.\n");
                System.out.print(" Write the date using this format (yyyy.MM.dd-HH:mm:ss): ");    
                input = sc.next().trim();
                if(input.equalsIgnoreCase("Q")){
                    LOGGER.info("Private Key provision cancelled by the user.");
                    return;
                }
                try {
                    if(input!=null){
                        dateFormat.parse(input);
                    }
                    validDate = true;
                } catch (ParseException ex) {
                    validDate = false;
                    LOGGER.error("Not valid date during PK provisioning: {}", input);
                }
            } while(!validDate);
        }
        // Certificate provision step to KeyServer database.
        if(insertRegiser(sha1, pkfile, input)){
            LOGGER.info("The certificate has been included on database: {}", sha1);
        } else {
            LOGGER.error("There is a problem with the provision for the current certificate: {}", sha1);
        }
    }
}

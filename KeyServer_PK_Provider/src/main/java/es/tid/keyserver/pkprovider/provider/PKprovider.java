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
package es.tid.keyserver.pkprovider.provider;

import es.tid.keyserver.pkprovider.config.ConfigFile;
import es.tid.keyserver.pkprovider.database.DataBase;
import java.io.IOException;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class for provisioning with private keys the KeyServer database.
 * @author <a href="mailto:jgm1986@hotmail.com">Javier Gusano Martinez</a>
 */
public class PKprovider {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Check number of shell input parameters
        if(args.length > 0){
            // Load config file with tool parammeters
            ConfigFile softwareConfig = new ConfigFile("general.properties");
            // Process options
            switch(args[0]){
                case "-h":
                case "-H":
                    PKprovider.showHelp();
                    break;
                case "-i":
                case "-I":
                    if(args.length==3){
                        // Create a database Object.
                        DataBase keyServerDB = new DataBase(softwareConfig);
                        boolean result = InsertRegiser(keyServerDB, args[1], args[2]);
                        if (result){
                            System.out.println("Done!");
                        } else {
                            System.out.println("Private key not saved.");
                        }
                    } else {
                        System.err.println("Not provided correct number of "
                                + "parammeters for 'insert' option.\n Please use:\n"
                                + "KeyServer_PK_Provider.jar -i SHA1_HASH PRIVATE_KEY_FILE");
                    }
                    break;
                case "-d":
                case "-D":
                    if(args.length==2){
                        // Delete a database Object.
                        DataBase keyServerDB = new DataBase(softwareConfig);
                        boolean result = DeleteRegiser(keyServerDB, args[1]);
                        if (result){
                            System.out.println("Done!");
                        } else {
                            System.out.println("Private key not deleted.");
                        }
                    } else {
                        System.err.println("Not provided correct number of "
                                + "parammeters for 'delete' option.\n Please use:\n"
                                + "KeyServer_PK_Provider.jar -d SHA1_HASH");
                    }
                    break;
                case "-f":
                case "-F":
                    if(args.length==2){
                        // Find a database register.
                        DataBase keyServerDB = new DataBase(softwareConfig);
                        FindRegiser(keyServerDB, args[1]);
                    } else {
                        System.err.println("Not provided correct number of "
                                + "parammeters for 'delete' option.\n Please use:\n"
                                + "KeyServer_PK_Provider.jar -d SHA1_HASH");
                    }
                    break;
                case "-l":
                case "-L":
                    // Find a database register.
                    DataBase keyServerDB = new DataBase(softwareConfig);
                    PrintFullDatabaseRegisers(keyServerDB);
                    break;
                default:
                    System.err.println("Your option '"+ args[0]+"' is not valid for this tool.");
            }
        } else {
            System.err.println("Error: No input parameters. Use '-h' for get "
                    + "a list with supported commands.");
        }
    }
    
    /**
     * This method shows a help message on shell output.
     */
    private static void showHelp(){
        System.out.println("\n                # KeyServer PrivateKey Provider #\n" +
                "-----------------------------------------------------------------\n" +
                "This tool is used to provide private keys associated to SHA1 \n" +
                "certificate value in to KeyServer database.\n" +
                "\n" +
                "·How to use?\n" +
                "This is the main structure of the command:\n" +
                "-------------------------------------------------------------------------------------+\n" +
                "| $ java -jar KeyServer_PK_Provider.jar [OPTION] <certificate_sha1> <PrivateKeyFile> |\n" +
                "-------------------------------------------------------------------------------------+\n" +
                "\n" +
                "·List of supported options\n" +
                "Supported options implemented in to this tool:\n" +
                "\n" +
                "+----------+-----------------------------------------------------------------------------------------------------+\n" +
                "| [OPTION] | Description                                                                                         |\n" +
                "+----------+-----------------------------------------------------------------------------------------------------+\n" +
                "|    -h    | Shows basic help. Example:                                                                          |\n" +
                "|          |  java -jar KeyServer_PK_Provider.jar -h                                                             |\n" +
                "+----------+-----------------------------------------------------------------------------------------------------+\n" +
                "|    -i    | Insert new private key inside KeyServer database. Example:                                          |\n" +
                "|          |  java -jar KeyServer_PK_Provider.jar -i 405FD8A83BFB64683BAEB51D9F8D99C9D872FA63 privateKeyFile.key |\n" +
                "+----------+-----------------------------------------------------------------------------------------------------+\n" +
                "|    -d    | Delete a private key from KeyServer database. This command use the SHA1 of the certificate for      |\n" +
                "|          | locate and delete the register. Example:                                                            |\n" +
                "|          |  java -jar KeyServer_PK_Provider.jar -d 405FD8A83BFB64683BAEB51D9F8D99C9D872FA63                    |\n" +
                "+----------+-----------------------------------------------------------------------------------------------------+\n" +
                "|    -f    | Find a register inside KeyServer database using SHA1 certificate. Example:                          |\n" +
                "|          |  java -jar KeyServer_PK_Provider.jar -f 405FD8A83BFB64683BAEB51D9F8D99C9D872FA63                    |\n" +
                "+----------+-----------------------------------------------------------------------------------------------------+\n" +
                "|    -l    | Show a full list with the database content. Only show the SHA1 certificates. Example:               |\n" +
                "|          |  java -jar KeyServer_PK_Provider.jar -l                                                             |\n" +
                "+----------+-----------------------------------------------------------------------------------------------------+\n");
    }

    /**
     * This method provides the main functionality to save a new Private key into 
     * KeyServer database.
     * @PARAM keyServerDB KeyServer Redis Database object.
     * @param sha1 Hash of the certificate as string.
     * @param fileName String with the file name with content is the PrivateKey.
     * @return True if the register has been saved inside Database.
     */
    private static boolean InsertRegiser(DataBase keyServerDB, String sha1, String fileName) {
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
            System.out.println("[debug] Base64 Private key: " + privKeyAsString);
        } catch (IOException ex) {
            Logger.getLogger(PKprovider.class.getName()).log(Level.SEVERE, null, ex);
        }
        // Save private key into database
        return keyServerDB.setPrivateKey(sha1, privKeyAsString);
    }

    /**
     * This method is used to delete a register from the database.
     * @param keyServerDB KeyServer Redis Database object.
     * @param sha1 SHA1 fingerprint of the certificate used as database index.
     * @return True if all works as expected.
     */
    private static boolean DeleteRegiser(DataBase keyServerDB, String sha1) {
        return keyServerDB.deletePrivateKey(sha1);
    }

    
    /**
     * This method is used to find and show the result on shell.
     * @param keyServerDB KeyServer Redis Database object.
     * @param sha1 SHA1 fingerprint of the certificate used as database index.
     * @return True if all works correctly. False if not.
     */
    private static void FindRegiser(DataBase keyServerDB, String sha1) {
        String value = keyServerDB.getPrivateKey(sha1);
        if(value == null){
            System.out.println("Register not found on database for the following key: " + sha1);
        } else {
            System.out.println("Register found on KeyServer database:\n" +
                    "·SHA1 index: " + sha1 +"\n"+
                    "·PrivateKey (base64):\n"+
                    value);
        }
    }

    /**
     * Show all registers on Redis Database.
     * @param keyServerDB Redis database object.
     */
    private static void PrintFullDatabaseRegisers(DataBase keyServerDB) {
        Set<String> list = keyServerDB.getHashList("*");
        String[] array = list.toArray(new String[list.size()]);
        for(int i=0; i<array.length; i++){
            System.out.println(i + ") "+ array[i]);
        }
    }
    
    
}

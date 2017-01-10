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
package es.tid.keyserver.ui;

import es.tid.keyserver.controllers.db.DataBase;
import es.tid.keyserver.core.status.KsMonitor;
import es.tid.keyserver.ui.controls.StatusController;
import es.tid.keyserver.ui.pkmanager.ProvisionController;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

/**
 * This class is used to manage the interactions between the KeyServer tool and 
 *     its administrator user.
 * @author <a href="mailto:jgm1986@hotmail.com">Javier Gusano Martinez</a>
 * @since v0.3.0
 */
public class UserInterfaceController {
	
    /**
     * KeyServer status monitor object.
     */
    private final KsMonitor monObj;
    
    /**
     * Key Server Private Key management object.
     */
    private ProvisionController pkCtrl;
    
    /**
     * KeyServer exit flag.
     *     If this flag is set to true, means the KeyServer will be closed.
     */
    private boolean exitFlag;
    /**
     * Logging object.
     */
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(UserInterfaceController.class);
    
    /**
     * Class constructor.
     * @param sc Shell user interface scanner object.
     * @param monObj KeyServer status monitor object.
     * @param dbObj Redis DataBase Object.
     * @since v0.3.0
     */
    public UserInterfaceController(Scanner sc, KsMonitor monObj, DataBase dbObj){
        // Set the constructor object to the class
        this.monObj = monObj;
        pkCtrl = new ProvisionController(sc, dbObj);
    }
    
    /**
     * This method receive the user input as String and make the required action
     * @param option String with the user input.
     * @since v0.3.0
     */
    public void userInputDigest(String option){
        switch(option.toUpperCase()){
            case "H":
                // Shows keyserver available commands.
                StatusController.showKeyServerHelp();
                break;
            case "I":
                // Shows KeyServer status and details.
                StatusController.showKsStatusDetails(monObj);
                break;
            case "P":
                // Provision: Insert a new private key inside KeyServer database."
                this.pkCtrl.menuInsertRegisters();
                break;
            case "D":
                //Delete: Remove a private key from the KeyServer database."
                this.pkCtrl.menuRemoveRegisters();
                break;
            case "F":
                // Find: Search a specific SHA1 register on KeyServer database."
                this.pkCtrl.menuFindRegisters();
                break;
            case "L":
                // List: Shows all registers inside KeyServer database."
                this.pkCtrl.menuShowRegistersMen();
                break;
            case "Q":
                // Close KeyServer
                this.exitFlag = true;
                break;
            case "S":
                // Shows KeyServer status and details.
                StatusController.showKsStats(this.monObj.getStatistics());
                break;
            default:
                // Not valid option.
                LOGGER.warn("Current user input not valid: {}", option);
                break;
        }
    }
    
    /**
     * This method is used to check if the KeyServer close operation has been
     *     requested by the user.
     * @return If the KeyServer should be closed, this method returns 'true'. False
     *     if not.
     * @since v0.3.0
     */
    public boolean exitFlag(){
        return this.exitFlag;
    }
}

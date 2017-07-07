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

package es.tid.keyserver.ui.controls;

import es.tid.keyserver.core.status.KsMonitor;
import org.eclipse.jetty.server.handler.StatisticsHandler;

/**
 * This class contains the statics method for KeyServer status user interface.
 * @author <a href="mailto:jgm1986@hotmail.com">Javier Martinez Gusano</a>
 * @since v0.3.0
 */
public class StatusController {
    /**
     * This method shows a list with supported parameters for KeyServer management.
     * @since v0.3.0
     */
    static public void showKeyServerHelp(){
        System.out.println("\n"
                + "                         - KeyServer Options -\n"
                + "                         ---------------------\n"
                + " Option   Description\n"
                + " ------   ---------------------------------------------------------------\n"
                + "    H      Shows help menu options.\n"
                + "    I      Shows KeyServer status and details.\n"
                + "    S      Jetty HTTPs connection statistics.\n"
                + "\n"
                + "    P      Provision: Insert a new private key inside KeyServer database.\n"
                + "    D      Delete: Remove a private key from the KeyServer database.\n"
                + "    F      Find: Search a specific SHA1 register on KeyServer database.\n"
                + "    L      List: Shows all registers inside KeyServer database.\n"
                + "\n"
                + "    Q      Close KeyServer tool.\n"
                + "\n"
                + " Write the option key and Enter to execute.");
    }
    
    /**
     * This method shows the current KeyServer status to the user on the screen.
     * @param mon KeyServer monitor object.
     * @since v0.3.0
     */
    static public void showKsStatusDetails(KsMonitor mon){
        System.out.println("\n"
                + "                          - KeyServer Status -\n"
                + "                          --------------------\n"
                + "  Info \n"
                + "  ----\n"
                + "  - Running since: " + mon.keyServerRunningSince() + "\n"
                + "  - Current version: " + mon.getCurrentKSVersion() + "\n"
                + "  - Last version available: " + mon.getLastKSVersionAvailable() + "\n"
                + "    URL: " + mon.getKSProjectURL() + "\n"
                + "  \n"
                + "  Services\n"
                + "  --------\n"
                + "  - Redis Status:\n"
                + "  \tConnection active: " + mon.isRedisConnectionAvailable() + "\n"
                + "  - Jetty HTTPS Server Status: " + mon.httpsServerStatus() + "\n"
                + "  \tCertificate expiration date: " + mon.getHttpsCertificateExpDate() + "\n"
                + "  \tCertificate remaining days: " + mon.getHttpsCertificateRemainDays() + "\n"
                + "\n");
    }

    /**
     * This method shows a formated message with Jetty statistics elements.
     * @param statistics Jetty statistics object.
     * @since v0.4.0
     */
    public static void showKsStats(StatisticsHandler statistics) {
        System.out.println("\n"
                + "                         - Connection Status -\n"
                + "                         ---------------------\n"
                + "  Stats" + "\n"
                + "  -----" + "\n"
                + "   - Total dispatched: " + statistics.getDispatched() +"\n"
                + "   - Current dispatches active: " + statistics.getDispatchedActive() +"\n"
                + "   - Current active requests: " + statistics.getRequestsActive() +"\n"
                + "   - Max number of dispatched: " + statistics.getDispatchedActiveMax() +"\n"
                + "   - Request dispatch time (Max / Mean): " + statistics.getDispatchedTimeMax() + "ms / "+ statistics.getDispatchedTimeMean() +"ms\n"
                + "   - Number of requests expired while suspended: " + statistics.getExpires() +"\n"
                + "   - Dispatched request time (Max / Mean): " + statistics.getRequestTimeMax() + "ms / "+ statistics.getRequestTimeMean() +"ms\n"
                + "   - Total request handled: " + statistics.getRequests() +"\n"
                + "   - Maximum number of active requests: " + statistics.getRequestsActiveMax() +"\n\n"
                + "  HTTP Response Status Codes" + "\n"
                + "  --------------------------" + "\n"
                + "   - 1xx (Informational):\t" + statistics.getResponses1xx() + "\n"
                + "   - 2xx (Success):\t\t" + statistics.getResponses2xx() + "\n"
                + "   - 3xx (Redirection):\t\t" + statistics.getResponses3xx() + "\n"
                + "   - 4xx (Client Error):\t" + statistics.getResponses4xx() + "\n"
                + "   - 5xx (Server Error):\t" + statistics.getResponses5xx() + "\n");
    }
}

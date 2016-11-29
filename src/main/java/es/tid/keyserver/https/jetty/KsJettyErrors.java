package es.tid.keyserver.https.jetty;

import es.tid.keyserver.https.protocol.ErrorJSON;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.servlet.ErrorPageErrorHandler;
import org.slf4j.LoggerFactory;

/**
 * Class for custom management of Jetty server errors.
 * @author <a href="mailto:jgm1986@hotmail.com">Javier Gusano Martinez</a>
 * @since v0.4.0
 */
public class KsJettyErrors extends ErrorPageErrorHandler {
    /**
     * Security logger object
     */
    private static final org.slf4j.Logger SECURITY = LoggerFactory.getLogger("security");
    
    /**
     * Jetty error handler for customize output.
     * @param target target Target for the request.
     * @param baseRequest This is the base request.
     * @param request Request from the client.
     * @param response Response to the client.
     * @throws IOException 
     * @since v0.4.0
     */
    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Thread.currentThread().setName("THJETTYERR_" + request.getRemoteAddr() + ":" + request.getRemotePort());
        String jsonOut;
        switch(response.getStatus()){
            case 403:
                // Unauthorized requests.
                jsonOut = new ErrorJSON(ErrorJSON.ERR_REQUEST_DENIED).toString();
                SECURITY.error("Incomming request for unauthorized IP: {} | Type: {} | Target: {}", 
                        request.getRemoteAddr(), 
                        request.getMethod(), 
                        target);
                break;
            default:
                // Message for not defined errors.
                jsonOut = new ErrorJSON(ErrorJSON.ERR_UNSPECIFIED).toString();
                SECURITY.error("Undefined error Code: {} | IP: {} | Type: {} | Target: {}", 
                        response.getStatus(), 
                        request.getRemoteAddr(), 
                        request.getMethod(), 
                        target);
        }
        response.getWriter().append(jsonOut);
    }
}

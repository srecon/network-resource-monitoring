package ru.fors.diagnostics;


import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.fors.diagnostics.HttpRequests.Authenticate;
import ru.fors.diagnostics.config.Server;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * User: sahmed
 * Date: 05.05.11 11:45
 *
 * @Copyright sahmed
 */
public class Monitoring {
    Logger logger = LoggerFactory.getLogger(Monitoring.class);
    private List<Server> servers;

    private volatile List<ErrorResponse> errors;

    private Monitoring(){
        errors = new ArrayList<ErrorResponse>();
    }

    public Monitoring(List<Server> servers) {
        this();
        this.servers = servers;
    }

    public List<ErrorResponse> getErrors() {
        return errors;
    }

    public List<Server> getServers() {
        return servers;
    }

    // @deprecated
    public void monitor(){
        HttpURLConnection connection = null;
        for(Server server : servers){
            try {
                URL url = getUrl(server);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                int responseCode = connection.getResponseCode();
                logger.info("Server response code:" + responseCode);
                String responseMsg =  connection.getResponseMessage();
                logger.info("responseMsg:"+ responseMsg);
                if(responseCode != 200 && responseCode != 403){
                    // generate report
                    logger.warn("Some of the server has down:" + url.toString());
                    ErrorResponse error = new ErrorResponse(responseMsg, responseCode, url.toString());
                    errors.add(error);
                }
            } catch (IOException e) {
                logger.error("IOException", e);
            } finally {
                connection.disconnect();
            }
        }
    }
    public void new_monitor(){
        for(Server server : getServers()){
            URL url = getUrl(server);
            HttpResponse response = Authenticate.getResponse(url.toString(),server.getAuthconfig());
            logger.info(response != null ? response.getStatusLine().toString() : "null");
            if(response == null || (response.getStatusLine().getStatusCode()!= HttpStatus.SC_OK &&
                                    response.getStatusLine().getStatusCode()!= HttpStatus.SC_MOVED_TEMPORARILY &&
                                    response.getStatusLine().getStatusCode()!= HttpStatus.SC_FORBIDDEN)){

                logger.warn("Some of the server has down:" + url.toString());
                ErrorResponse error;
                if(response != null){
                    error = new ErrorResponse(response.getStatusLine().toString(), response.getStatusLine().getStatusCode(), url.toString());

                }else{
                    error = new ErrorResponse("response null",1000, url.toString());
                }
                errors.add(error);
            }
        }
    }
    public URL getUrl(final Server server){
        final String urlStr = server.getProtocol()+"://"+server.getHostName()+":"+server.getPort()+"/"+server.getContextRoot();
        URL url = null;
        try {
            url = new URL(urlStr);
        } catch (MalformedURLException e) {
            logger.error("MalFormed Exception:", e);
        }
        return url;
    }

    public void clearErrors(){
        this.errors.clear();
    }

    public boolean hasError(){
        return !this.errors.isEmpty();
    }
}

package ru.fors.diagnostics.HttpRequests;


import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.fors.diagnostics.config.Authconfig;
import ru.fors.diagnostics.config.Param;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: sahmed
 * Date: 19.05.11 10:06
 *
 * @Copyright sahmed
 */
public class Authenticate {
    private static Logger logger = LoggerFactory.getLogger(Authenticate.class);

    public static HttpResponse getResponse(final String serverUrl, final Authconfig authConfig){

        HttpClient httpClient = new DefaultHttpClient();
        // set time out
        HttpParams httpParams = httpClient.getParams();
        HttpConnectionParams.setConnectionTimeout(httpParams,5000);
        HttpConnectionParams.setSoTimeout(httpParams, 5000);

        HttpPost httpPost = new HttpPost(serverUrl);
        HttpResponse response = null;
        List<NameValuePair> nvp = new ArrayList<NameValuePair>();
        if(authConfig!= null && !authConfig.getParam().isEmpty()){
            // add auth params
            for(Param param : authConfig.getParam()){
                nvp.add(new BasicNameValuePair(param.getName(), param.getValue()));
            }
        }
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nvp, HTTP.UTF_8));
            response = httpClient.execute(httpPost);

        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
        } catch(ClientProtocolException e){
            logger.error(e.getMessage());
        } catch(IOException e){
            logger.error(e.getMessage());
        } finally {
            httpClient.getConnectionManager().shutdown();
        }

        return response;
    }

}

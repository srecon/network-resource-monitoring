package ru.fors.diagnostics;

import java.io.Serializable;

/**
 * User: sahmed
 * Date: 06.05.11 9:04
 *
 * @Copyright sahmed
 */
public class ErrorResponse implements Serializable {
    private String responseMsg;
    private int responseCode;
    private String url;

    public ErrorResponse(String responseMsg, int responseCode, String url) {
        this.responseMsg = responseMsg;
        this.responseCode = responseCode;
        this.url = url;
    }

    public String getResponseMsg() {
        return responseMsg;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return new StringBuffer("\n").append("URL: "+ this.url).append(" | Response code:"+this.responseCode).toString();
    }
}

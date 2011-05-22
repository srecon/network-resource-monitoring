package ru.fors.diagnostics.notification;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * User: sahmed
 * Date: 06.05.11 10:44
 *
 * @Copyright sahmed
 */
public class SMTPAuthenticator extends Authenticator {
    private String username;
    private String password;
    private boolean needAuth;

    public SMTPAuthenticator(String username, String password, boolean needAuth) {
        this.username = username;
        this.password = password;
        this.needAuth = needAuth;
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        if(needAuth){
            return new PasswordAuthentication(username, password);
        }else{
            return null;
        }
    }
}

package ru.fors.diagnostics.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.fors.diagnostics.ErrorResponse;
import ru.fors.diagnostics.config.Config;
import ru.fors.diagnostics.config.Settings;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Properties;

/**
 * User: sahmed
 * Date: 06.05.11 10:46
 *
 * @Copyright sahmed
 */
public class EmailSender {
    private Logger logger = LoggerFactory.getLogger(EmailSender.class);

    private Settings settings;

    public EmailSender(Settings settings) {
        this.settings = settings;
    }

    public Settings getSettings() {
        return settings;
    }

    private Properties prepareProperties(){
        Properties props = new Properties();
        props.setProperty("mail.smtp.host", getSettings().getSmtp().getHost());
        props.setProperty("mail.smtp.port", String.valueOf(getSettings().getSmtp().getPort()) );
        props.setProperty("mail.smtp.user", getSettings().getSmtp().getUsername());
        props.setProperty("mail.smtp.password", getSettings().getSmtp().getPassword());
        props.setProperty("mail.smtp.auth", "true");
        return props;
    }
    private MimeMessage prepareMessage(Session mailSession,String charset,
                                        String from, String subject,
                                        String HtmlMessage,String[] recipient) {
        //Multipurpose Internet Mail Extensions
        MimeMessage message = null;
        try {
            message = new MimeMessage(mailSession);

            message.setFrom(new InternetAddress(from));

            message.setSubject(subject, "UTF-8");

            for (int i=0;i<recipient.length;i++){
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient[i]));
            }

            message.setContent(HtmlMessage, "text/html; charset=\""+charset+"\"");

            logger.info("Message prepared..");
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return message;
    }
    public void sendEmail(List<ErrorResponse> errors)
    {
        Transport transport = null;

        String from = getSettings().getSmtp().getFrom();
        String[] to = getReceiptions(getSettings().getNotifications().getEmails());
        String subject = getSettings().getSmtp().getSubject();
        String userName = getSettings().getSmtp().getUsername();
        String password = getSettings().getSmtp().getPassword();

        String HtmlMessage = generateMsg(getSettings().getSmtp().getMsgTemplate(), errors);

        try {
            Properties props = prepareProperties();

            Session mailSession = Session.getDefaultInstance(
                            props, new SMTPAuthenticator(userName,password, true));

            transport =  mailSession.getTransport("smtp");
            MimeMessage message = prepareMessage(mailSession, "UTF-8",
                                                from, subject, HtmlMessage, to);
            transport.connect();
            Transport.send(message);
            logger.info("Message send...");
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        finally{
            try {
                transport.close();
            } catch (MessagingException ex) {
                logger.error(ex.getMessage());
            }
        }
    }
    private String[] getReceiptions(String receptions){
        return receptions.split(",");
    }
    //@todo use mesage format
    private String generateMsg(String template, List<ErrorResponse> errors){
        StringBuffer msg = new StringBuffer(template).append("<br/>");
        int cnt=1;
        for(ErrorResponse error : errors){
            msg.append(cnt).append(") ").append(error.toString()).append("<br/>");
            cnt++;
        }
        return msg.toString();
    }
}

package ru.fors.diagnostics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.fors.diagnostics.config.Config;
import ru.fors.diagnostics.notification.EmailSender;

import java.util.Calendar;
import java.util.Date;

/**
 * User: sahmed
 * Date: 05.05.11 15:16
 *
 * @Copyright Fors Developer center
 * @deprecated
 */
public class Runner implements Runnable {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final long MILI_SECOND= 1000l;
    //@todo use conf, even better to use quartz
    private static final int INTERVAL = 3;
    private boolean hasNotified = false;

    public void run() {
        ReadXMLConfig xmlConfig = new ReadXMLConfig();
        final Config config = xmlConfig.getConfig();
        final long interval = config.getSettings().getNotifications().getInterval() * MILI_SECOND;

        Monitoring monitor = new Monitoring(config.getServers().getServer());
        EmailSender emailSender = new EmailSender(config.getSettings());
        Date notifyTime = null;
        for(;;){
            //monitor.monitor();
            monitor.new_monitor();
            if(monitor.hasError()){
                if(!hasNotified){
                    //emailSender.sendEmail(monitor.getErrors());
                    notifyTime = new Date(System.currentTimeMillis());
                    hasNotified = true;
                }else{
                    // resend after 3 hour
                    if(isTimeToResend(notifyTime)){
                        //emailSender.sendEmail(monitor.getErrors());
                        notifyTime = new Date(System.currentTimeMillis());
                        logger.info("Email resend...");
                    }
                }
            }else{
                hasNotified = false;
            }
            monitor.clearErrors();
            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
            }
        }
    }
    public boolean isTimeToResend(Date lastSendTime){
        Calendar cal = Calendar.getInstance();
        cal.setTime(lastSendTime);
        cal.add(Calendar.HOUR,INTERVAL);
        return cal.getTime().after(new Date(System.currentTimeMillis()));
    }
}

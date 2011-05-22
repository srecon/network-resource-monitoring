package ru.fors.diagnostics.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.fors.diagnostics.Monitoring;
import ru.fors.diagnostics.ReadXMLConfig;
import ru.fors.diagnostics.config.Config;
import ru.fors.diagnostics.notification.EmailSender;

import java.util.Calendar;
import java.util.Date;

/**
 * User: sahmed
 * Date: 19.05.11 15:16
 *
 * @Copyright Fors Developer center
 */
public class MonitorTask {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final long MILI_SECOND= 1000l;
    private static final int INTERVAL = 3;
    private boolean hasNotified = false;
    private static Date notifyTime = new Date(System.currentTimeMillis());

    public synchronized void run() {
        ReadXMLConfig xmlConfig = new ReadXMLConfig();
        final Config config = xmlConfig.getConfig();
        final long interval = config.getSettings().getNotifications().getInterval() * MILI_SECOND;

        Monitoring monitor = new Monitoring(config.getServers().getServer());
        EmailSender emailSender = new EmailSender(config.getSettings());
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
    }
    public boolean isTimeToResend(Date lastSendTime){
        Calendar cal = Calendar.getInstance();
        cal.setTime(lastSendTime);
        cal.add(Calendar.HOUR,INTERVAL);
        return cal.getTime().before(new Date(System.currentTimeMillis()));
    }
}

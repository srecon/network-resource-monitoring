package ru.fors.diagnostics;

import org.quartz.*;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.calendar.HolidayCalendar;
import ru.fors.diagnostics.config.Config;
import ru.fors.diagnostics.schedule.MonitorJob;
import ru.fors.diagnostics.schedule.MonitorTask;

import java.text.ParseException;
import java.util.Date;

import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.quartz.CronScheduleBuilder.*;

/**
 * User: sahmed
 * Date: 11.05.11 10:38
 *
 * @Copyright sahmed
 */
public class StartService {
    public static void main(String[] args) {

        ReadXMLConfig xmlConfig = new ReadXMLConfig();
        final Config config = xmlConfig.getConfig();

        JobDetail jobDetail = newJob(MonitorJob.class).withIdentity("monitorJob").build();
        jobDetail.getJobDataMap().put("monitorTask", new MonitorTask());
        // define trigger
        /*Trigger trigger = newTrigger().withIdentity("monitorTrigger").startNow()
                                      .withSchedule(simpleSchedule()
                                      .withIntervalInMinutes(config.getSettings().getNotifications().getInterval())
                                      .repeatForever())
                                      //.withIntervalInSeconds(20).repeatForever())
                                      //.modifiedByCalendar("holidays")
                                      .build();
        */
        // define cron - every minute on 9-17 by mon-fri
        Trigger trigger = null;
        try {
            trigger = newTrigger().withIdentity("monitorTrigger").startNow()
                                          .withSchedule(cronSchedule("0 0/2 13-19 ? * MON-FRI"))
                                          .build();
        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println("Trigger can't configured -- system will be exit");
            System.exit(-1);
        }
        SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();

        Scheduler sched = null;
        try {
            sched = schedFact.getScheduler();

            sched.start();
            sched.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

    }
}

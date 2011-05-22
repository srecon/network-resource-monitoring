package ru.fors.diagnostics.schedule;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Map;

/**
 * User: sahmed
 * Date: 19.05.11 15:05
 *
 * @Copyright Fors Developer center
 */
public class MonitorJob implements Job{

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Map dataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        MonitorTask task = (MonitorTask) dataMap.get("monitorTask");
        task.run();
    }
}

package com.example.scheduler_service.services;

import com.example.scheduler_service.config.AppConfig;
import com.example.scheduler_service.scheduler.CallbackJob;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class SchedulerService {

    private static final Logger logger = LoggerFactory.getLogger(SchedulerService.class);

    private final SchedulerFactoryBean schedulerFactoryBean;
    private final ApplicationContext applicationContext;
    private final AppConfig appConfig;

    @Autowired
    public SchedulerService(SchedulerFactoryBean schedulerFactoryBean, ApplicationContext applicationContext, AppConfig appConfig) {
        this.schedulerFactoryBean = schedulerFactoryBean;
        this.applicationContext = applicationContext;
        this.appConfig = appConfig;
    }

    public void scheduleTask(String taskId, LocalDateTime triggerTime, String payload) throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        String callbackUrl = appConfig.getCallback(); // Fetch from config

        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("taskId", taskId);
        jobDataMap.put("callbackUrl", callbackUrl);
        jobDataMap.put("payload", payload);
        // jobDataMap.put("applicationContext", applicationContext);

        JobDetail jobDetail = JobBuilder.newJob(CallbackJob.class)
                .withIdentity("job-" + taskId, "callback-jobs")
                .usingJobData(jobDataMap)
                .storeDurably()
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("trigger-" + taskId, "callback-triggers")
                .startAt(Date.from(triggerTime.atZone(ZoneId.systemDefault()).toInstant()))
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow())
                .build();

        scheduler.scheduleJob(jobDetail, trigger);
        logger.info("Task scheduled with ID: {} to trigger at: {}", taskId, triggerTime);
    }
}

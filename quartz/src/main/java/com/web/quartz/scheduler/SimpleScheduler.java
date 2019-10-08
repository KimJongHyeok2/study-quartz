package com.web.quartz.scheduler;

import javax.annotation.PostConstruct;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.stereotype.Component;

import com.web.quartz.job.SimpleJob;

@Component
public class SimpleScheduler {
	private SchedulerFactory factory;
	private Scheduler scheduler;
	
	@PostConstruct
	public void start() throws Exception {
		factory = new StdSchedulerFactory();
		scheduler = factory.getScheduler();
		scheduler.start();
		
		JobDetail job = JobBuilder.newJob(SimpleJob.class).withIdentity("simpleJob").build();
		
		Trigger trigger = TriggerBuilder.newTrigger().withSchedule(CronScheduleBuilder.cronSchedule("0/10 * * * * ?")).build();
		scheduler.scheduleJob(job, trigger);
	}
}
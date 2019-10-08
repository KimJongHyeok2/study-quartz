package com.web.quartz.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SimpleJob implements Job {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.info("Job Process...");
	}

}
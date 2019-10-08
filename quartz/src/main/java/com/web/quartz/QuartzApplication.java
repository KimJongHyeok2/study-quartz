package com.web.quartz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.web.quartz.scheduler.SimpleScheduler;

@SpringBootApplication
public class QuartzApplication {

	@Autowired
	private SimpleScheduler sheduler;
	
	public static void main(String[] args) {
		SpringApplication.run(QuartzApplication.class, args);
	}

}
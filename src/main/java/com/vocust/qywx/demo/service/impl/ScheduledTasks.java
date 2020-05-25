package com.vocust.qywx.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.vocust.qywx.demo.service.QychatService;

@Component
@Service
@EnableScheduling
public class ScheduledTasks implements CommandLineRunner {

	
	
	@Autowired
	private QychatService qychatService;

	@Async
	@Override
	public void run(String... args) throws Exception {
		qychatService.initQychatData();
	}
	
	

}
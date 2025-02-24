package com.airtribe.TaskTrackingAndManagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TaskTrackingAndManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskTrackingAndManagementApplication.class, args);
	}

}

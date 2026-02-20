package com.example.lms_submission_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@EnableFeignClients
@SpringBootApplication
public class LmsSubmissionServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LmsSubmissionServiceApplication.class, args);
	}

}

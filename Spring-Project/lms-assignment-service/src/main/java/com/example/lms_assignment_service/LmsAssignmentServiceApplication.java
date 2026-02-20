package com.example.lms_assignment_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.kafka.annotation.EnableKafka;

@EnableFeignClients
@EnableKafka
@SpringBootApplication
public class LmsAssignmentServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LmsAssignmentServiceApplication.class, args);
	}

}

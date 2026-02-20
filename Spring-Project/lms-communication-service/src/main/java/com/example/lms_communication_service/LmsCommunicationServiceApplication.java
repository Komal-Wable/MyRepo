package com.example.lms_communication_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@EnableFeignClients
@SpringBootApplication
public class LmsCommunicationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LmsCommunicationServiceApplication.class, args);
	}

}

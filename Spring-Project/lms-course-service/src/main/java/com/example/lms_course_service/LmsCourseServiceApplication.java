package com.example.lms_course_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;


@EnableKafka
@SpringBootApplication
public class LmsCourseServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LmsCourseServiceApplication.class, args);
	}

}

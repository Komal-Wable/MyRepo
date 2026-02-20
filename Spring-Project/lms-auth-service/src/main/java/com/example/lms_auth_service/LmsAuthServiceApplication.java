package com.example.lms_auth_service;

import com.example.lms_auth_service.entity.Role;
import com.example.lms_auth_service.entity.User;
import com.example.lms_auth_service.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
@EnableFeignClients
@SpringBootApplication
public class LmsAuthServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LmsAuthServiceApplication.class, args);
	}

	// 🔥 AUTO CREATE ADMIN
	@Bean
	CommandLineRunner createAdmin(UserRepository repo,
								  PasswordEncoder encoder) {

		return args -> {

			if (!repo.existsByEmail("admin@lms.com")) {

				User admin = new User();
				admin.setName("Principal");
				admin.setEmail("admin@lms.com");
				admin.setPassword(encoder.encode("admin123"));
				admin.setRole(Role.ROLE_ADMIN);
				admin.setApproved(true);
				admin.setBlocked(false);

				repo.save(admin);

				System.out.println("🔥 DEFAULT ADMIN CREATED 🔥");
			}
		};
	}
}

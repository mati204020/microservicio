package com.hospital.service_auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class ServiceAuthApplication {

	public static void main(String[] args) {
		System.setProperty("spring.autoconfigure.exclude", "org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration");
		SpringApplication.run(ServiceAuthApplication.class, args);
	}

}

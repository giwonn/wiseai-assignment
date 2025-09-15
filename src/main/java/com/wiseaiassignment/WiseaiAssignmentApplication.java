package com.wiseaiassignment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableJpaAuditing
@EnableAsync
@SpringBootApplication
public class WiseaiAssignmentApplication {

	public static void main(String[] args) {
		SpringApplication.run(WiseaiAssignmentApplication.class, args);
	}

}

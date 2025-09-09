package com.wiseaiassignment;

import org.springframework.boot.SpringApplication;

public class TestWiseaiAssignmentApplication {

	public static void main(String[] args) {
		SpringApplication.from(WiseaiAssignmentApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}

package com.wiseaiassignment.helper.config;

import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

@Configuration
public class MysqlTestContainersConfig {

	private static final MySQLContainer<?> mySqlContainer;

	static {
		mySqlContainer = new MySQLContainer<>(DockerImageName.parse("mysql:8.0"))
				.withDatabaseName("wise_ai")
				.withUsername("application")
				.withPassword("application")
				.withCommand(
						"--character-set-server=utf8mb4",
						"--collation-server=utf8mb4_general_ci",
						"--skip-character-set-client-handshake"
				);
		mySqlContainer.start();

		System.setProperty("spring.datasource.url", mySqlContainer.getJdbcUrl());
		System.setProperty("spring.datasource.username", mySqlContainer.getUsername());
		System.setProperty("spring.datasource.password", mySqlContainer.getPassword());
	}

}

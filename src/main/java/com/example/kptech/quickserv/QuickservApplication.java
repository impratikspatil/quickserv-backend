package com.example.kptech.quickserv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
@EnableMongoRepositories(basePackages = "com.example.kptech.quickserv.repository")

public class QuickservApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuickservApplication.class, args);
	}

}

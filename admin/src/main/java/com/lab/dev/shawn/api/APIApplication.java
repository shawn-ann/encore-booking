package com.lab.dev.shawn.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaAuditing
@EntityScan("com.lab.dev.shawn.api.entity")
@EnableJpaRepositories("com.lab.dev.shawn.api.repository")
public class APIApplication {

	public static void main(String[] args) {
		SpringApplication.run(APIApplication.class, args);
	}

}

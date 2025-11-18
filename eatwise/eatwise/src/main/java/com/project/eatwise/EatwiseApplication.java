package com.project.eatwise;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.project.eatwise", "global", "domain"})
@EnableJpaRepositories(basePackages = {"domain"})
@EntityScan(basePackages = {"domain"})
public class EatwiseApplication {

	public static void main(String[] args) {
		SpringApplication.run(EatwiseApplication.class, args);
	}

}

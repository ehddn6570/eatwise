package com.project.eatwise;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.project.eatwise", "domain", "global"})
public class EatwiseApplication {

	public static void main(String[] args) {
		SpringApplication.run(EatwiseApplication.class, args);
	}

}

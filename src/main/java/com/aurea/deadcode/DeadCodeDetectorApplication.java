package com.aurea.deadcode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource("/com/aurea/deadcode/integration.xml")
public class DeadCodeDetectorApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeadCodeDetectorApplication.class, args);
	}
}

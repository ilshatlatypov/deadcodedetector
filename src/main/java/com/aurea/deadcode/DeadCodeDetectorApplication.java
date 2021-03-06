package com.aurea.deadcode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource(locations = {
		"classpath:/com/aurea/deadcode/processing-flow.xml",
		"classpath:/com/aurea/deadcode/understand-service-config.xml"
})
public class DeadCodeDetectorApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeadCodeDetectorApplication.class, args);
	}
}

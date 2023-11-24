package com.javabootcamp.gym;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Gym CRM System",
				description = "API Definitions of the Gym crm Microservice",
				version = "1.0.0"
		)
)
public class GymApplication {

	public static void main(String[] args) {
		SpringApplication.run(GymApplication.class, args);
	}

}

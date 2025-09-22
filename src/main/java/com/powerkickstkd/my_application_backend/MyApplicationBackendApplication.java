package com.powerkickstkd.my_application_backend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MyApplicationBackendApplication {

	/*
		TODO
			Data Started: July 3
			Date Ended: ---
	*/
	public static void main(String[] args) {
		SpringApplication.run(MyApplicationBackendApplication.class, args);
	}

	// NOTE: For debugging purposes (COMMAND LINE FOR OUR DATABASE)
	@Bean
	public CommandLineRunner commandLineRunner() {

		return runner-> {

		};
	}
}

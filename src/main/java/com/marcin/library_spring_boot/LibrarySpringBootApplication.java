package com.marcin.library_spring_boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
public class LibrarySpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibrarySpringBootApplication.class, args);
	}

}

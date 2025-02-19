package com.example.CollegeBackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class CollegeBackendApplication {
	public static void main(String[] args)  {
		System.out.println("Server is running");
		SpringApplication.run(CollegeBackendApplication.class, args);
	}
}

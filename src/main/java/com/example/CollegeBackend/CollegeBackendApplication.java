package com.example.CollegeBackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class CollegeBackendApplication {
	private static final String URL = "jdbc:mysql://localhost:3306/college-portal";
	private static final String USER = "root";
	private static final String PASSWORD = "";


	public static void main(String[] args)  {
		System.out.println("Server is running");
		SpringApplication.run(CollegeBackendApplication.class, args);
	}
}

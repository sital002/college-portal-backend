package com.example.CollegeBackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@SpringBootApplication
public class CollegeBackendApplication {
	private static final String URL = "jdbc:mysql://localhost:3306/college-portal";
	private static final String USER = "root";
	private static final String PASSWORD = "";


	public static void main(String[] args)  {
		System.out.println("Server is running");
		SpringApplication.run(CollegeBackendApplication.class, args);
	}

	public static Connection connect() {
		try {
			return DriverManager.getConnection(URL, USER, PASSWORD);
		} catch (SQLException e) {
			throw new RuntimeException("Error connecting to the database \n", e);
		}
	}
}

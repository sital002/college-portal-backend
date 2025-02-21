package com.example.CollegeBackend;

import com.example.CollegeBackend.model.ApiResponse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@SpringBootApplication
public class CollegeBackendApplication {
	public static void main(String[] args)  {
		System.out.println("Server is running");
		SpringApplication.run(CollegeBackendApplication.class, args);
	}
}
@ControllerAdvice
class ValidationHandler{


	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse> handleGeneralException(Exception ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false,ex.getMessage()));
	}
}
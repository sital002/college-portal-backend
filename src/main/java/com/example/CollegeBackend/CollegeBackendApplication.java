package com.example.CollegeBackend;

import com.example.CollegeBackend.model.ApiResponse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;


@SpringBootApplication
public class CollegeBackendApplication {
	public static void main(String[] args)  {
		System.out.println("Server is running");
		SpringApplication.run(CollegeBackendApplication.class, args);
	}
}
@ControllerAdvice
class GlobalExceptionHandler{

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errorDetails = new HashMap<>();

		ex.getBindingResult().getAllErrors().forEach(error -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errorDetails.put(fieldName, errorMessage);
		});

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
	}
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse> handleGeneralException(Exception ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false,ex.getMessage()));
	}
}
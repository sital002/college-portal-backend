package com.example.CollegeBackend;

import com.example.CollegeBackend.model.ApiResponse;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;


@SpringBootApplication
public class CollegeBackendApplication {
	public static void main(String[] args)  {
		System.out.println("Server is running");
		SpringApplication.run(CollegeBackendApplication.class, args);
	}
}
@RestControllerAdvice
class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiError> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errorDetails = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach(error -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errorDetails.put(fieldName, errorMessage);
		});

		ApiError apiError = new ApiError(errorDetails);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ApiError> handleIllegalArgumentException(IllegalArgumentException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiError(ex.getMessage()));
	}
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiError> handleException(Exception ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiError(ex.getMessage()));
	}

}

@Getter
@Setter
class ApiError {
	private Object error;
	private boolean success;
	public ApiError(Object error) {
		this.error = error;
		this.success = false;
	}
}
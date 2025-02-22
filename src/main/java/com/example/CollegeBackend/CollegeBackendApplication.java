package com.example.CollegeBackend;

import com.example.CollegeBackend.utils.ApiError;
import jakarta.validation.constraints.Email;
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

	@ExceptionHandler(ApiError.class)
	public ResponseEntity<ApiErrorResponse> handleApiError(ApiError ex) {
		ApiErrorResponse errorResponse = new ApiErrorResponse(ex.getMessage());
		return  ResponseEntity.status(ex.getStatus()).body(errorResponse);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errorDetails = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach(error -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errorDetails.put(fieldName, errorMessage);
		});

		ApiErrorResponse apiError = new ApiErrorResponse(errorDetails);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
	}
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ApiErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiErrorResponse(ex.getMessage()));
	}
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiErrorResponse> handleException(Exception ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiErrorResponse(ex.getMessage()));
	}

}

@Getter
@Setter
class ApiErrorResponse {
	private Object error;
	private boolean success;
	public ApiErrorResponse(Object error) {
		this.error = error;
		this.success = false;
	}
}
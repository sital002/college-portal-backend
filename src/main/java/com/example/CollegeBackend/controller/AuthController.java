package com.example.CollegeBackend.controller;

import com.example.CollegeBackend.dto.SignUpRequest;
import com.example.CollegeBackend.dto.model.ApiResponse;
import com.example.CollegeBackend.dto.model.User;
import com.example.CollegeBackend.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> signUp(@Valid @RequestBody SignUpRequest request)  {
            if (userRepository.findByEmail(request.getEmail()) != null) {
                throw new IllegalArgumentException("Email already exists");
            }
            User user = new User(request.getFirstName(), request.getLastName(), request.getEmail(), request.getPassword());
            userRepository.save(user);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse(true, "User registered successfully"));
    }
}





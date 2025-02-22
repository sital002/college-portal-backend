package com.example.CollegeBackend.controller;

import com.example.CollegeBackend.dto.SignUpRequest;
import com.example.CollegeBackend.dto.model.ApiResponse;
import com.example.CollegeBackend.dto.model.User;
import com.example.CollegeBackend.repository.UserRepository;
import com.example.CollegeBackend.utils.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
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
    public ResponseEntity<ApiResponse> signUp(@Valid @RequestBody SignUpRequest request, HttpServletResponse response)  {
            if (userRepository.findByEmail(request.getEmail()) != null) {
                throw new IllegalArgumentException("Email already exists");
            }
            User user = new User(request.getFirstName(), request.getLastName(), request.getEmail(), request.getPassword());
            userRepository.save(user);

       String  accessToken=  JwtUtil.generateToken(request.getEmail());
       String  refreshToken=  JwtUtil.generateToken(request.getEmail());

        Cookie access_cookie = new Cookie("access_token", accessToken);
        access_cookie.setMaxAge(60 * 60 * 24);
        access_cookie.setHttpOnly(true);
        access_cookie.setSecure(true);
        access_cookie.setPath("/");

        Cookie refresh_cookie = new Cookie("refresh_token", refreshToken);
        refresh_cookie.setMaxAge(60 * 60 * 24);
        refresh_cookie.setHttpOnly(true);
        refresh_cookie.setSecure(true);
        refresh_cookie.setPath("/");

        response.addCookie(access_cookie);
        response.addCookie(refresh_cookie);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse(true, "User registered successfully"));
    }
}





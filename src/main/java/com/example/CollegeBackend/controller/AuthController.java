package com.example.CollegeBackend.controller;

import com.example.CollegeBackend.dto.*;
import com.example.CollegeBackend.model.User;
import com.example.CollegeBackend.repository.UserRepository;
import com.example.CollegeBackend.utils.ApiError;
import com.example.CollegeBackend.utils.JwtUtil;
import com.example.CollegeBackend.utils.PasswordHasher;
import org.springframework.http.ResponseCookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;




    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> signUp(@Valid @RequestBody SignUpRequest request, HttpServletResponse response) {
        if (userRepository.findByEmail(request.getEmail()) != null) {
            throw new ApiError(HttpStatus.BAD_REQUEST, "Email already exists");
        }
        try {
            String hashedPassword = PasswordHasher.hashPassword(request.getPassword());
            User user = new User(request.getFirstName(), request.getLastName(), request.getEmail(), hashedPassword,
                    Role.STUDENT);
            User newUser = userRepository.save(user);
            JwtPayload payload = new JwtPayload(newUser.getEmail(), newUser.getRole());

            String accessToken = JwtUtil.generateToken(payload);
            String refreshToken = JwtUtil.generateToken(payload);
            ResponseCookie access_cookie = generateCookie("access_token", accessToken);
            ResponseCookie refresh_cookie = generateCookie("refresh_token", refreshToken);
            response.addHeader("Set-Cookie", access_cookie.toString());
            response.addHeader("Set-Cookie", refresh_cookie.toString());
            HashMap<String, Object> token = new HashMap<>();
            token.put("access_token", accessToken);
            token.put("refresh_token", refreshToken);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse(true, "User registered successfully", token));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Error while hashing password");
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<ApiResponse> signIn(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        User userExists = userRepository.findByEmail(request.getEmail());
        if (userExists == null) {
            throw new ApiError(HttpStatus.NOT_FOUND, "Invalid email or password");
        }
        try {
            boolean isPasswordMatched = PasswordHasher.verifyPassword(request.getPassword(), userExists.getPassword());
            if (!isPasswordMatched) {
                throw new ApiError(HttpStatus.NOT_FOUND, "Invalid email or password");
            }
            System.out.println(userExists.getEmail() + " " + userExists.getRole());
            JwtPayload payload = new JwtPayload(userExists.getEmail(), userExists.getRole());

            String accessToken = JwtUtil.generateToken(payload);
            String refreshToken = JwtUtil.generateToken(payload);
            ResponseCookie access_cookie = generateCookie("access_token", accessToken);
            ResponseCookie refresh_cookie = generateCookie("refresh_token", refreshToken);
            response.addHeader("Set-Cookie", access_cookie.toString());
            response.addHeader("Set-Cookie", refresh_cookie.toString());

            HashMap<String, Object> token = new HashMap<>();
            token.put("access_token", accessToken);
            token.put("refresh_token", refreshToken);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ApiResponse(true, "Login successfully", token));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Error while hashing password");
        }
    }

    @GetMapping("/verify-token")
    public ResponseEntity<ApiResponse> verifyToken(@CookieValue String access_token,
            @CookieValue String refresh_token) {
        if (access_token == null || refresh_token == null) {
            throw new ApiError(HttpStatus.NOT_FOUND, "Token isn't provided");
        }
        JwtPayload isValidToken = JwtUtil.parseToken(access_token);
        if (isValidToken == null) {
            throw new ApiError(HttpStatus.NOT_FOUND, "Invalid access token");
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse(true, "Token verified successfully", isValidToken));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse> me(@RequestHeader("Authorization") String authorizationHeader) {

        authorizationHeader = authorizationHeader.replace("Bearer ", "");
        System.out.println(authorizationHeader);
        JwtPayload isValidToken = JwtUtil.parseToken(authorizationHeader);
        if (isValidToken == null) {
            throw new ApiError(HttpStatus.NOT_FOUND, "Invalid access token");
        }
        User user = userRepository.findByEmail(isValidToken.getEmail());
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(user));

    }

    public static ResponseCookie generateCookie(String name, String value) {
        return ResponseCookie.from(name, value)
                .maxAge(60 * 60 * 24)
                .httpOnly(true)
                .secure(true).sameSite("Lax")
                .path("/")
                .build();
    }
}

package com.example.CollegeBackend.controller;

import com.example.CollegeBackend.dto.*;
import com.example.CollegeBackend.model.User;
import com.example.CollegeBackend.repository.UserRepository;
import com.example.CollegeBackend.utils.ApiError;
import com.example.CollegeBackend.utils.JwtUtil;
import com.example.CollegeBackend.utils.PasswordHasher;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
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
                    request.getRole());
            User newUser = userRepository.save(user);
            JwtPayload payload = new JwtPayload(newUser.getEmail(), newUser.getRole(), newUser.getId());

            String accessToken = JwtUtil.generateToken(payload);
            String refreshToken = JwtUtil.generateToken(payload);
            Cookie access_cookie = generateCookie("access_token", accessToken);
            Cookie refresh_cookie = generateCookie("refresh_token", refreshToken);
            response.addCookie(access_cookie);
            response.addCookie(refresh_cookie);
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
            JwtPayload payload = new JwtPayload(userExists.getEmail(), userExists.getRole(), userExists.getId());
            String accessToken = JwtUtil.generateToken(payload);
            String refreshToken = JwtUtil.generateToken(payload);
            Cookie access_cookie = generateCookie("access_token", accessToken);
            Cookie refresh_cookie = generateCookie("refresh_token", refreshToken);
            response.addCookie(access_cookie);
            response.addCookie(refresh_cookie);
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
    public ResponseEntity<ApiResponse> me(HttpServletRequest request) {
        JwtPayload jwtPayload = (JwtPayload) request.getAttribute("jwtPayload");
        System.out.println(jwtPayload);
        if (jwtPayload == null) {
            throw new ApiError(HttpStatus.UNAUTHORIZED, "Access token is missing okay");
        }

        User user = userRepository.findByEmail(jwtPayload.getEmail());
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(user));

    }

    public static Cookie generateCookie(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(60 * 60 * 24);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        return cookie;
    }
}

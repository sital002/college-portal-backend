package com.example.CollegeBackend.controller;

import com.example.CollegeBackend.dto.LoginRequest;
import com.example.CollegeBackend.dto.SignUpRequest;
import com.example.CollegeBackend.dto.ApiResponse;
import com.example.CollegeBackend.model.User;
import com.example.CollegeBackend.repository.UserRepository;
import com.example.CollegeBackend.utils.JwtUtil;
import com.example.CollegeBackend.utils.PasswordHasher;
import jakarta.servlet.http.Cookie;
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
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> signUp(@Valid @RequestBody SignUpRequest request, HttpServletResponse response)  {
            if (userRepository.findByEmail(request.getEmail()) != null) {
                throw new IllegalArgumentException("Email already exists");
            }
            try{
            String hashedPassword = PasswordHasher.hashPassword(request.getPassword());
            User user = new User(request.getFirstName(), request.getLastName(), request.getEmail(), hashedPassword);
            User newUser =   userRepository.save(user);
            String  accessToken=  JwtUtil.generateToken(newUser.getEmail());
            String  refreshToken=  JwtUtil.generateToken(newUser.getEmail());
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
            catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                throw new IllegalArgumentException("Error while hashing password");
            }
    }
    @PostMapping("/signin")
    public  ResponseEntity<ApiResponse> signIn(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        User userExists = userRepository.findByEmail(request.getEmail());
        if(userExists == null){
            throw new IllegalArgumentException("Invalid email or password");
        }
        try{

        boolean isPasswordMatched =  PasswordHasher.verifyPassword(request.getPassword(),userExists.getPassword());
        if(!isPasswordMatched){
            throw new IllegalArgumentException("Invalid email or password");
        }
            String  accessToken=  JwtUtil.generateToken(userExists.getEmail());
            String  refreshToken=  JwtUtil.generateToken(userExists.getEmail());
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

            HashMap<Object, Object> token =  new HashMap<>();
            token.put("access_token", accessToken);
            token.put("refresh_token", refreshToken);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ApiResponse(true, "Login successfully",token));
        }
        catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new IllegalArgumentException("Error while hashing password");
        }
    }
}






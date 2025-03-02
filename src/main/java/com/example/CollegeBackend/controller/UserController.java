package com.example.CollegeBackend.controller;

import com.example.CollegeBackend.dto.ApiResponse;
import com.example.CollegeBackend.dto.JwtPayload;
import com.example.CollegeBackend.model.User;
import com.example.CollegeBackend.repository.UserRepository;
import com.example.CollegeBackend.utils.ApiError;
import com.example.CollegeBackend.utils.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;


    @GetMapping("/all")
    public @ResponseBody Iterable<User> getAll() {
        return userRepository.findAll();
    }

    @PostMapping("/update")
    public  ResponseEntity<ApiResponse> updateDetail(@Valid @RequestBody User request, @CookieValue String access_token , @CookieValue String refresh_token) {
        if(access_token == null && refresh_token == null) {
            throw new ApiError(HttpStatus.BAD_REQUEST,"Access token and refresh token are both required");
        }
        JwtPayload isValidToken = JwtUtil.parseToken(access_token);
        if(isValidToken == null ){
            throw new ApiError(HttpStatus.NOT_FOUND, "Invalid access token");
        }
        User user = userRepository.findByEmail(isValidToken.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhoneNumber(request.getPhoneNumber());
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(user));

    }

}



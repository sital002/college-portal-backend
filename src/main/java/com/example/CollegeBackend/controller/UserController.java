package com.example.CollegeBackend.controller;

import com.example.CollegeBackend.dto.ApiResponse;
import com.example.CollegeBackend.dto.JwtPayload;
import com.example.CollegeBackend.model.User;
import com.example.CollegeBackend.repository.UserRepository;
import com.example.CollegeBackend.utils.ApiError;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
    public ResponseEntity<ApiResponse> updateDetail(HttpServletRequest request, @Validated @RequestBody User input) {
        JwtPayload jwtPayload = (JwtPayload) request.getAttribute("jwtPayload");
        if (jwtPayload == null) {
            throw new ApiError(HttpStatus.UNAUTHORIZED, "Access token is missing");
        }

        User user = userRepository.findByEmail(jwtPayload.getEmail());
        user.setFirstName(input.getFirstName());
        user.setLastName(input.getLastName());
        user.setPhoneNumber(input.getPhoneNumber());
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(user));

    }

}

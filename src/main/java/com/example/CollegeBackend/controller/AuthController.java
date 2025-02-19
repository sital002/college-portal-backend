package com.example.CollegeBackend.controller;


import com.example.CollegeBackend.model.LoginRequest;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/signin")
    public String signIn(@RequestBody LoginRequest request){
        try{
        String email = request.getEmail();
        String password = request.getPassword();
        if(email.isEmpty() || password.isEmpty()){
            throw new Exception("Email or password is empty");
        }
        if(password.length() < 8){
            throw new Exception("Password must be at least 8 characters");
        }
        return "Signing in successfully";
        }
        catch (Exception e){
            return e.getMessage();
        }
    }
}

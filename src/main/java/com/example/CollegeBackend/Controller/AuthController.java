package com.example.CollegeBackend.Controller;


import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @GetMapping("/signin")
    public String signIn(){
        return "Sign in successful";
    }

    @PostMapping("/signup")
    public String signUp(@RequestBody String user){
        System.out.println(user);
        return "Sign up successful";
    }
}

package com.example.CollegeBackend.controller;

import com.example.CollegeBackend.model.User;
import com.example.CollegeBackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api") // Optional base path
public class UserController {

    @Autowired
    private UserRepository userRepository;


    @GetMapping("/all")
    public @ResponseBody Iterable<User> getAll() {
        return userRepository.findAll();
    }

    @GetMapping("/new")
    public @ResponseBody User newUser() {
        User user = new User("Ramesh","Bhandari","Tandi","trset");
        return userRepository.save(user);

    }

}



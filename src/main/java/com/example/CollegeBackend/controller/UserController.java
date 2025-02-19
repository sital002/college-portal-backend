package com.example.CollegeBackend.controller;

import com.example.CollegeBackend.model.User;
import com.example.CollegeBackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api") // Optional base path
public class UserController {

    private static final String URL = "jdbc:mysql://localhost:3306/college-portal";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    @Autowired
    private UserRepository userRepository;


    @GetMapping("/all")
    public @ResponseBody Iterable<User> getAll() {
        return userRepository.findAll();
    }

    @GetMapping("/new")
    public @ResponseBody User newUser() {
        User user = new User("Ramesh","Bhandari",40,"Tandi",988888887);
        return userRepository.save(user);

    }

}



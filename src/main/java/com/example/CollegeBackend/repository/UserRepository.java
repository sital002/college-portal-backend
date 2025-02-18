package com.example.CollegeBackend.repository;

import com.example.CollegeBackend.model.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository {
    User findByEmail(String email);
}

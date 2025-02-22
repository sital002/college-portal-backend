package com.example.CollegeBackend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtPayload {
    private String email;
    private Role role;

    public JwtPayload(String email, Role role) {
        this.email = email;
        this.role = role;
    }
}

package com.example.CollegeBackend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtPayload {
    private String email;
    private Role role;
    private Long id;

    public JwtPayload(String email, Role role, Long id) {
        this.email = email;
        this.role = role;
        this.id = id;
    }
}

package com.example.CollegeBackend.model;


import com.example.CollegeBackend.dto.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@Entity
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    @JsonIgnore
    private String password;
    private boolean emailVerified = false;
    private String profilePicture;
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Role role = Role.STUDENT;

    public User(String firstName, String lastName, String email, String password , Role role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User() {}
}

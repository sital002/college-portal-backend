package com.example.CollegeBackend.model;


import com.example.CollegeBackend.dto.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.UniqueElements;


@Setter
@Getter
@Entity
public class User {


    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    private String firstName;
    private String lastName;
    @UniqueElements
    private String email;
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

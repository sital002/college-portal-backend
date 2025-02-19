package com.example.CollegeBackend.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    private  int roll;
    private  String address;
    private  long primaryContact;


    public User(String firstName, String lastName, int roll, String address, long primaryContact) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.roll = roll;
        this.address = address;
        this.primaryContact = primaryContact;
    }

    public User() {

    }
}

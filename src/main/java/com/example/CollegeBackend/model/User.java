package com.example.CollegeBackend.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
public class User {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;

    @Setter
    private String firstName;
    @Setter
    private String lastName;
    @Setter
    private  int roll;
    @Setter
    private  String address;
    @Setter
    private  long primaryContact;


    public User(String firstName, String lastName, int roll, String address, long primaryContact) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.roll = roll;
        this.address = address;
        this.primaryContact = primaryContact;
        this.id = roll;
    }

}

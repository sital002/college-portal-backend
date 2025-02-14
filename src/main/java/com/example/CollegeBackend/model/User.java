package com.example.CollegeBackend.model;


public class User {

    private String name;
    private int roll;
    private  String firstName;
    private  String lastName;
    private  String address;
    private long primaryContact;

    public User( int roll,  String firstName, String lastName, String address, long primaryContact) {
        this.address = address;
        this.primaryContact = primaryContact;
        this.firstName = firstName;
        this.lastName = lastName;
        this.roll = roll;
    }

    public int getRoll() {
        return roll;
    }
    public void setRoll(int roll) {
        this.roll = roll;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public long getPrimaryContact() {
        return primaryContact;
    }
    public void setPrimaryContact(long primaryContact) {
        this.primaryContact = primaryContact;
    }

}
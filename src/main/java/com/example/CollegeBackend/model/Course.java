package com.example.CollegeBackend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String description;
    private String courseCode;

    public Course() {}
    public Course(String name, String description, String courseCode) {
        this.name = name;
        this.description = description;
        this.courseCode = courseCode;
    }

}

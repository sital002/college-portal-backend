package com.example.CollegeBackend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
public class IssuedBook {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Book book;

    @ManyToOne
    private User student;

    private Date issueDate;
    private Date dueDate;
    private boolean returned;
    public IssuedBook() {}

    public IssuedBook(Book book, User student,Date dueDate, boolean returned) {
        this.book = book;
        this.student = student;
        this.issueDate = new Date();
        this.dueDate = dueDate;
        this.returned = returned;
    }
}


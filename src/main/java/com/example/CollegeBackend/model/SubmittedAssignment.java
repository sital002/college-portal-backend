package com.example.CollegeBackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
public class SubmittedAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    private User student;

    @ManyToOne
    @JoinColumn(name = "assignment_id", referencedColumnName = "id")
    private Assignment assignment;

    private  String attachments;

    private Date submittedDate = new Date();

    public SubmittedAssignment() {}
    public SubmittedAssignment(User student, String attachments, Assignment assignment) {
        this.student = student;
        this.attachments = attachments;
        this.assignment = assignment;
    }
}


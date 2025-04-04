package com.example.CollegeBackend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Assignment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;
    private String description;
    private String deadLine;
    private String attachments;
    private String room;

    @ManyToOne
    @JoinColumn(name = "teacher_id", referencedColumnName = "id")
    private User teacher;

    public Assignment() {
    }

    public Assignment(String title, String description, String deadLine, String attachments, String room,
            User teacher) {
        this.title = title;
        this.description = description;
        this.deadLine = deadLine;
        this.attachments = attachments;
        this.room = room;
        this.teacher = teacher;
    }
}
package com.example.CollegeBackend.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User createdBy;

    private Date createdDate;

    public Notice(String title, String content ,User createdBy) {
        this.title = title;
        this.content = content;
        this.createdBy = createdBy;
        this.createdDate = new Date();
    }
public  Notice(){}


}

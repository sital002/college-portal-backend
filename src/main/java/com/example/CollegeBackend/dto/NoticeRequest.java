package com.example.CollegeBackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoticeRequest {

    @NotBlank(message = "title cannot be empty")
    @Size(min = 8, message = "title must be at least 8 characters")
    @Size(max = 64,message = "title must be atmost 64 characters long")
    private String title;


    @NotBlank(message = "content cannot be empty")
    @Size(min = 8, message = "content must be at least 8 characters")
    @Size(max = 1000,message = "content must be atmost 1000 characters long")
    private String content;

    public NoticeRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }

}

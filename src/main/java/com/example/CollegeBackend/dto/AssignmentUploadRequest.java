package com.example.CollegeBackend.dto;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssignmentUploadRequest {
    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 50, message = "Title must be between 3 and 50 characters")
    private String title;
    @NotBlank(message = "Description is required")
    @Size(min = 10, max = 500, message = "Description must be between 10 and 500 characters")
    private String description;

    @NotBlank(message = "Deadline is required")
    @Future(message = "Deadline must be a future date")
    private String deadLine;

    @NotBlank(message = "Room is required")
    private String room;

    private MultipartFile file;

    public AssignmentUploadRequest() {
    }

    public AssignmentUploadRequest(String title, String description, String deadLine,
            String room) {
        this.title = title;
        this.description = description;
        this.deadLine = deadLine;
        this.room = room;
    }

}

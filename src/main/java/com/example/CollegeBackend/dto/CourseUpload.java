package com.example.CollegeBackend.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseUpload {
    @NotBlank(message = "Name cannot be black")
    @Size(min = 3,message = "Name must be within 3 and 64",max = 64)
    private String name;

    @NotBlank(message = "Description cannot be black")
    @Size(min = 3,message = "Description must be within 3 and 400",max = 400)
    private String description;

    @NotBlank(message = "CourseCode cannot be black")
    @Size(min = 3,message = "CourseCode must be within 3 and 64",max = 64)
    private String cousreCode;
}

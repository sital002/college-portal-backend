package com.example.CollegeBackend.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookRequest {
    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 50, message = "Title must be between 3 and 50 characters")
    private String title;

    @NotBlank( message = "Author is required")
    @Size(min = 3, max = 50, message = "Author must be between 3 and 50 characters")
    private String author;

    @NotBlank(message = "Isbn is required")
    @Size(min = 3, max = 50, message = "Isbn must be between 3 and 50 characters")
    private String isbn;

    @NotBlank(message = "Available is required")
    private boolean available;

    public BookRequest(String title, String author, String isbn, boolean available) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.available = available;
    }
}

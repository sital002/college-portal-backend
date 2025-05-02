package com.example.CollegeBackend.repository;

import com.example.CollegeBackend.model.Book;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<Book, Long> {
    List<Book> findByAvailable(boolean available);

    List<Book> findByAuthor(String author);

    List<Book> findByTitleContainingIgnoreCase(String title);

    List<Book> findByAuthorContainingIgnoreCase(String author);

    List<Book> findByIsbnContainingIgnoreCase(String isbn);

    List<Book> findByAvailableAndTitleContainingIgnoreCase(boolean available, String title);

    List<Book> findByAvailableAndAuthorContainingIgnoreCase(boolean available, String author);

    List<Book> findByAvailableAndIsbnContainingIgnoreCase(boolean available, String isbn);
}
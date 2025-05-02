package com.example.CollegeBackend.repository;

import com.example.CollegeBackend.model.IssuedBook;
import org.springframework.data.repository.CrudRepository;

public interface IssuedBookRepository extends CrudRepository<IssuedBook, Integer> {
}

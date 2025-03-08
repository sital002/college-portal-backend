package com.example.CollegeBackend.repository;

import com.example.CollegeBackend.model.Assignment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignmentRepository extends CrudRepository<Assignment, Long> {
}
package com.example.CollegeBackend.repository;

import com.example.CollegeBackend.model.Assignment;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignmentRepository extends CrudRepository<Assignment, Long> {

    List<Assignment> findByTeacherId(Long teacher_id);

}
package com.example.CollegeBackend.repository;

import com.example.CollegeBackend.model.Course;
import org.springframework.data.repository.CrudRepository;

public interface CourseRepository extends CrudRepository<Course, Long> {
}

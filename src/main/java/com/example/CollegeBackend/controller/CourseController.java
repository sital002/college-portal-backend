package com.example.CollegeBackend.controller;


import com.example.CollegeBackend.dto.ApiResponse;
import com.example.CollegeBackend.dto.CourseUpload;
import com.example.CollegeBackend.dto.JwtPayload;
import com.example.CollegeBackend.dto.Role;
import com.example.CollegeBackend.model.Course;
import com.example.CollegeBackend.model.User;
import com.example.CollegeBackend.repository.CourseRepository;
import com.example.CollegeBackend.repository.UserRepository;
import com.example.CollegeBackend.utils.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/course")
public class CourseController {

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/new")
    public ResponseEntity<ApiResponse> addCourse(HttpServletRequest request, @RequestBody CourseUpload course) {
        JwtPayload jwtPayload = (JwtPayload) request.getAttribute("jwtPayload");
        if(jwtPayload == null) {
            throw  new ApiError(HttpStatus.UNAUTHORIZED,"Please login first");
        }
        User user = userRepository.findById(jwtPayload.getId()).orElseThrow(()-> new ApiError(HttpStatus.UNAUTHORIZED,"User not found"));
        if(!user.getRole().equals(Role.ADMIN)){
            throw new ApiError(HttpStatus.UNAUTHORIZED,"You are not allowed to add course");
        }
        Course newCourse = courseRepository.save(new Course(course.getName(),course.getDescription(),course.getCousreCode()));
        return  ResponseEntity.ok(new ApiResponse(newCourse));

    }
    @PutMapping("/{id:.+}")
    public ResponseEntity<ApiResponse> updateCourse(HttpServletRequest request, @RequestBody CourseUpload body ,@PathVariable Long id) {
        JwtPayload jwtPayload = (JwtPayload) request.getAttribute("jwtPayload");
        if(jwtPayload == null) {
            throw  new ApiError(HttpStatus.UNAUTHORIZED,"Please login first");
        }
        Course course = courseRepository.findById(id).orElseThrow(()-> new ApiError(HttpStatus.NOT_FOUND,"Course not found"));
        User user = userRepository.findById(jwtPayload.getId()).orElseThrow(()-> new ApiError(HttpStatus.UNAUTHORIZED,"User not found"));
        if(!user.getRole().equals(Role.ADMIN)){
            throw new ApiError(HttpStatus.UNAUTHORIZED,"You are not allowed to add course");
        }
        course.setName((body.getName()));
        course.setDescription((body.getDescription()));
        course.setCourseCode((body.getCousreCode()));
       Course updatedCourse =  courseRepository.save(course);
        return  ResponseEntity.ok(new ApiResponse(updatedCourse));
    }

    @DeleteMapping("/{id:.+}")
    public ResponseEntity<ApiResponse> deleteCourse(HttpServletRequest request ,@PathVariable Long id) {
        JwtPayload jwtPayload = (JwtPayload) request.getAttribute("jwtPayload");
        if(jwtPayload == null) {
            throw  new ApiError(HttpStatus.UNAUTHORIZED,"Please login first");
        }
        if(!jwtPayload.getRole().equals(Role.ADMIN)){
            throw new ApiError(HttpStatus.UNAUTHORIZED,"You are not allowed to delete course");
        }
        Course courseExists = courseRepository.findById(id).orElseThrow(()-> new ApiError(HttpStatus.NOT_FOUND,"Course not found"));
        courseRepository.deleteById(courseExists.getId());
        return  ResponseEntity.ok(new ApiResponse("Course deleted Successfully"));
    }

    @GetMapping("/")
    public ResponseEntity<ApiResponse> getAllCourses(HttpServletRequest request) {
        JwtPayload jwtPayload = (JwtPayload) request.getAttribute("jwtPayload");
        if(jwtPayload == null) {
            throw  new ApiError(HttpStatus.UNAUTHORIZED,"Please login first");
        }
        if(!jwtPayload.getRole().equals(Role.ADMIN)){
            throw new ApiError(HttpStatus.UNAUTHORIZED,"You are not allowed to view courses");
        }
        List<Course> courses = (List<Course>) courseRepository.findAll();
        return  ResponseEntity.ok(new ApiResponse(courses));
    }

}

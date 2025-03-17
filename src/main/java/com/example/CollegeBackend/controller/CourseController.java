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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}

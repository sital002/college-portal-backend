package com.example.CollegeBackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.example.CollegeBackend.service.AssignmentService;
import com.example.CollegeBackend.utils.ApiError;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import com.example.CollegeBackend.dto.ApiResponse;
import com.example.CollegeBackend.dto.JwtPayload;
import com.example.CollegeBackend.model.Assignment;

@RestController
@RequestMapping("/api/v1/assignments")
public class AssignmentController {

    @Autowired
    private AssignmentService assignmentService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse> uploadAssignment(HttpServletRequest request,
            @RequestParam("file") MultipartFile file) {
        try {
            JwtPayload jwtPayload = (JwtPayload) request.getAttribute("jwtPayload");
            if (jwtPayload == null) {
                return ResponseEntity.status(401).body(new ApiResponse("Access token is missing"));
            }
            assignmentService.saveFile(file);
            return ResponseEntity.ok(new ApiResponse("File uploaded successfully"));
        } catch (Exception e) {
            throw new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Error while uploading file");
        }
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createAssignment(HttpServletRequest request,
            @RequestParam("title") @NotBlank(message = "Title is required") @Size(min = 3, max = 50) String title,
            @RequestParam("description") @NotBlank(message = "Description is required") @Size(min = 10, max = 500) String description,
            @RequestParam("deadLine") @NotBlank String deadLine,
            @RequestParam("room") @NotBlank String room,
            @RequestParam("file") MultipartFile file) {
        JwtPayload jwtPayload = (JwtPayload) request.getAttribute("jwtPayload");
        if (jwtPayload == null) {
            throw new ApiError(HttpStatus.UNAUTHORIZED, "Access token is missing");
        }
        try {
            String filePath = assignmentService.saveFile(file);
            Assignment newAssignment = new Assignment(title, description, deadLine, room, filePath);
            Assignment createdAssignment = assignmentService.createAssignment(newAssignment);
            return ResponseEntity.ok(new ApiResponse(createdAssignment));
        } catch (Exception e) {
            throw new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Error while creating assignment: " + e.getMessage());
        }
    }
}

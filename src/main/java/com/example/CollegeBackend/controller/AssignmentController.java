package com.example.CollegeBackend.controller;

import com.example.CollegeBackend.dto.Role;
import com.example.CollegeBackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import com.example.CollegeBackend.model.User;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/v1/assignments")
public class AssignmentController {

    @Autowired
    private AssignmentService assignmentService;
    @Autowired
    private UserRepository userRepository;

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

        User user = userRepository.findByEmail(jwtPayload.getEmail());

        Role userRole = user.getRole();
        System.out.println(userRole);
        if (userRole.name().equals("STUDENT")) {
            throw new ApiError(HttpStatus.FORBIDDEN, "You aren't allowed to create assignment");
        }
        try {
            String filePath = assignmentService.saveFile(file);
            Assignment createdAssignment = assignmentService
                    .createAssignment(
                            new Assignment(title, description, deadLine, filePath, room, user));
            return ResponseEntity.ok(new ApiResponse(createdAssignment));
        } catch (Exception e) {
            throw new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Error while creating assignment: " + e.getMessage());
        }
    }

    @GetMapping("/view/{teacherId:.+}")
    public ResponseEntity<ApiResponse> viewAssignments(HttpServletRequest request, @PathVariable Long teacherId) {
        JwtPayload jwtPayload = (JwtPayload) request.getAttribute("jwtPayload");
        if (jwtPayload == null) {
            throw new ApiError(HttpStatus.UNAUTHORIZED, "Access token is missing");
        }
        try {
            return ResponseEntity.ok(new ApiResponse(assignmentService.viewAssignments(teacherId)));
        } catch (Exception e) {
            throw new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Error while fetching assignments: " + e.getMessage());
        }
    }

    private final Path uploadsFolder = Paths.get("uploads");

    @GetMapping("/uploads/{filename:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) throws IOException {
        Path filePath = uploadsFolder.resolve(filename).normalize();
        if (Files.exists(filePath) && Files.isReadable(filePath)) {
            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            Resource resource = new UrlResource(filePath.toUri());
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);
        } else {
            throw new IOException("File not found");
        }
    }

}

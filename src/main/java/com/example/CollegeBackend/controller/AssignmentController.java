package com.example.CollegeBackend.controller;

import com.example.CollegeBackend.dto.Role;
import com.example.CollegeBackend.model.SubmittedAssignment;
import com.example.CollegeBackend.repository.AssignmentRepository;
import com.example.CollegeBackend.repository.SubmittedAssignmentRepository;
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
    @Autowired
    private AssignmentRepository assignmentRepository;
    @Autowired
    private SubmittedAssignmentRepository submittedAssignmentRepository;

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
        if (userRole.equals(Role.STUDENT)) {
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

    @GetMapping("/view" )
    public ResponseEntity<ApiResponse> viewAssignments(HttpServletRequest request) {
        JwtPayload jwtPayload = (JwtPayload) request.getAttribute("jwtPayload");
        if (jwtPayload == null) {
            throw new ApiError(HttpStatus.UNAUTHORIZED, "Access token is missing");
        }
        System.out.println(jwtPayload.getId());
        try {
            return ResponseEntity.ok(new ApiResponse(assignmentService.viewAssignments(jwtPayload.getId())));
        } catch (Exception e) {
            throw new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Error while fetching assignments: " + e.getMessage());
        }
    }

    @PutMapping("/update/{id:.+}")
    public ResponseEntity<ApiResponse> updateAssignments(HttpServletRequest request, @PathVariable Long id ,
                        @RequestParam("title") @NotBlank(message = "Title is required") @Size(min = 3, max = 50) String title,
                                                         @RequestParam("description") @NotBlank(message = "Description is required") @Size(min = 10, max = 500) String description,
                                                         @RequestParam("deadLine") @NotBlank String deadLine,
                                                         @RequestParam("room") @NotBlank String room,
                                                         @RequestParam("file") MultipartFile file){

        JwtPayload jwtPayload = (JwtPayload) request.getAttribute("jwtPayload");
        if (jwtPayload == null) {
            throw new ApiError(HttpStatus.UNAUTHORIZED, "Access token is missing");
        }
        User user = userRepository.findByEmail(jwtPayload.getEmail());
        Role userRole = user.getRole();
        if (userRole.equals(Role.STUDENT)) {
            throw new ApiError(HttpStatus.FORBIDDEN, "You aren't allowed to update assignments");
        }
        Assignment assignmentExists = assignmentRepository.findById(id).orElseThrow();
        if(!assignmentExists.getTeacher().getId().equals(user.getId())){
            throw new ApiError(HttpStatus.FORBIDDEN, "You are not allowed to update assignments");
        }
        try{
            String filePath = assignmentService.saveFile(file);
            assignmentExists.setTitle(title);
            assignmentExists.setDescription(description);
            assignmentExists.setDeadLine(deadLine);
            assignmentExists.setRoom(room);
            assignmentExists.setAttachments(filePath);
            Assignment updatedAssignment = assignmentService.updateAssignment(assignmentExists);
            return ResponseEntity.ok(new ApiResponse(updatedAssignment));

        }
        catch(Exception e){
            throw new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Error while updating assignments: " + e.getMessage());
        }
    }

    @GetMapping("/single/{id:.+}")
    public ResponseEntity<ApiResponse> singleAssignment(HttpServletRequest request, @PathVariable Long id){
        JwtPayload jwtPayload = (JwtPayload) request.getAttribute("jwtPayload");
        if (jwtPayload == null) {
            throw new ApiError(HttpStatus.UNAUTHORIZED, "Access token is missing");
        }
        Assignment assignmentExists = assignmentRepository.findById(id).orElseThrow(()->new ApiError(HttpStatus.NOT_FOUND, "Assignment not found"));
        User user = userRepository.findByEmail(jwtPayload.getEmail());
        Role userRole = user.getRole();
        if(userRole.equals(Role.STUDENT)){
            throw new ApiError(HttpStatus.FORBIDDEN, "You aren't allowed to view assignment");
        }
        if(!assignmentExists.getTeacher().getId().equals(user.getId())){
            throw new ApiError(HttpStatus.FORBIDDEN, "You are only allowed to view assignments created by you");
        }
        return ResponseEntity.ok(new ApiResponse(assignmentExists));
    }

    @DeleteMapping("/single/{id:.+}")
    private ResponseEntity<ApiResponse> deleteAssignment(HttpServletRequest request, @PathVariable Long id){
        JwtPayload jwtPayload = (JwtPayload) request.getAttribute("jwtPayload");
        if (jwtPayload == null) {
            throw new ApiError(HttpStatus.UNAUTHORIZED, "Access token is missing");
        }
        User user = userRepository.findByEmail(jwtPayload.getEmail());
        Role userRole = user.getRole();
        if(userRole.equals(Role.STUDENT)){
            throw new ApiError(HttpStatus.FORBIDDEN, "You aren't allowed to delete assignment");
        }
        Assignment assignmentExists = assignmentRepository.findById(id).orElseThrow(()->new ApiError(HttpStatus.NOT_FOUND, "Assignment not found"));
        if(!assignmentExists.getTeacher().getId().equals(user.getId())){
            throw new ApiError(HttpStatus.FORBIDDEN, "You are only allowed to delete assignments created by you");
        }
        assignmentRepository.delete(assignmentExists);
        return ResponseEntity.ok(new ApiResponse("Assignment deleted"));
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


    @PostMapping("/submit/{assignmentId:.+}")
    public ResponseEntity<ApiResponse> submitAssignment(HttpServletRequest request, @RequestParam("room") @NotBlank String room,@RequestParam("file") MultipartFile file , @PathVariable Long assignmentId){
        JwtPayload jwtPayload = (JwtPayload) request.getAttribute("jwtPayload");
        if (jwtPayload == null) {
            throw new ApiError(HttpStatus.UNAUTHORIZED, "Access token is missing");
        }
        User user = userRepository.findById(jwtPayload.getId()).orElseThrow(()->new ApiError(HttpStatus.NOT_FOUND, "User not found"));
        if(!user.getRole().equals(Role.STUDENT)){
            throw new ApiError(HttpStatus.FORBIDDEN, "You aren't allowed to submit assignments");
        }
        Assignment assignment = assignmentRepository.findById(assignmentId).orElseThrow(()->new ApiError(HttpStatus.NOT_FOUND, "Assignment not found"));

        try{
            String assignmentUrl = assignmentService.saveFile(file);
            SubmittedAssignment newSubmittetdAssignment = submittedAssignmentRepository.save(new SubmittedAssignment(user,assignmentUrl,assignment));
            return ResponseEntity.ok(new ApiResponse(newSubmittetdAssignment));

        }catch (Exception e){
            throw new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Error while submitting assignment");
        }
    }

}

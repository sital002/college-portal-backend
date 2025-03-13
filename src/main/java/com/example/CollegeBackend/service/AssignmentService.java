package com.example.CollegeBackend.service;

import com.example.CollegeBackend.model.Assignment;
import com.example.CollegeBackend.repository.AssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.nio.file.StandardCopyOption;

@Service
public class AssignmentService {

    @Autowired
    private AssignmentRepository assignmentRepository;

    private final String uploadDir = "uploads/";


    public String saveFile(MultipartFile file) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String originalFilename = file.getOriginalFilename();
        String newFilename = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "_" + originalFilename;
        Path filePath = uploadPath.resolve(newFilename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        return filePath.toString();
    }

    public Assignment createAssignment(Assignment assignment) {
        return assignmentRepository.save(assignment);
    }

    public List<Assignment> viewAssignments(Long teacherId) {
        return assignmentRepository.findByTeacherId(teacherId);
    }
    public  Assignment updateAssignment(Assignment assignment) {
        return assignmentRepository.save(assignment);
    }
}
package com.example.CollegeBackend.controller;

import com.example.CollegeBackend.dto.ApiResponse;
import com.example.CollegeBackend.dto.JwtPayload;
import com.example.CollegeBackend.dto.NoticeRequest;
import com.example.CollegeBackend.dto.Role;
import com.example.CollegeBackend.model.Notice;
import com.example.CollegeBackend.model.User;
import com.example.CollegeBackend.repository.NoticeRepository;
import com.example.CollegeBackend.repository.UserRepository;
import com.example.CollegeBackend.utils.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/notice")
public class NoticeController {

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private UserRepository userRepository;


    @PostMapping("/new")
    public ResponseEntity<ApiResponse> newNotice(HttpServletRequest request, @RequestBody NoticeRequest notice) {
        JwtPayload jwtPayload = (JwtPayload) request.getAttribute("jwtPayload");
        if(jwtPayload == null) {
            throw  new ApiError(HttpStatus.UNAUTHORIZED,"You need to be logged in");
        }
        User user = userRepository.findById(jwtPayload.getId()).orElseThrow(()-> new ApiError(HttpStatus.UNAUTHORIZED,"User not found"));
        if(!user.getRole().equals(Role.ADMIN)){
            throw new ApiError(HttpStatus.UNAUTHORIZED,"You are not allowed to publish notice");
        }
        Notice newNotice = noticeRepository.save(new Notice(notice.getTitle(),notice.getContent(),user));
        return  ResponseEntity.ok(new ApiResponse(newNotice));
    }

    @GetMapping("/")
    public ResponseEntity<ApiResponse> getAllNotice(HttpServletRequest request) {
        JwtPayload jwtPayload = (JwtPayload) request.getAttribute("jwtPayload");
        if(jwtPayload == null) {
            throw  new ApiError(HttpStatus.UNAUTHORIZED,"You need to be logged in");
        }
        List<Notice> notices = (List<Notice>) noticeRepository.findAll();
        return ResponseEntity.ok(new ApiResponse(notices));
    }

}

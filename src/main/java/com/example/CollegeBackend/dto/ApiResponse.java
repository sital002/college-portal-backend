package com.example.CollegeBackend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponse {
    private boolean success;
    private String message;
    private Object data;
    public ApiResponse( boolean success ,String message,Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }
    public ApiResponse(Object data) {
        this.success = true;
        this.data = data;
    }
    public ApiResponse(String message) {
        this.success = true;
        this.message = message;
    }

}

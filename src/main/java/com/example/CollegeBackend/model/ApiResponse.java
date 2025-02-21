package com.example.CollegeBackend.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponse {
    private boolean success;
    private String message;
    private Object data;

    public ApiResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.data = null;
    }

    public ApiResponse( boolean success ,String message,Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

}

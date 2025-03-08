package com.example.CollegeBackend.middleware;

import com.example.CollegeBackend.dto.JwtPayload;
import com.example.CollegeBackend.utils.JwtUtil;

public class Authentication {
    public static boolean authenticate(String token) {
        JwtPayload jwtPayload = JwtUtil.parseToken(token);
        return jwtPayload != null;
    }

    public static JwtPayload getJwtPayload(String token) {
        return JwtUtil.parseToken(token);
    }
}

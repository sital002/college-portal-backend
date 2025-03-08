package com.example.CollegeBackend.middleware;

import com.example.CollegeBackend.dto.JwtPayload;
import com.example.CollegeBackend.utils.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;

public class AuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        String accessToken = null;

        if (request.getCookies() != null) {
            Cookie accessCookie = Arrays.stream(request.getCookies())
                    .filter(cookie -> "access_token".equals(cookie.getName()))
                    .findFirst()
                    .orElse(null);
            if (accessCookie != null) {
                accessToken = accessCookie.getValue();
            }
        }

        if (accessToken == null) {
            String authorizationHeader = request.getHeader("Authorization");
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                accessToken = authorizationHeader.replace("Bearer ", "");
            }
        }

        if (accessToken != null) {
            JwtPayload jwtPayload = JwtUtil.parseToken(accessToken);
            if (jwtPayload == null) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write("Invalid access token");
                return;
            }
            request.setAttribute("jwtPayload", jwtPayload);
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Access token is missing");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
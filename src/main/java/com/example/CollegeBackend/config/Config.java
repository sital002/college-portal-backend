package com.example.CollegeBackend.config;

import java.util.List;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.CollegeBackend.middleware.AuthenticationFilter;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.lang.NonNull;

@Configuration
public class Config {

    @Bean
    public FilterRegistrationBean<AuthenticationFilter> authenticationFilter() {
        FilterRegistrationBean<AuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new AuthenticationFilter());
        registrationBean.addUrlPatterns("/api/v1/*");
        registrationBean.setUrlPatterns(List.of("/api/v1/*"));
        registrationBean.setFilter(new AuthenticationFilter() {
            @Override   
            protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
                String method = request.getMethod();
                if ("OPTIONS".equalsIgnoreCase(method)) {
                    return true; // Allow all preflight requests to skip auth
                }
                String path = request.getRequestURI();
                String[] excludeUrls = {
                        "/api/v1/auth/signin",
                        "/api/v1/auth/signup",
                        "/api/v1/assignments/uploads"
                };
                for (String url : excludeUrls) {
                    if (path.startsWith(url)) {
                        return true;
                    }
                }
                return false;
            }
        });
        return registrationBean;
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOriginPatterns("http://localhost:3000", "http://localhost:8081",
                                "http://localhost:5173")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }

}
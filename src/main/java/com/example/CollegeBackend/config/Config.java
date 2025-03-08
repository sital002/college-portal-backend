package com.example.CollegeBackend.config;

import java.util.Arrays;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.CollegeBackend.middleware.AuthenticationFilter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.lang.NonNull;

@Configuration
public class Config {

    @Bean
    public FilterRegistrationBean<AuthenticationFilter> authenticationFilter() {
        FilterRegistrationBean<AuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new AuthenticationFilter());
        registrationBean.addUrlPatterns("/api/v1/*");
        registrationBean.setUrlPatterns(Arrays.asList("/api/v1/*"));
        registrationBean.setFilter(new AuthenticationFilter() {
            @Override
            protected boolean shouldNotFilter(@NonNull HttpServletRequest request) throws ServletException {
                String path = request.getRequestURI();
                return path.startsWith("/api/v1/auth/signin") || path.startsWith("/api/v1/auth/signup");
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
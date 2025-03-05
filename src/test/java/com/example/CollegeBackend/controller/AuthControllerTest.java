package com.example.CollegeBackend.controller;

import com.example.CollegeBackend.dto.LoginRequest;
import com.example.CollegeBackend.dto.SignUpRequest;
import com.example.CollegeBackend.model.User;
import com.example.CollegeBackend.repository.UserRepository;
import com.example.CollegeBackend.utils.PasswordHasher;
import com.example.CollegeBackend.dto.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)

class AuthControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockitoBean
        private UserRepository userRepository;

        @MockitoBean
        private PasswordHasher passwordHasher;

        @InjectMocks
        private AuthController authController;

        @BeforeEach
        void setUp() {
                MockitoAnnotations.openMocks(this);
                this.mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
        }

        @Test
        void testSignUp() throws Exception {
                SignUpRequest signUpRequest = new SignUpRequest("John", "Doe", "john.doe@example.com", "password");
                when(userRepository.findByEmail(signUpRequest.getEmail())).thenReturn(null);
                when(userRepository.save(any(User.class))).thenReturn(new User());

                mockMvc.perform(post("/api/v1/auth/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"firstName\":\"John\", \"lastName\":\"Doe\", \"email\":\"john.doe@example.com\", \"password\":\"password\"}"))
                                .andExpect(status().isCreated());
        }

        @Test
        void testSignIn() throws Exception {
                LoginRequest loginRequest = new LoginRequest("john.doe@example.com", "password");
                User user = new User("John", "Doe", "john.doe@example.com", passwordHasher.hashPassword("password"),
                                Role.STUDENT);
                when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(user);
                when(passwordHasher.verifyPassword(loginRequest.getPassword(), user.getPassword())).thenReturn(true);

                mockMvc.perform(post("/api/v1/auth/signin")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"email\":\"john.doe@example.com\", \"password\":\"password\"}"))
                                .andExpect(status().isOk());
        }

        @Test
        void testSignInInvalidCredentials() throws Exception {
                LoginRequest loginRequest = new LoginRequest("john.doe@example.com", "wrongpassword");
                User user = new User("John", "Doe", "john.doe@example.com",
                                passwordHasher.hashPassword("password"),
                                Role.STUDENT);
                when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(user);
                when(passwordHasher.verifyPassword(loginRequest.getPassword(), user.getPassword()))
                                .thenReturn(false);

                mockMvc.perform(post("/api/v1/auth/signin")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"email\":\"john.doe@example.com\", \"password\":\"wrongpassword\"}"))
                                .andExpect(status().isNotFound());
        }
}
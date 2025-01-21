package com.dp.authservice.controller;

import com.dp.authservice.config.SecurityConfig;
import com.dp.authservice.exception.UserNotFoundException;
import com.dp.authservice.request.UserRequest;
import com.dp.authservice.response.UserResponse;
import com.dp.authservice.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.mockito.Mock;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    private UserRequest userRequest;
    private UserResponse userResponse;

    @BeforeEach
    void setup() {
        userRequest = new UserRequest("test@test.com", "password");

        userResponse = new UserResponse();
        userResponse.setId(1L);
        userResponse.setEmail("test@test.com");
        userResponse.setCreatedAt(LocalDateTime.now());
    }

    @Test
    @WithMockUser
    void createUser_ValidInput_ReturnsCreatedUser() throws Exception {
        when(userService.createUser(any(UserRequest.class))).thenReturn(userResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("test@test.com"));
    }

    @Test
    @WithMockUser
    void createUser_InvalidInput_ReturnsBadRequest() throws Exception {
        UserRequest invalidRequest = new UserRequest("invalid_email", null);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void createUser_UserServiceThrowsException_ReturnsInternalServerError() throws Exception {
        when(userService.createUser(any(UserRequest.class))).thenThrow(new RuntimeException("Database error"));
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Internal Server Error"));
    }

    @Test
    @WithMockUser
    void getUserById_ValidId_ReturnsUser() throws Exception{
        when(userService.getUserById(1L)).thenReturn(userResponse);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("test@test.com"));
    }

//    @Test
//    @WithMockUser
//    void getUserById_InvalidId_ThrowsResourceNotFound() throws Exception {
//        when(userService.getUserById(2L)).thenThrow(new UserNotFoundException("User not found with id: 2"));
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/2"))
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.error").value("Resource Not Found"));
//    }

    @Test
    @WithMockUser
    void getUserById_UserServiceThrowsException_ReturnsInternalServerError() throws Exception {
        when(userService.getUserById(2L)).thenThrow(new RuntimeException("Database error"));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/2"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Internal Server Error"));
    }

//    @Test
//    @WithMockUser
//    public void getUserById_invalidInput_returnsNotFound() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/invalid"))
//                .andExpect(status().isNotFound());
//    }
}

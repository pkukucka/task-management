package com.jumpsoft.taskmanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.jumpsoft.taskmanagement.dto.user.User;
import com.jumpsoft.taskmanagement.dto.user.UserCreateRequest;
import com.jumpsoft.taskmanagement.dto.user.UserUpdateRequest;
import com.jumpsoft.taskmanagement.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for UserController.
 * Tests all endpoints: GET, POST, PATCH, DELETE.
 */

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    private UserCreateRequest testUserCreateRequest;
    private User createdUser;


    @BeforeEach
    public void setUp() throws Exception {
        // Create a test user request
        testUserCreateRequest = new UserCreateRequest("testuser", "Test User");
        
        // Create a user to test get, update, and delete endpoints
        createdUser = userService.createUser(testUserCreateRequest);
    }

    @Test
    public void getAllUsers_ReturnsUsersList() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", isA(ArrayList.class)))
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].id", notNullValue()))
                .andExpect(jsonPath("$[0].username", notNullValue()));
    }

    @Test
    public void getUserById_WhenUserExists_ReturnsUser() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/users/{id}", createdUser.id())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(createdUser.id().intValue())))
                .andExpect(jsonPath("$.username", is(createdUser.username())))
                .andExpect(jsonPath("$.fullName", is(createdUser.fullName())));
    }

    @Test
    public void getUserById_WhenUserDoesNotExist_ReturnsNotFound() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/users/99999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createUser_WithValidData_ReturnsCreatedUser() throws Exception {
        // Arrange
        UserCreateRequest newUser = new UserCreateRequest("testuser1", "Test User 1");

        // Act & Assert
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.username", is(newUser.username())))
                .andExpect(jsonPath("$.fullName", is(newUser.fullName())));
    }

    @Test
    public void createUser_WithDuplicateUsername_ReturnsConflict() throws Exception {
        // Arrange
        UserCreateRequest duplicateUser = new UserCreateRequest("testuser", "Test User 1");

        // Act & Assert
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(duplicateUser)))
                .andExpect(status().isConflict());
    }

    @Test
    public void updateUser_WithValidData_ReturnsUpdatedUser() throws Exception {
        // Arrange
        UserUpdateRequest updateRequest = new UserUpdateRequest("Updated User");

        // Act & Assert
        mockMvc.perform(patch("/api/users/{id}", createdUser.id())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(createdUser.id().intValue())))
                .andExpect(jsonPath("$.fullName", is(updateRequest.fullName())));
    }

    @Test
    public void updateUser_WhenUserDoesNotExist_ReturnsNotFound() throws Exception {
        // Arrange
        UserUpdateRequest updateRequest = new UserUpdateRequest("Updated User");

        // Act & Assert
        mockMvc.perform(patch("/api/users/99999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteUser_WhenUserExists_ReturnsNoContent() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/users/{id}", createdUser.id())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Verify user is deleted
        mockMvc.perform(get("/api/users/{id}", createdUser.id())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}
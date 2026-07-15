package com.expensetracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.expensetracker.TestCleanup;
import com.expensetracker.TestSecurityConfig;
import com.expensetracker.dto.CategoryDTO;
import com.expensetracker.dto.RegisterRequest;
import com.expensetracker.model.TransactionType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestSecurityConfig.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CategoryControllerTest extends TestCleanup {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Long userId;

    @Test
    @Order(1)
    @DisplayName("Setup - register a user")
    void setupUser() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setName("CatTestUser");
        request.setEmail("cattest@example.com");
        request.setPassword("password123");

        MvcResult result = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        // User ID will be 1 since we cleaned the DB
        userId = 1L;
    }

    @Test
    @Order(2)
    @DisplayName("Create category - should succeed")
    void testCreateCategory() throws Exception {
        CategoryDTO dto = new CategoryDTO();
        dto.setName("Food");
        dto.setIcon("🍔");
        dto.setColor("#FF5733");
        dto.setType(TransactionType.EXPENSE);

        mockMvc.perform(post("/api/categories")
                        .param("userId", userId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Food"));
    }

    @Test
    @Order(3)
    @DisplayName("Create category - should fail without name")
    void testCreateCategoryNoName() throws Exception {
        CategoryDTO dto = new CategoryDTO();
        dto.setType(TransactionType.EXPENSE);

        mockMvc.perform(post("/api/categories")
                        .param("userId", userId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(4)
    @DisplayName("List categories - should return categories")
    void testListCategories() throws Exception {
        mockMvc.perform(get("/api/categories")
                        .param("userId", userId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("Food"));
    }

    @Test
    @Order(5)
    @DisplayName("Delete category - should succeed")
    void testDeleteCategory() throws Exception {
        mockMvc.perform(delete("/api/categories/1")
                        .param("userId", userId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Category deleted"));
    }
}
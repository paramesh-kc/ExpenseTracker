package com.expensetracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.expensetracker.TestCleanup;
import com.expensetracker.dto.CategoryDTO;
import com.expensetracker.dto.RegisterRequest;
import com.expensetracker.model.TransactionType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import jakarta.servlet.http.Cookie; 

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CategoryControllerTest extends TestCleanup {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Long userId;
    private Long categoryId;
    private String jwtToken; 

    @BeforeAll
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

        String response = result.getResponse().getContentAsString();
        System.out.println("=== REGISTER RESPONSE: " + response + " ===");

        userId = objectMapper.readTree(response).get("userId").asLong();
        System.out.println("=== USER ID: " + userId + " ===");

        jwtToken = result.getResponse().getCookie("token").getValue();
        System.out.println("=== JWT TOKEN: " + jwtToken + " ===");

    }

    @Test
    @Order(1)
    @DisplayName("Create category - should succeed")
    void testCreateCategory() throws Exception {
        CategoryDTO dto = new CategoryDTO();
        dto.setName("Food");
        dto.setIcon("🍔");
        dto.setColor("#FF5733");
        dto.setType(TransactionType.EXPENSE);

        MvcResult result = mockMvc.perform(post("/api/categories")
                        .cookie(new Cookie("token", jwtToken))
                        .param("userId", userId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Food"))
                .andReturn();

        String response = result.getResponse().getContentAsString();
        categoryId = objectMapper.readTree(response).get("id").asLong();
    }

    @Test
    @Order(2)
    @DisplayName("Create category - should fail without name")
    void testCreateCategoryNoName() throws Exception {
        CategoryDTO dto = new CategoryDTO();
        dto.setType(TransactionType.EXPENSE);

        mockMvc.perform(post("/api/categories")
                        .cookie(new Cookie("token", jwtToken))
                        .param("userId", userId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(3)
    @DisplayName("List categories - should return categories")
    void testListCategories() throws Exception {
        mockMvc.perform(get("/api/categories")
                        .cookie(new Cookie("token", jwtToken))
                        .param("userId", userId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("Food"));
    }

    @Test
    @Order(4)
    @DisplayName("Delete category - should succeed")
    void testDeleteCategory() throws Exception {
        mockMvc.perform(delete("/api/categories/" + categoryId)
                        .cookie(new Cookie("token", jwtToken))
                        .param("userId", userId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Category deleted"));
    }
}
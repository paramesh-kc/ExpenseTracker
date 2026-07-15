package com.expensetracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.expensetracker.TestCleanup;
import com.expensetracker.dto.*;
import com.expensetracker.model.TransactionType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import jakarta.servlet.http.Cookie;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TransactionControllerTest extends TestCleanup {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Long userId;
    private Long categoryId;
    private Long transactionId;
    private String jwtToken; 

    @BeforeAll
    void setup() throws Exception {
        // register user
        RegisterRequest user = new RegisterRequest();
        user.setName("TxnTestUser");
        user.setEmail("txntest@example.com");
        user.setPassword("password123");

        MvcResult userResult = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andReturn();

        String userResponse = userResult.getResponse().getContentAsString();
        userId = objectMapper.readTree(userResponse).get("userId").asLong();
        jwtToken = userResult.getResponse().getCookie("token").getValue();
        System.out.println("=== JWT TOKEN: " + jwtToken + " ===");

        // create category
        CategoryDTO category = new CategoryDTO();
        category.setName("TestFood");
        category.setIcon("🍔");
        category.setColor("#FF0000");
        category.setType(TransactionType.EXPENSE);

        MvcResult catResult = mockMvc.perform(post("/api/categories")
                        .cookie(new Cookie("token", jwtToken))
                        .param("userId", userId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(category)))
                .andExpect(status().isCreated())
                .andReturn();

        String catResponse = catResult.getResponse().getContentAsString();
        categoryId = objectMapper.readTree(catResponse).get("id").asLong();
    }

    @Test
    @Order(1)
    @DisplayName("Create transaction - should succeed")
    void testCreateTransaction() throws Exception {
        TransactionDTO dto = new TransactionDTO();
        dto.setAmount(500.0);
        dto.setType(TransactionType.EXPENSE);
        dto.setNote("Lunch");
        dto.setDate(LocalDate.of(2025, 7, 15));
        dto.setCategoryId(categoryId);

        MvcResult result = mockMvc.perform(post("/api/transactions")
                        .cookie(new Cookie("token", jwtToken))
                        .param("userId", userId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.amount").value(500.0))
                .andExpect(jsonPath("$.note").value("Lunch"))
                .andReturn();

        String response = result.getResponse().getContentAsString();
        transactionId = objectMapper.readTree(response).get("id").asLong();
    }

    @Test
    @Order(2)
    @DisplayName("Create transaction - should fail without amount")
    void testCreateTransactionNoAmount() throws Exception {
        TransactionDTO dto = new TransactionDTO();
        dto.setType(TransactionType.EXPENSE);
        dto.setDate(LocalDate.of(2025, 7, 15));
        dto.setCategoryId(categoryId);

        mockMvc.perform(post("/api/transactions")
                        .cookie(new Cookie("token", jwtToken))
                        .param("userId", userId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(3)
    @DisplayName("List transactions - should return transactions")
    void testListTransactions() throws Exception {
        mockMvc.perform(get("/api/transactions")
                        .cookie(new Cookie("token", jwtToken))
                        .param("userId", userId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @Order(4)
    @DisplayName("Get single transaction - should succeed")
    void testGetTransaction() throws Exception {
        mockMvc.perform(get("/api/transactions/" + transactionId)
                        .cookie(new Cookie("token", jwtToken))
                        .param("userId", userId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(500.0));
    }

    @Test
    @Order(5)
    @DisplayName("Get non-existent transaction - should return 404")
    void testGetTransactionNotFound() throws Exception {
        mockMvc.perform(get("/api/transactions/999999")
                        .cookie(new Cookie("token", jwtToken))
                        .param("userId", userId.toString()))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(6)
    @DisplayName("Update transaction - should succeed")
    void testUpdateTransaction() throws Exception {
        TransactionDTO dto = new TransactionDTO();
        dto.setAmount(750.0);
        dto.setType(TransactionType.EXPENSE);
        dto.setNote("Dinner updated");
        dto.setDate(LocalDate.of(2025, 7, 15));
        dto.setCategoryId(categoryId);

        mockMvc.perform(put("/api/transactions/" + transactionId)
                        .cookie(new Cookie("token", jwtToken))
                        .param("userId", userId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(750.0))
                .andExpect(jsonPath("$.note").value("Dinner updated"));
    }

    @Test
    @Order(7)
    @DisplayName("Delete transaction - should succeed")
    void testDeleteTransaction() throws Exception {
        mockMvc.perform(delete("/api/transactions/" + transactionId)
                        .cookie(new Cookie("token", jwtToken))
                        .param("userId", userId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Transaction deleted"));
    }
}
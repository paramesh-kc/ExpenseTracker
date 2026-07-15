package com.expensetracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.expensetracker.TestCleanup;
import com.expensetracker.TestSecurityConfig;
import com.expensetracker.dto.*;
import com.expensetracker.model.TransactionType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestSecurityConfig.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DashboardControllerTest extends TestCleanup {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Order(1)
    @DisplayName("Setup - create user, categories, and transactions")
    void setup() throws Exception {
        // Register
        RegisterRequest user = new RegisterRequest();
        user.setName("DashUser");
        user.setEmail("dashtest@example.com");
        user.setPassword("password123");
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)));

        // Category: Salary (INCOME) → id=1
        CategoryDTO salary = new CategoryDTO();
        salary.setName("Salary");
        salary.setIcon("💰");
        salary.setColor("#27AE60");
        salary.setType(TransactionType.INCOME);
        mockMvc.perform(post("/api/categories").param("userId", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(salary)));

        // Category: Food (EXPENSE) → id=2
        CategoryDTO food = new CategoryDTO();
        food.setName("Food");
        food.setIcon("🍔");
        food.setColor("#FF5733");
        food.setType(TransactionType.EXPENSE);
        mockMvc.perform(post("/api/categories").param("userId", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(food)));

        // Transaction: Salary income
        TransactionDTO salaryTxn = new TransactionDTO();
        salaryTxn.setAmount(75000.0);
        salaryTxn.setType(TransactionType.INCOME);
        salaryTxn.setNote("Monthly salary");
        salaryTxn.setDate(LocalDate.of(2025, 7, 1));
        salaryTxn.setCategoryId(1L);
        mockMvc.perform(post("/api/transactions").param("userId", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(salaryTxn)));

        // Transaction: Food expense
        TransactionDTO foodTxn = new TransactionDTO();
        foodTxn.setAmount(500.0);
        foodTxn.setType(TransactionType.EXPENSE);
        foodTxn.setNote("Lunch");
        foodTxn.setDate(LocalDate.of(2025, 7, 15));
        foodTxn.setCategoryId(2L);
        mockMvc.perform(post("/api/transactions").param("userId", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(foodTxn)));
    }

    @Test
    @Order(2)
    @DisplayName("Dashboard summary - should return correct totals")
    void testDashboardSummary() throws Exception {
        mockMvc.perform(get("/api/dashboard/summary")
                        .param("userId", "1")
                        .param("month", "7")
                        .param("year", "2025"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalIncome").value(75000.0))
                .andExpect(jsonPath("$.totalExpense").value(500.0))
                .andExpect(jsonPath("$.balance").value(74500.0))
                .andExpect(jsonPath("$.byCategory").isArray())
                .andExpect(jsonPath("$.dailyExpenses").isArray());
    }

    @Test
    @Order(3)
    @DisplayName("Dashboard summary - empty month should return zeros")
    void testDashboardEmptyMonth() throws Exception {
        mockMvc.perform(get("/api/dashboard/summary")
                        .param("userId", "1")
                        .param("month", "1")
                        .param("year", "2020"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalIncome").value(0.0))
                .andExpect(jsonPath("$.totalExpense").value(0.0))
                .andExpect(jsonPath("$.balance").value(0.0));
    }
}
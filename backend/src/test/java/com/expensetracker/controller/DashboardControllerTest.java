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
public class DashboardControllerTest extends TestCleanup {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Long userId;
    private Long salaryCategId;
    private Long foodCategId;
    private String jwtToken; 


    @BeforeAll
    void setup() throws Exception {

        // register user
        RegisterRequest user = new RegisterRequest();
        user.setName("DashUser");
        user.setEmail("dashtest@example.com");
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

        // create Salary category (INCOME)
        CategoryDTO salary = new CategoryDTO();
        salary.setName("Salary");
        salary.setIcon("💰");
        salary.setColor("#27AE60");
        salary.setType(TransactionType.INCOME);

        MvcResult salaryResult = mockMvc.perform(post("/api/categories")
                        .cookie(new Cookie("token", jwtToken))
                        .param("userId", userId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(salary)))
                .andExpect(status().isCreated())
                .andReturn();

        String salaryResponse = salaryResult.getResponse().getContentAsString();
        salaryCategId = objectMapper.readTree(salaryResponse).get("id").asLong();

        // create Food category (EXPENSE)
        CategoryDTO food = new CategoryDTO();
        food.setName("Food");
        food.setIcon("🍔");
        food.setColor("#FF5733");
        food.setType(TransactionType.EXPENSE);

        MvcResult foodResult = mockMvc.perform(post("/api/categories")
                        .cookie(new Cookie("token", jwtToken))
                        .param("userId", userId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(food)))
                .andExpect(status().isCreated())
                .andReturn();

        String foodResponse = foodResult.getResponse().getContentAsString();
        foodCategId = objectMapper.readTree(foodResponse).get("id").asLong();

        // create salary transaction (INCOME)
        TransactionDTO salaryTxn = new TransactionDTO();
        salaryTxn.setAmount(75000.0);
        salaryTxn.setType(TransactionType.INCOME);
        salaryTxn.setNote("Monthly salary");
        salaryTxn.setDate(LocalDate.of(2025, 7, 1));
        salaryTxn.setCategoryId(salaryCategId);

        mockMvc.perform(post("/api/transactions")
                        .cookie(new Cookie("token", jwtToken))
                        .param("userId", userId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(salaryTxn)))
                .andExpect(status().isCreated());

        // create food transaction (EXPENSE)
        TransactionDTO foodTxn = new TransactionDTO();
        foodTxn.setAmount(500.0);
        foodTxn.setType(TransactionType.EXPENSE);
        foodTxn.setNote("Lunch");
        foodTxn.setDate(LocalDate.of(2025, 7, 15));
        foodTxn.setCategoryId(foodCategId);

        mockMvc.perform(post("/api/transactions")
                        .cookie(new Cookie("token", jwtToken))
                        .param("userId", userId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(foodTxn)))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(1)
    @DisplayName("Dashboard summary - should return correct totals")
    void testDashboardSummary() throws Exception {
        mockMvc.perform(get("/api/dashboard/summary")
                        .cookie(new Cookie("token", jwtToken))
                        .param("userId", userId.toString())
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
    @Order(2)
    @DisplayName("Dashboard summary - empty month should return zeros")
    void testDashboardEmptyMonth() throws Exception {
        mockMvc.perform(get("/api/dashboard/summary")
                        .cookie(new Cookie("token", jwtToken))
                        .param("userId", userId.toString())
                        .param("month", "1")
                        .param("year", "2020"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalIncome").value(0.0))
                .andExpect(jsonPath("$.totalExpense").value(0.0))
                .andExpect(jsonPath("$.balance").value(0.0));
    }
}
package com.expensetracker;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class TestCleanup {

    @Autowired
    private JdbcTemplate jdbcTemplate;

//    @BeforeAll
//    void cleanDatabase() {
//        System.out.println("Cleaning database for "+ getClass().getSimpleName());
//        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0");
//        jdbcTemplate.execute("TRUNCATE TABLE transactions");
//        jdbcTemplate.execute("TRUNCATE TABLE budgets");
//        jdbcTemplate.execute("TRUNCATE TABLE categories");
//        jdbcTemplate.execute("TRUNCATE TABLE users");
//        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1");
//    }

     void truncateTables() {
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0");
        jdbcTemplate.execute("TRUNCATE TABLE transactions");
        jdbcTemplate.execute("TRUNCATE TABLE budgets");
        jdbcTemplate.execute("TRUNCATE TABLE categories");
        jdbcTemplate.execute("TRUNCATE TABLE users");
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1");
    }

    @BeforeAll
    void beforeAll() {
        truncateTables();
    }

    @AfterAll
    void afterAll() {
        truncateTables();
    }
}
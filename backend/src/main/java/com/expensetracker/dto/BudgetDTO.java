package com.expensetracker.dto;

import jakarta.validation.constraints.*;

public class BudgetDTO {

    @NotNull(message = "Category ID is required")
    private Long categoryId;

    @NotNull(message = "Monthly limit is required")
    @Positive(message = "Limit must be positive")
    private Double monthlyLimit;

    @NotNull(message = "Month is required")
    @Min(1) @Max(12)
    private Integer month;

    @NotNull(message = "Year is required")
    private Integer year;

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public Double getMonthlyLimit() { return monthlyLimit; }
    public void setMonthlyLimit(Double monthlyLimit) { this.monthlyLimit = monthlyLimit; }

    public Integer getMonth() { return month; }
    public void setMonth(Integer month) { this.month = month; }

    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }
}
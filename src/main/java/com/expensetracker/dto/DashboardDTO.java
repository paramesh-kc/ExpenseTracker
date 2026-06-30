package com.expensetracker.dto;

import java.util.List;
import java.util.Map;

public class DashboardDTO {

    private Double totalIncome;
    private Double totalExpense;
    private Double balance;
    private List<Map<String, Object>> byCategory;
    private List<Map<String, Object>> dailyExpenses;

    public Double getTotalIncome() { return totalIncome; }
    public void setTotalIncome(Double totalIncome) { this.totalIncome = totalIncome; }

    public Double getTotalExpense() { return totalExpense; }
    public void setTotalExpense(Double totalExpense) { this.totalExpense = totalExpense; }

    public Double getBalance() { return balance; }
    public void setBalance(Double balance) { this.balance = balance; }

    public List<Map<String, Object>> getByCategory() { return byCategory; }
    public void setByCategory(List<Map<String, Object>> byCategory) { this.byCategory = byCategory; }

    public List<Map<String, Object>> getDailyExpenses() { return dailyExpenses; }
    public void setDailyExpenses(List<Map<String, Object>> dailyExpenses) { this.dailyExpenses = dailyExpenses; }
}
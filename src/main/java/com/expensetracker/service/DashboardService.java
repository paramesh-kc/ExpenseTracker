package com.expensetracker.service;

import com.expensetracker.dto.DashboardDTO;
import com.expensetracker.model.TransactionType;
import com.expensetracker.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.*;

@Service
public class DashboardService {

    private final TransactionRepository transactionRepository;

    public DashboardService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public DashboardDTO getSummary(Long userId, int month, int year) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        Double totalIncome = transactionRepository
                .sumAmountByUserAndTypeAndDateBetween(userId, TransactionType.INCOME, startDate, endDate);
        Double totalExpense = transactionRepository
                .sumAmountByUserAndTypeAndDateBetween(userId, TransactionType.EXPENSE, startDate, endDate);

        if (totalIncome == null) totalIncome = 0.0;
        if (totalExpense == null) totalExpense = 0.0;

        // By category
        List<Object[]> categoryData = transactionRepository
                .sumByCategoryAndDateBetween(userId, TransactionType.EXPENSE, startDate, endDate);
        List<Map<String, Object>> byCategory = new ArrayList<>();
        for (Object[] row : categoryData) {
            Map<String, Object> map = new HashMap<>();
            map.put("category", row[0]);
            map.put("total", row[1]);
            byCategory.add(map);
        }

        // Daily expenses
        List<Object[]> dailyData = transactionRepository
                .sumByDateAndDateBetween(userId, TransactionType.EXPENSE, startDate, endDate);
        List<Map<String, Object>> dailyExpenses = new ArrayList<>();
        for (Object[] row : dailyData) {
            Map<String, Object> map = new HashMap<>();
            map.put("date", row[0].toString());
            map.put("total", row[1]);
            dailyExpenses.add(map);
        }

        DashboardDTO dto = new DashboardDTO();
        dto.setTotalIncome(totalIncome);
        dto.setTotalExpense(totalExpense);
        dto.setBalance(totalIncome - totalExpense);
        dto.setByCategory(byCategory);
        dto.setDailyExpenses(dailyExpenses);

        return dto;
    }
}
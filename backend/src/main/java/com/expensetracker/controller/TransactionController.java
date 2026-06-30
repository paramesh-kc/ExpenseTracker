package com.expensetracker.controller;

import com.expensetracker.dto.TransactionDTO;
import com.expensetracker.model.Transaction;
import com.expensetracker.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    private Map<String, Object> toMap(Transaction t) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", t.getId());
        map.put("amount", t.getAmount());
        map.put("type", t.getType());
        map.put("note", t.getNote());
        map.put("date", t.getDate().toString());
        map.put("categoryId", t.getCategory().getId());
        map.put("categoryName", t.getCategory().getName());
        return map;
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAll(
            @RequestParam(defaultValue = "1") Long userId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {
        List<Transaction> transactions = transactionService.getAll(userId, startDate, endDate);
        List<Map<String, Object>> response = new ArrayList<>();
        for (Transaction t : transactions) {
            response.add(toMap(t));
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getById(
            @PathVariable Long id,
            @RequestParam(defaultValue = "1") Long userId) {
        Transaction transaction = transactionService.getById(id, userId);
        return ResponseEntity.ok(toMap(transaction));
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(
            @Valid @RequestBody TransactionDTO dto,
            @RequestParam(defaultValue = "1") Long userId) {
        Transaction transaction = transactionService.create(dto, userId);
        Map<String, Object> response = toMap(transaction);
        response.put("message", "Transaction created successfully");
        return ResponseEntity.status(201).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> update(
            @PathVariable Long id,
            @Valid @RequestBody TransactionDTO dto,
            @RequestParam(defaultValue = "1") Long userId) {
        Transaction transaction = transactionService.update(id, dto, userId);
        return ResponseEntity.ok(toMap(transaction));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(
            @PathVariable Long id,
            @RequestParam(defaultValue = "1") Long userId) {
        transactionService.delete(id, userId);
        return ResponseEntity.ok(Map.of("message", "Transaction deleted"));
    }
}
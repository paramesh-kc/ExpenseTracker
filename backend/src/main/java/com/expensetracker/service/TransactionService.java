package com.expensetracker.service;

import com.expensetracker.dto.TransactionDTO;
import com.expensetracker.exception.ResourceNotFoundException;
import com.expensetracker.model.*;
import com.expensetracker.repository.*;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public TransactionService(TransactionRepository transactionRepository,
                              CategoryRepository categoryRepository,
                              UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    public List<Transaction> getAll(Long userId, LocalDate startDate, LocalDate endDate) {
        if (startDate != null && endDate != null) {
            return transactionRepository
                    .findByUserIdAndDateBetweenOrderByDateDesc(userId, startDate, endDate);
        }
        return transactionRepository.findByUserIdOrderByDateDesc(userId);
    }

    public Transaction getById(Long id, Long userId) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));

        if (!transaction.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Transaction not found");
        }
        return transaction;
    }

    public Transaction create(TransactionDTO dto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        Transaction transaction = new Transaction();
        transaction.setAmount(dto.getAmount());
        transaction.setType(dto.getType());
        transaction.setNote(dto.getNote());
        transaction.setDate(dto.getDate());
        transaction.setCategory(category);
        transaction.setUser(user);

        return transactionRepository.save(transaction);
    }

    public Transaction update(Long id, TransactionDTO dto, Long userId) {
        Transaction transaction = getById(id, userId);

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        transaction.setAmount(dto.getAmount());
        transaction.setType(dto.getType());
        transaction.setNote(dto.getNote());
        transaction.setDate(dto.getDate());
        transaction.setCategory(category);

        return transactionRepository.save(transaction);
    }

    public void delete(Long id, Long userId) {
        Transaction transaction = getById(id, userId);
        transactionRepository.delete(transaction);
    }
}
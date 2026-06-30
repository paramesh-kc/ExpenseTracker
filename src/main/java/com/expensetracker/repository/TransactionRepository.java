package com.expensetracker.repository;
import com.expensetracker.model.Transaction;
import com.expensetracker.model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUserIdOrderByDateDesc(Long userId);
    List<Transaction> findByUserIdAndDateBetweenOrderByDateDesc(
            Long userId, LocalDate startDate, LocalDate endDate);
    @Query("SELECT SUM(t.amount) FROM Transaction t " +
            "WHERE t.user.id = :userId AND t.type = :type " +
            "AND t.date BETWEEN :startDate AND :endDate")
    Double sumAmountByUserAndTypeAndDateBetween(
            @Param("userId") Long userId, @Param("type") TransactionType type,
            @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    @Query("SELECT t.category.name, SUM(t.amount) FROM Transaction t " +
            "WHERE t.user.id = :userId AND t.type = :type " +
            "AND t.date BETWEEN :startDate AND :endDate GROUP BY t.category.name")
    List<Object[]> sumByCategoryAndDateBetween(
            @Param("userId") Long userId, @Param("type") TransactionType type,
            @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    @Query("SELECT t.date, SUM(t.amount) FROM Transaction t " +
            "WHERE t.user.id = :userId AND t.type = :type " +
            "AND t.date BETWEEN :startDate AND :endDate " +
            "GROUP BY t.date ORDER BY t.date")
    List<Object[]> sumByDateAndDateBetween(
            @Param("userId") Long userId, @Param("type") TransactionType type,
            @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
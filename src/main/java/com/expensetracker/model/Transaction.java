package com.expensetracker.model;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
@Entity
@Table(name = "transactions")
public class Transaction {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Double amount;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;
    @Column(length = 255)
    private String note;
    @Column(nullable = false)
    private LocalDate date;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @PrePersist
    protected void onCreate() { createdAt = LocalDateTime.now(); }
    public Transaction() {}
    public Transaction(Double amount, TransactionType type, String note,
                       LocalDate date, Category category, User user) {
        this.amount=amount; this.type=type; this.note=note;
        this.date=date; this.category=category; this.user=user;
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Double getAmount() { return amount; }
    public void setAmount(Double a) { this.amount = a; }
    public TransactionType getType() { return type; }
    public void setType(TransactionType t) { this.type = t; }
    public String getNote() { return note; }
    public void setNote(String n) { this.note = n; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate d) { this.date = d; }
    public Category getCategory() { return category; }
    public void setCategory(Category c) { this.category = c; }
    public User getUser() { return user; }
    public void setUser(User u) { this.user = u; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime t) { this.createdAt = t; }
}
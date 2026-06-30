package com.expensetracker.model;
import jakarta.persistence.*;
@Entity
@Table(name = "budgets")
public class Budget {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(name = "monthly_limit", nullable = false)
    private Double monthlyLimit;
    @Column(nullable = false)
    private Integer month;
    @Column(nullable = false)
    private Integer year;
    public Budget() {}
    public Budget(Category cat, User user, Double limit, Integer month, Integer year) {
        this.category=cat; this.user=user; this.monthlyLimit=limit;
        this.month=month; this.year=year;
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Category getCategory() { return category; }
    public void setCategory(Category c) { this.category = c; }
    public User getUser() { return user; }
    public void setUser(User u) { this.user = u; }
    public Double getMonthlyLimit() { return monthlyLimit; }
    public void setMonthlyLimit(Double l) { this.monthlyLimit = l; }
    public Integer getMonth() { return month; }
    public void setMonth(Integer m) { this.month = m; }
    public Integer getYear() { return year; }
    public void setYear(Integer y) { this.year = y; }
}
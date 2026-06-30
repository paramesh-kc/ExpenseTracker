package com.expensetracker.model;
import jakarta.persistence.*;
@Entity
@Table(name = "categories")
public class Category {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 50)
    private String name;
    @Column(length = 20)
    private String icon;
    @Column(length = 7)
    private String color;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    public Category() {}
    public Category(String name, String icon, String color, TransactionType type, User user) {
        this.name=name; this.icon=icon; this.color=color; this.type=type; this.user=user;
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String n) { this.name = n; }
    public String getIcon() { return icon; }
    public void setIcon(String i) { this.icon = i; }
    public String getColor() { return color; }
    public void setColor(String c) { this.color = c; }
    public TransactionType getType() { return type; }
    public void setType(TransactionType t) { this.type = t; }
    public User getUser() { return user; }
    public void setUser(User u) { this.user = u; }
}
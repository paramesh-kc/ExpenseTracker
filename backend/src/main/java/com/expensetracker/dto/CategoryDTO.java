package com.expensetracker.dto;

import com.expensetracker.model.TransactionType;
import jakarta.validation.constraints.*;

public class CategoryDTO {

    @NotBlank(message = "Category name is required")
    private String name;

    private String icon;
    private String color;

    @NotNull(message = "Type is required (INCOME or EXPENSE)")
    private TransactionType type;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public TransactionType getType() { return type; }
    public void setType(TransactionType type) { this.type = type; }
}
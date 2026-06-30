package com.expensetracker.controller;

import com.expensetracker.dto.CategoryDTO;
import com.expensetracker.model.Category;
import com.expensetracker.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAll(
            @RequestParam(defaultValue = "1") Long userId) {
        List<Category> categories = categoryService.getAllByUser(userId);
        List<Map<String, Object>> response = new ArrayList<>();
        for (Category c : categories) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", c.getId());
            map.put("name", c.getName());
            map.put("icon", c.getIcon());
            map.put("color", c.getColor());
            map.put("type", c.getType());
            response.add(map);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(
            @Valid @RequestBody CategoryDTO dto,
            @RequestParam(defaultValue = "1") Long userId) {
        Category category = categoryService.create(dto, userId);
        Map<String, Object> response = new HashMap<>();
        response.put("id", category.getId());
        response.put("name", category.getName());
        response.put("icon", category.getIcon());
        response.put("color", category.getColor());
        response.put("type", category.getType());
        response.put("message", "Category created successfully");
        return ResponseEntity.status(201).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(
            @PathVariable Long id,
            @RequestParam(defaultValue = "1") Long userId) {
        categoryService.delete(id, userId);
        return ResponseEntity.ok(Map.of("message", "Category deleted"));
    }
}
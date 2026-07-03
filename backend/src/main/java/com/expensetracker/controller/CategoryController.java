package com.expensetracker.controller;

import com.expensetracker.dto.CategoryDTO;
import com.expensetracker.model.Category;
import com.expensetracker.service.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
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

    // Helper: get userId from JWT token (set by JwtAuthenticationFilter)
    private Long getUserId(HttpServletRequest request) {
        return (Long) request.getAttribute("userId");
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAll(HttpServletRequest request) {
        Long userId = getUserId(request);
        List<Category> categories = categoryService.getAllByUser(userId);
        List<Map<String, Object>> response = new ArrayList<>();
        for (Category c : categories) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", c.getId());
            map.put("name", c.getName());
            map.put("icon", c.getIcon() != null ? c.getIcon() : "");
            map.put("color", c.getColor() != null ? c.getColor() : "");
            map.put("type", c.getType());
            response.add(map);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(
            @Valid @RequestBody CategoryDTO dto, HttpServletRequest request) {
        Long userId = getUserId(request);
        Category category = categoryService.create(dto, userId);
        Map<String, Object> response = new HashMap<>();
        response.put("id", category.getId());
        response.put("name", category.getName());
        response.put("type", category.getType());
        response.put("message", "Category created");
        return ResponseEntity.status(201).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(
            @PathVariable Long id, HttpServletRequest request) {
        Long userId = getUserId(request);
        categoryService.delete(id, userId);
        return ResponseEntity.ok(Map.of("message", "Category deleted"));
    }
}
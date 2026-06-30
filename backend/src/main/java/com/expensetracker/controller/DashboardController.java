package com.expensetracker.controller;

import com.expensetracker.dto.DashboardDTO;
import com.expensetracker.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/summary")
    public ResponseEntity<DashboardDTO> getSummary(
            @RequestParam(defaultValue = "1") Long userId,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year) {
        if (month == null) month = LocalDate.now().getMonthValue();
        if (year == null) year = LocalDate.now().getYear();

        return ResponseEntity.ok(dashboardService.getSummary(userId, month, year));
    }
}
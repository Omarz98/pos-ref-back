package com.posref.pos.controller;

import com.posref.pos.dto.dashboard.DashboardResponse;
import com.posref.pos.service.IDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class DashboardController {

    private final IDashboardService dashboardService;

    @GetMapping
    public ResponseEntity<DashboardResponse> dashboard() {

        return ResponseEntity.ok(
                dashboardService.obtenerDashboard()
        );
    }
}

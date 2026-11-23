package com.posref.pos.controller.reportes;

import com.posref.pos.dto.dashboard.DashboardResponse;
import com.posref.pos.service.IDashboardService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
public class ReportesOperativosController {

    private final IDashboardService dashboardService;

    @GetMapping("/operacion")
    @PreAuthorize("hasAuthority('REPORTE_VER')")
    public ResponseEntity<DashboardResponse> obtenerOperacion() {

        return ResponseEntity.ok(
                dashboardService.obtenerDashboard()
        );
    }
}
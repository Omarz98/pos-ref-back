package com.posref.pos.controller.taller;

import com.posref.pos.dto.taller.TallerDashboardResponse;
import com.posref.pos.service.taller.TallerDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/taller")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class TallerDashboardController {

    private final TallerDashboardService service;

    @GetMapping("/dashboard")
    public ResponseEntity<TallerDashboardResponse>
    dashboard() {

        return ResponseEntity.ok(
                service.obtenerDashboard()
        );
    }
}

package com.posref.pos.controller.reportes;

import com.posref.pos.dto.reportes.ReporteIngresoCajaResponse;
import com.posref.pos.service.reportes.ReportesCajaService;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reportes/caja")
@RequiredArgsConstructor
public class ReportesCajaController {

    private final ReportesCajaService reportesCajaService;

    @GetMapping("/ingresos")
    @PreAuthorize("hasAuthority('REPORTE_CAJA_VER')")
    public ResponseEntity<List<ReporteIngresoCajaResponse>> ingresos(

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate desde,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate hasta
    ) {

        return ResponseEntity.ok(
                reportesCajaService.obtenerIngresos(desde, hasta)
        );
    }
}

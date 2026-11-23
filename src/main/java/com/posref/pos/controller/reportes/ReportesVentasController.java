package com.posref.pos.controller.reportes;

import com.posref.pos.dto.reportes.ReporteVentaDetalleResponse;
import com.posref.pos.dto.reportes.ReporteVentaDiariaResponse;
import com.posref.pos.dto.reportes.ReporteVentasResumenResponse;
import com.posref.pos.model.EstadoVenta;
import com.posref.pos.service.reportes.ReporteMetodoPagoResponse;
import com.posref.pos.service.reportes.ReporteVentasDetalleService;
import com.posref.pos.service.reportes.ReportesVentasService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reportes/ventas")
@RequiredArgsConstructor
public class ReportesVentasController {

    private final ReportesVentasService reportesVentasService;
    private final ReporteVentasDetalleService detalleService;

    @GetMapping("/resumen")
    @PreAuthorize("hasAuthority('REPORTE_VENTAS_VER')")
    public ResponseEntity<ReporteVentasResumenResponse> resumen(

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate desde,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate hasta
    ) {

        return ResponseEntity.ok(
                reportesVentasService.obtenerResumen(desde, hasta)
        );
    }

    @GetMapping("/metodos-pago")
    @PreAuthorize("hasAuthority('REPORTE_VENTAS_VER')")
    public ResponseEntity<List<ReporteMetodoPagoResponse>>
    obtenerMetodosPago(

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate desde,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate hasta
    ) {

        return ResponseEntity.ok(
                reportesVentasService.obtenerMetodosPago(
                        desde,
                        hasta
                )
        );
    }

    @GetMapping("/diarias")
    @PreAuthorize("hasAuthority('REPORTE_VENTAS_VER')")
    public ResponseEntity<List<ReporteVentaDiariaResponse>> ventasDiarias(

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate desde,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate hasta
    ) {

        return ResponseEntity.ok(
                reportesVentasService.obtenerVentasDiarias(
                        desde,
                        hasta
                )
        );
    }

    @GetMapping("/detalle")
    @PreAuthorize("hasAuthority('REPORTE_VENTAS_VER')")
    public ResponseEntity<Page<ReporteVentaDetalleResponse>> detalle(

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate desde,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate hasta,

            @RequestParam(required = false)
            EstadoVenta estado,

            @RequestParam(required = false)
            Long ventaId,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size
    ) {

        return ResponseEntity.ok(
                detalleService.obtenerDetalle(
                        desde,
                        hasta,
                        estado,
                        ventaId,
                        page,
                        size
                )
        );
    }
}
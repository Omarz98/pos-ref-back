package com.posref.pos.controller;

import com.posref.pos.dto.CobroVentaRequest;
import com.posref.pos.dto.OrdenTallerResponse;
import com.posref.pos.dto.VentaRequest;
import com.posref.pos.dto.VentaResponse;
import com.posref.pos.model.Ventas;
import com.posref.pos.service.OrdenTallerService;
import com.posref.pos.service.VentaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ventas")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173") // Para conectar con React
public class VentaController {

    private final VentaService ventaService;
    private final OrdenTallerService ordenServicioService;

    @PreAuthorize("hasAuthority('VENTA_CREAR')")
    @PostMapping
    public ResponseEntity<VentaResponse> crearVenta(
            @RequestBody VentaRequest request
    ) {

        return ResponseEntity.ok(
                ventaService.guardarVenta(request)
        );
    }

    @PreAuthorize("hasAuthority('VENTA_VER')")
    @GetMapping
    public ResponseEntity<List<VentaResponse>> listar() {

        return ResponseEntity.ok(
                ventaService.obtenerVentas()
        );
    }

    @PreAuthorize("hasAuthority('VENTA_ACTUALIZAR')")
    @PutMapping("/{id}/cobrar")
    public ResponseEntity<VentaResponse> cobrarVenta(
            @PathVariable Long id,
            @RequestBody CobroVentaRequest request
    ) {
        VentaResponse venta = ventaService.cobrarVenta(id, request);

        return ResponseEntity.ok(venta);
    }

    //@PreAuthorize("hasAuthority('VENTA_VER')")
    @GetMapping("/pendientes")
    public ResponseEntity<List<OrdenTallerResponse>> obtenerPendientes() {
        return ResponseEntity.ok(
                ordenServicioService.obtenerOrdenesPendientesDePago()
        );
    }
}


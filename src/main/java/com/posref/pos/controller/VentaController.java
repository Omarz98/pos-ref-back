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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ventas")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173") // Para conectar con React
public class VentaController {

    private final VentaService ventaService;
    private final OrdenTallerService ordenServicioService;

    @PostMapping
    public ResponseEntity<VentaResponse> crearVenta(
            @RequestBody VentaRequest request
    ) {

        return ResponseEntity.ok(
                ventaService.guardarVenta(request)
        );
    }

    @GetMapping
    public ResponseEntity<List<VentaResponse>> listar() {

        return ResponseEntity.ok(
                ventaService.obtenerVentas()
        );
    }

    @PutMapping("/{id}/cobrar")
    public ResponseEntity<VentaResponse> cobrarVenta(
            @PathVariable Long id,
            @RequestBody CobroVentaRequest request
    ) {
        VentaResponse venta = ventaService.cobrarVenta(id, request);

        return ResponseEntity.ok(venta);
    }

    @GetMapping("/pendientes")
    public ResponseEntity<List<OrdenTallerResponse>> obtenerPendientes() {
        return ResponseEntity.ok(
                ordenServicioService.obtenerOrdenesPendientesDePago()
        );
    }
}


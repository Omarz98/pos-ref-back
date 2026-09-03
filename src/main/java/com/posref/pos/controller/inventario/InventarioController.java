package com.posref.pos.controller.inventario;

import com.posref.pos.dto.inventario.InventarioMovimientoResponse;
import com.posref.pos.dto.inventario.InventarioProductoResponse;
import com.posref.pos.dto.inventario.InventarioResumenResponse;
import com.posref.pos.dto.inventario.MovimientoInventarioRequest;
import com.posref.pos.model.inventario.InventarioMovimiento;
import com.posref.pos.service.inventario.IInventarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventario")
@CrossOrigin(origins = "*")
public class InventarioController {

    private final IInventarioService inventarioService;

    public InventarioController(
            IInventarioService inventarioService
    ) {
        this.inventarioService = inventarioService;
    }

    @GetMapping
    public ResponseEntity<List<InventarioProductoResponse>>
    obtenerInventario() {

        return ResponseEntity.ok(
                inventarioService.obtenerInventario()
        );
    }

    @GetMapping("/producto/{productoId}")
    public ResponseEntity<InventarioProductoResponse>
    obtenerProducto(
            @PathVariable Long productoId
    ) {

        return ResponseEntity.ok(
                inventarioService.obtenerProducto(
                        productoId
                )
        );
    }

    @GetMapping("/movimientos/{productoId}")
    public ResponseEntity<List<InventarioMovimientoResponse>> obtenerMovimientos(
            @PathVariable Long productoId) {

        return ResponseEntity.ok(
                inventarioService.obtenerMovimientos(productoId)
        );
    }

    @PostMapping("/movimientos")
    public ResponseEntity<InventarioMovimientoResponse> registrarMovimiento(
            @RequestBody MovimientoInventarioRequest request) {

        InventarioMovimientoResponse movimiento =
                inventarioService.registrarMovimiento(request);

        return ResponseEntity.ok(movimiento);
    }

    @GetMapping("/resumen")
    public ResponseEntity<InventarioResumenResponse>
    obtenerResumen() {

        return ResponseEntity.ok(
                inventarioService.obtenerResumen()
        );
    }

}

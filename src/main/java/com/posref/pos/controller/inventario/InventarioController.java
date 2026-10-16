package com.posref.pos.controller.inventario;

import com.posref.pos.dto.inventario.InventarioMovimientoResponse;
import com.posref.pos.dto.inventario.InventarioProductoResponse;
import com.posref.pos.dto.inventario.InventarioResumenResponse;
import com.posref.pos.dto.inventario.MovimientoInventarioRequest;
import com.posref.pos.service.inventario.IInventarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventario")
@Validated
public class InventarioController {

    private final IInventarioService inventarioService;

    public InventarioController(
            IInventarioService inventarioService
    ) {
        this.inventarioService = inventarioService;
    }

    // Consultar inventario completo
    @PreAuthorize("hasAuthority('INVENTARIO_VER')")
    @GetMapping
    public ResponseEntity<List<InventarioProductoResponse>>
    obtenerInventario() {

        return ResponseEntity.ok(
                inventarioService.obtenerInventario()
        );
    }

    // Consultar inventario de un producto
    @PreAuthorize("hasAuthority('INVENTARIO_VER')")
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

    // Consultar historial de movimientos
    @PreAuthorize("hasAuthority('INVENTARIO_MOVIMIENTOS_VER')")
    @GetMapping("/movimientos/{productoId}")
    public ResponseEntity<List<InventarioMovimientoResponse>> obtenerMovimientos(
            @PathVariable Long productoId) {

        return ResponseEntity.ok(
                inventarioService.obtenerMovimientos(productoId)
        );
    }

    // Registrar entrada, salida o ajuste
    @PreAuthorize("hasAuthority('INVENTARIO_MOVIMIENTO_CREAR')")
    @PostMapping("/movimientos")
    public ResponseEntity<InventarioMovimientoResponse> registrarMovimiento(
            @RequestBody MovimientoInventarioRequest request) {

        InventarioMovimientoResponse movimiento =
                inventarioService.registrarMovimiento(request);

        return ResponseEntity.ok(movimiento);
    }

    // Consultar resumen de inventario
    @PreAuthorize("hasAuthority('INVENTARIO_VER')")
    @GetMapping("/resumen")
    public ResponseEntity<InventarioResumenResponse>
    obtenerResumen() {

        return ResponseEntity.ok(
                inventarioService.obtenerResumen()
        );
    }

}

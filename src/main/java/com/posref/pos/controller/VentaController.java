package com.posref.pos.controller;

import com.posref.pos.dto.VentaRequest;
import com.posref.pos.model.Ventas;
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

    @PostMapping
    public ResponseEntity<Ventas> crearVenta(
            @RequestBody VentaRequest request
    ) {

        return ResponseEntity.ok(
                ventaService.guardarVenta(request)
        );
    }

    @GetMapping
    public List<Ventas> listar() {
        return ventaService.obtenerVentas();
    }
}


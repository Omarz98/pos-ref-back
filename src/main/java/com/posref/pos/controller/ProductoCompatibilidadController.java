package com.posref.pos.controller;

import com.posref.pos.dto.ProductoCompatibleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.posref.pos.service.IProductoCompatibilidadService;

import java.util.List;

@RestController
@RequestMapping("/api/compatibilidad")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class ProductoCompatibilidadController {

    private final IProductoCompatibilidadService
            productoCompatibilidadService;

    @GetMapping("/moto-version/{motoVersionId}/productos")
    public ResponseEntity<List<ProductoCompatibleResponse>>
    obtenerProductosCompatibles(
            @PathVariable Long motoVersionId
    ) {

        return ResponseEntity.ok(
                productoCompatibilidadService
                        .obtenerProductosCompatibles(motoVersionId)
        );
    }

    @GetMapping(
            "/moto-version/{motoVersionId}/producto/{productoId}"
    )
    public ResponseEntity<Boolean> validarCompatibilidad(
            @PathVariable Long motoVersionId,
            @PathVariable Long productoId
    ) {

        return ResponseEntity.ok(
                productoCompatibilidadService.esCompatible(
                        productoId,
                        motoVersionId
                )
        );
    }
}

package com.posref.pos.controller;

import com.posref.pos.dto.MotocicletaRequest;
import com.posref.pos.dto.MotocicletaResponse;
import com.posref.pos.dto.ProductosDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.posref.pos.service.IMotocicletaService;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/motocicletas")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class MotocicletaController {

    private final IMotocicletaService motocicletaService;

    @GetMapping
    public ResponseEntity<List<MotocicletaResponse>> traerMotocicletas(){
        return ResponseEntity.ok(motocicletaService.traerMotocicletas());
    }

    @PostMapping
    public ResponseEntity<MotocicletaResponse> guardar(
            @Valid @RequestBody MotocicletaRequest request
    ) {

        MotocicletaResponse response =
                motocicletaService.guardar(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MotocicletaResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody MotocicletaRequest request
    ) {

        return ResponseEntity.ok(
                motocicletaService.actualizar(id, request)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<MotocicletaResponse> obtenerPorId(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                motocicletaService.obtenerPorId(id)
        );
    }

    @GetMapping("cliente/{clienteId}")
    public ResponseEntity<List<MotocicletaResponse>>
    obtenerPorCliente(
            @PathVariable Long clienteId
    ) {

        return ResponseEntity.ok(
                motocicletaService.obtenerPorCliente(clienteId)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivar(
            @PathVariable Long id
    ) {

        motocicletaService.desactivar(id);

        return ResponseEntity.noContent().build();
    }
}
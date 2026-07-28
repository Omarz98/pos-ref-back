package com.posref.pos.controller;

import com.posref.pos.dto.OrdenTallerRequest;
import com.posref.pos.dto.OrdenTallerResponse;
import com.posref.pos.model.EstadoOrdenTrabajo;
import com.posref.pos.service.OrdenTallerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ordenes-taller")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class OrdenTallerController {

    private final OrdenTallerService ordenTallerService;

    @PostMapping
    public ResponseEntity<OrdenTallerResponse> guardar(
            @Valid
            @RequestBody
            OrdenTallerRequest request
    ) {

        OrdenTallerResponse response =
                ordenTallerService.guardar(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<OrdenTallerResponse>>
    obtenerTodas(
            @RequestParam(
                    required = false
            )
            EstadoOrdenTrabajo estado
    ) {

        if (estado != null) {
            return ResponseEntity.ok(
                    ordenTallerService
                            .obtenerPorEstado(estado)
            );
        }

        return ResponseEntity.ok(
                ordenTallerService.obtenerTodas()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdenTallerResponse>
    obtenerPorId(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                ordenTallerService.obtenerPorId(id)
        );
    }

    @GetMapping("/pendientes")
    public ResponseEntity<List<OrdenTallerResponse>>
    obtenerOrdenesPendientes() {

        return ResponseEntity.ok(
                ordenTallerService.obtenerOrdenesPendientesDePago()
        );
    }
}

package com.posref.pos.controller;

import com.posref.pos.dto.*;
import com.posref.pos.dto.taller.ActualizarOrdenTallerRequest;
import com.posref.pos.dto.taller.AsignarTecnicoOrdenRequest;
import com.posref.pos.dto.taller.CambiarEstadoOrdenRequest;
import com.posref.pos.model.EstadoOrdenTrabajo;
import com.posref.pos.service.IOrdenTallerService;
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

    private final IOrdenTallerService ordenTallerService;

    @PostMapping
    public ResponseEntity<OrdenTallerResponse> guardar(
            @Valid
            @RequestBody
            OrdenTallerRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ordenTallerService
                                .guardar(request)
                );
    }

    @GetMapping
    public ResponseEntity<List<OrdenTallerResponse>>
    obtenerTodas(
            @RequestParam(required = false)
            EstadoOrdenTrabajo estado,
            @RequestParam(required = false)
            String buscar
    ) {

        return ResponseEntity.ok(
                ordenTallerService.buscar(
                        estado,
                        buscar
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdenTallerResponse>
    obtenerPorId(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                ordenTallerService
                        .obtenerPorId(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrdenTallerResponse>
    actualizar(
            @PathVariable Long id,
            @RequestBody
            ActualizarOrdenTallerRequest request
    ) {

        return ResponseEntity.ok(
                ordenTallerService
                        .actualizar(
                                id,
                                request
                        )
        );
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<OrdenTallerResponse>
    cambiarEstado(
            @PathVariable Long id,
            @Valid
            @RequestBody
            CambiarEstadoOrdenRequest request
    ) {

        return ResponseEntity.ok(
                ordenTallerService
                        .cambiarEstado(
                                id,
                                request
                        )
        );
    }

    @PatchMapping("/{id}/tecnico")
    public ResponseEntity<OrdenTallerResponse>
    asignarTecnico(
            @PathVariable Long id,
            @Valid
            @RequestBody
            AsignarTecnicoOrdenRequest request
    ) {

        return ResponseEntity.ok(
                ordenTallerService
                        .asignarTecnico(
                                id,
                                request
                        )
        );
    }

    @PostMapping("/{id}/servicios")
    public ResponseEntity<OrdenTallerResponse>
    agregarServicio(
            @PathVariable Long id,
            @Valid
            @RequestBody
            OrdenServicioRequest request,
            @RequestHeader(
                    value = "X-Usuario-Id",
                    required = false
            )
            Long usuarioId
    ) {

        return ResponseEntity.ok(
                ordenTallerService
                        .agregarServicio(
                                id,
                                request,
                                usuarioId
                        )
        );
    }

    @DeleteMapping(
            "/{id}/servicios/{detalleId}"
    )
    public ResponseEntity<OrdenTallerResponse>
    eliminarServicio(
            @PathVariable Long id,
            @PathVariable Long detalleId,
            @RequestHeader(
                    value = "X-Usuario-Id",
                    required = false
            )
            Long usuarioId
    ) {

        return ResponseEntity.ok(
                ordenTallerService
                        .eliminarServicio(
                                id,
                                detalleId,
                                usuarioId
                        )
        );
    }

    @PostMapping("/{id}/productos")
    public ResponseEntity<OrdenTallerResponse>
    agregarProducto(
            @PathVariable Long id,
            @Valid
            @RequestBody
            OrdenProductoRequest request,
            @RequestHeader(
                    value = "X-Usuario-Id",
                    required = false
            )
            Long usuarioId
    ) {

        return ResponseEntity.ok(
                ordenTallerService
                        .agregarProducto(
                                id,
                                request,
                                usuarioId
                        )
        );
    }

    @DeleteMapping(
            "/{id}/productos/{detalleId}"
    )
    public ResponseEntity<OrdenTallerResponse>
    eliminarProducto(
            @PathVariable Long id,
            @PathVariable Long detalleId,
            @RequestHeader(
                    value = "X-Usuario-Id",
                    required = false
            )
            Long usuarioId
    ) {

        return ResponseEntity.ok(
                ordenTallerService
                        .eliminarProducto(
                                id,
                                detalleId,
                                usuarioId
                        )
        );
    }

    @GetMapping("/pendientes")
    public ResponseEntity<List<OrdenTallerResponse>>
    obtenerOrdenesPendientes() {

        return ResponseEntity.ok(
                ordenTallerService
                        .obtenerOrdenesPendientesDePago()
        );
    }

    @GetMapping(
            "/motocicleta/{motoId}/historial"
    )
    public ResponseEntity<List<OrdenTallerResponse>>
    historialMotocicleta(
            @PathVariable Long motoId
    ) {

        return ResponseEntity.ok(
                ordenTallerService
                        .historialMotocicleta(
                                motoId
                        )
        );
    }
}

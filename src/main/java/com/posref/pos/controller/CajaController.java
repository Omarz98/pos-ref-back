package com.posref.pos.controller;

import com.posref.pos.dto.caja.AperturaCajaRequest;
import com.posref.pos.dto.caja.CajaResponse;
import com.posref.pos.dto.caja.CierreCajaRequest;
import com.posref.pos.dto.caja.MovimientoCajaRequest;
import com.posref.pos.model.seguridad.Usuario;
import com.posref.pos.repository.seguridad.UsuarioRepository;
import com.posref.pos.service.caja.ICajaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/caja")
@RequiredArgsConstructor
public class CajaController {

    private final ICajaService cajaService;
    private final UsuarioRepository usuarioRepository;

    @PostMapping("/abrir")
    public ResponseEntity<CajaResponse> abrirCaja(
            @Valid @RequestBody AperturaCajaRequest request,
            Authentication authentication
    ) {
        Usuario usuario = buscarUsuario(authentication);

        return ResponseEntity.ok(
                cajaService.abrirCaja(usuario.getId(), request)
        );
    }

    @GetMapping("/actual")
    public ResponseEntity<CajaResponse> obtenerCajaActual(
            Authentication authentication
    ) {
        Usuario usuario = buscarUsuario(authentication);

        return ResponseEntity.ok(
                cajaService.obtenerCajaAbierta(usuario.getId())
        );
    }

    @PostMapping("/movimientos")
    public ResponseEntity<CajaResponse> registrarMovimiento(
            @Valid @RequestBody MovimientoCajaRequest request,
            Authentication authentication
    ) {
        Usuario usuario = buscarUsuario(authentication);

        return ResponseEntity.ok(
                cajaService.registrarMovimiento(
                        usuario.getId(),
                        request
                )
        );
    }

    @PostMapping("/cerrar")
    public ResponseEntity<CajaResponse> cerrarCaja(
            @Valid @RequestBody CierreCajaRequest request,
            Authentication authentication
    ) {
        Usuario usuario = buscarUsuario(authentication);

        return ResponseEntity.ok(
                cajaService.cerrarCaja(usuario.getId(), request)
        );
    }

    @GetMapping("/historial")
    public ResponseEntity<List<CajaResponse>> historial() {
        return ResponseEntity.ok(
                cajaService.obtenerHistorial()
        );
    }

    private Usuario buscarUsuario(Authentication authentication) {
        if (authentication == null
                || authentication.getName() == null) {
            throw new RuntimeException(
                    "No fue posible identificar al usuario"
            );
        }

        return usuarioRepository
                .findByUsername(authentication.getName())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Usuario autenticado no encontrado"
                        )
                );
    }
}

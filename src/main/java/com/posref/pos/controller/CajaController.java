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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/caja")
@RequiredArgsConstructor
public class CajaController {

    private final ICajaService cajaService;
    private final UsuarioRepository usuarioRepository;

    // APERTURA DE CAJA
    @PreAuthorize("hasAuthority('CAJA_ABRIR')")
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

    // CONSULTAR CAJA ACTUAL
    @PreAuthorize("hasAuthority('CAJA_VER')")
    @GetMapping("/actual")
    public ResponseEntity<CajaResponse> obtenerCajaActual(
            Authentication authentication
    ) {
        Usuario usuario = buscarUsuario(authentication);

        return ResponseEntity.ok(
                cajaService.obtenerCajaAbierta(usuario.getId())
        );
    }

    // REGISTRAR MOVIMIENTO DE CAJA
    @PreAuthorize("hasAuthority('CAJA_MOVIMIENTO_CREAR')")
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

    // CIERRE DE CAJA
    @PreAuthorize("hasAuthority('CAJA_CERRAR')")
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

    // HISTORIAL GENERAL DE CAJAS
    @PreAuthorize("hasAuthority('CAJA_HISTORIAL_VER')")
    @GetMapping("/historial")
    public ResponseEntity<List<CajaResponse>> historial() {
        return ResponseEntity.ok(
                cajaService.obtenerHistorial()
        );
    }

    // OBTENER USUARIO AUTENTICADO
    private Usuario buscarUsuario(Authentication authentication) {

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication.getName() == null) {

            throw new AuthenticationCredentialsNotFoundException(
                    "No fue posible identificar al usuario"
            );
        }

        Usuario usuario = usuarioRepository
                .findByUsername(authentication.getName())
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "Usuario autenticado no encontrado"
                        )
                );

        if (!Boolean.TRUE.equals(usuario.getActivo())) {
            throw new org.springframework.security.access.AccessDeniedException(
                    "El usuario se encuentra inactivo"
            );
        }

        return usuario;
    }
}

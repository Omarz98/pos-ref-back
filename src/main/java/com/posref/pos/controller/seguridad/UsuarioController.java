package com.posref.pos.controller.seguridad;

import com.posref.pos.dto.usuario.*;
import com.posref.pos.service.seguridad.IUsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final IUsuarioService usuarioService;

    @PreAuthorize("""
        hasAuthority('USUARIO_VER')
        or hasRole('ADMINISTRADOR')
    """)
    @GetMapping
    public ResponseEntity<List<UsuarioResponse>>
    obtenerTodos() {

        return ResponseEntity.ok(
                usuarioService.obtenerTodos()
        );
    }

    @PreAuthorize("""
        hasAuthority('USUARIO_VER')
        or hasRole('ADMINISTRADOR')
    """)
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse>
    obtenerPorId(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                usuarioService.obtenerPorId(id)
        );
    }

    @PreAuthorize("""
        hasAuthority('USUARIO_CREAR')
        or hasRole('ADMINISTRADOR')
    """)
    @PostMapping
    public ResponseEntity<UsuarioResponse>
    crear(
            @RequestBody UsuarioRequest request
    ) {

        UsuarioResponse usuario =
                usuarioService.crear(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(usuario);
    }

    @PreAuthorize("""
        hasAuthority('USUARIO_EDITAR')
        or hasRole('ADMINISTRADOR')
    """)
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse>
    actualizar(
            @PathVariable Long id,
            @RequestBody
            UsuarioActualizarRequest request
    ) {

        return ResponseEntity.ok(
                usuarioService.actualizar(
                        id,
                        request
                )
        );
    }

    @PreAuthorize("""
        hasAuthority('USUARIO_CAMBIAR_ESTADO')
        or hasRole('ADMINISTRADOR')
    """)
    @PatchMapping("/{id}/estado")
    public ResponseEntity<UsuarioResponse>
    cambiarEstado(
            @PathVariable Long id,
            @RequestBody
            CambiarEstadoUsuarioRequest request
    ) {

        return ResponseEntity.ok(
                usuarioService.cambiarEstado(
                        id,
                        request.getActivo()
                )
        );
    }

    @PreAuthorize("""
        hasAuthority('USUARIO_RESTABLECER_PASSWORD')
        or hasRole('ADMINISTRADOR')
    """)
    @PatchMapping("/{id}/password")
    public ResponseEntity<Void>
    cambiarPassword(
            @PathVariable Long id,
            @RequestBody
            CambiarPasswordRequest request
    ) {

        usuarioService.cambiarPassword(
                id,
                request
        );

        return ResponseEntity.noContent()
                .build();
    }

}

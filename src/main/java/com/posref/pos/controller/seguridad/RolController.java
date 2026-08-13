package com.posref.pos.controller.seguridad;

import com.posref.pos.dto.rol.RolResponse;
import com.posref.pos.repository.seguridad.RolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RolController {

    private final RolRepository rolRepository;

    @PreAuthorize("""
        hasAuthority('ROL_VER')
        or hasAuthority('USUARIO_CREAR')
        or hasAuthority('USUARIO_EDITAR')
        or hasRole('ADMINISTRADOR')
    """)
    @GetMapping
    public ResponseEntity<List<RolResponse>>
    obtenerRoles() {

        List<RolResponse> roles =
                rolRepository
                        .findAll()
                        .stream()
                        .map(rol ->
                                RolResponse
                                        .builder()
                                        .id(
                                                rol.getId()
                                        )
                                        .nombre(
                                                rol.getNombre()
                                        )
                                        .descripcion(
                                                rol.getDescripcion()
                                        )
                                        .activo(
                                                rol.getActivo()
                                        )
                                        .build()
                        )
                        .toList();

        return ResponseEntity.ok(roles);
    }

}

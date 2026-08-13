package com.posref.pos.dto.usuario;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class UsuarioActualizarRequest {
    private String nombre;

    private String email;

    private Boolean activo;

    private Set<Long> rolesIds = new HashSet<>();
}

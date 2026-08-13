package com.posref.pos.dto.usuario;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class UsuarioRequest {
    private String nombre;

    private String username;

    private String email;

    private String password;

    private Boolean activo = true;

    private Set<Long> rolesIds = new HashSet<>();
}

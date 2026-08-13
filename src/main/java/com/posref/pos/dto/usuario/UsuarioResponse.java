package com.posref.pos.dto.usuario;

import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponse {

    private Long id;

    private String nombre;

    private String username;

    private String email;

    private Boolean activo;

    private LocalDateTime fechaCreacion;

    @Builder.Default
    private Set<RolResumenResponse> roles = new HashSet<>();

}

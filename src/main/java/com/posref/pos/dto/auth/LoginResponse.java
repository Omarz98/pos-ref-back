package com.posref.pos.dto.auth;

import java.util.Set;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String tipo;
    private Long usuarioId;
    private String nombre;
    private String username;
    private Set<String> roles;
    private Set<String> permisos;
}

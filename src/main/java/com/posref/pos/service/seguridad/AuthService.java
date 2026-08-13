package com.posref.pos.service.seguridad;

import com.posref.pos.dto.auth.LoginRequest;
import com.posref.pos.dto.auth.LoginResponse;
import com.posref.pos.model.seguridad.Permiso;
import com.posref.pos.model.seguridad.Rol;
import com.posref.pos.security.JwtService;
import com.posref.pos.security.UsuarioPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public LoginResponse login(LoginRequest request) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getUsername(),
                                request.getPassword()
                        )
                );

        UsuarioPrincipal principal =
                (UsuarioPrincipal) authentication.getPrincipal();

        Set<String> roles =
                principal.getUsuario()
                        .getRoles()
                        .stream()
                        .map(Rol::getNombre)
                        .collect(Collectors.toSet());

        Set<String> permisos =
                principal.getUsuario()
                        .getRoles()
                        .stream()
                        .flatMap(rol ->
                                rol.getPermisos().stream()
                        )
                        .map(Permiso::getNombre)
                        .collect(Collectors.toSet());

        return LoginResponse.builder()
                .token(jwtService.generarToken(principal))
                .tipo("Bearer")
                .usuarioId(principal.getId())
                .nombre(principal.getNombre())
                .username(principal.getUsername())
                .roles(roles)
                .permisos(permisos)
                .build();
    }
}

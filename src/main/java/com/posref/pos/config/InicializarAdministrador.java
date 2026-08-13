package com.posref.pos.config;

import com.posref.pos.model.seguridad.Rol;
import com.posref.pos.model.seguridad.Usuario;
import com.posref.pos.repository.seguridad.RolRepository;
import com.posref.pos.repository.seguridad.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class InicializarAdministrador implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        if (usuarioRepository.existsByUsername("admin")) {
            return;
        }

        Rol rolAdministrador =
                rolRepository
                        .findByNombre("ADMINISTRADOR")
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "No existe el rol ADMINISTRADOR"
                                )
                        );

        Usuario administrador =
                Usuario.builder()
                        .nombre("Administrador")
                        .username("admin")
                        .email("admin@derians.com")
                        .password(
                                passwordEncoder.encode(
                                        "Admin123"
                                )
                        )
                        .activo(true)
                        .roles(Set.of(rolAdministrador))
                        .build();

        usuarioRepository.save(administrador);
    }

}

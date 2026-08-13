package com.posref.pos.service.seguridad;

import com.posref.pos.dto.usuario.*;
import com.posref.pos.exception.RecursoNoEncontradoException;
import com.posref.pos.exception.ReglaNegocioException;
import com.posref.pos.model.seguridad.Rol;
import com.posref.pos.model.seguridad.Usuario;
import com.posref.pos.repository.seguridad.RolRepository;
import com.posref.pos.repository.seguridad.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService implements IUsuarioService{

    private final UsuarioRepository usuarioRepository;

    private final RolRepository rolRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioResponse> obtenerTodos() {

        return usuarioRepository
                .findAll()
                .stream()
                .map(this::convertirResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponse obtenerPorId(
            Long id
    ) {

        Usuario usuario =
                buscarUsuario(id);

        return convertirResponse(usuario);
    }

    @Override
    @Transactional
    public UsuarioResponse crear(
            UsuarioRequest request
    ) {

        validarDatosCreacion(request);

        String username =
                request.getUsername().trim();

        if (
                usuarioRepository
                        .existsByUsername(username)
        ) {
            throw new ReglaNegocioException(
                    "El nombre de usuario ya está registrado"
            );
        }

        String email = normalizarEmail(
                request.getEmail()
        );

        if (
                email != null &&
                        usuarioRepository.existsByEmail(email)
        ) {
            throw new ReglaNegocioException(
                    "El correo electrónico ya está registrado"
            );
        }

        Set<Rol> roles =
                obtenerRoles(
                        request.getRolesIds()
                );

        Usuario usuario =
                Usuario.builder()
                        .nombre(
                                request
                                        .getNombre()
                                        .trim()
                        )
                        .username(username)
                        .email(email)
                        .password(
                                passwordEncoder.encode(
                                        request.getPassword()
                                )
                        )
                        .activo(
                                request.getActivo() == null
                                        ? true
                                        : request.getActivo()
                        )
                        .roles(roles)
                        .build();

        return convertirResponse(
                usuarioRepository.save(usuario)
        );
    }

    @Override
    @Transactional
    public UsuarioResponse actualizar(
            Long id,
            UsuarioActualizarRequest request
    ) {

        Usuario usuario =
                buscarUsuario(id);

        if (
                request.getNombre() == null ||
                        request.getNombre().isBlank()
        ) {
            throw new ReglaNegocioException(
                    "El nombre es obligatorio"
            );
        }

        String email =
                normalizarEmail(
                        request.getEmail()
                );

        if (
                email != null &&
                        usuarioRepository
                                .existsByEmailAndIdNot(
                                        email,
                                        id
                                )
        ) {
            throw new ReglaNegocioException(
                    "El correo electrónico ya está registrado"
            );
        }

        Set<Rol> roles =
                obtenerRoles(
                        request.getRolesIds()
                );

        usuario.setNombre(
                request.getNombre().trim()
        );

        usuario.setEmail(email);

        usuario.setRoles(roles);

        if (request.getActivo() != null) {
            usuario.setActivo(
                    request.getActivo()
            );
        }

        return convertirResponse(
                usuarioRepository.save(usuario)
        );
    }

    @Override
    @Transactional
    public UsuarioResponse cambiarEstado(
            Long id,
            Boolean activo
    ) {

        if (activo == null) {
            throw new ReglaNegocioException(
                    "Debes indicar el estado del usuario"
            );
        }

        Usuario usuario =
                buscarUsuario(id);

        usuario.setActivo(activo);

        return convertirResponse(
                usuarioRepository.save(usuario)
        );
    }

    @Override
    @Transactional
    public void cambiarPassword(
            Long id,
            CambiarPasswordRequest request
    ) {

        if (
                request.getNuevaPassword() == null ||
                        request.getNuevaPassword().isBlank()
        ) {
            throw new ReglaNegocioException(
                    "La nueva contraseña es obligatoria"
            );
        }

        if (
                request
                        .getNuevaPassword()
                        .length() < 8
        ) {
            throw new ReglaNegocioException(
                    "La contraseña debe tener al menos 8 caracteres"
            );
        }

        Usuario usuario =
                buscarUsuario(id);

        usuario.setPassword(
                passwordEncoder.encode(
                        request.getNuevaPassword()
                )
        );

        usuarioRepository.save(usuario);
    }

    private Usuario buscarUsuario(
            Long id
    ) {

        return usuarioRepository
                .findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Usuario no encontrado: " + id
                        )
                );
    }

    private Set<Rol> obtenerRoles(
            Set<Long> rolesIds
    ) {

        if (
                rolesIds == null ||
                        rolesIds.isEmpty()
        ) {
            throw new ReglaNegocioException(
                    "Debes asignar al menos un rol"
            );
        }

        List<Rol> rolesEncontrados =
                rolRepository.findAllById(
                        rolesIds
                );

        if (
                rolesEncontrados.size() !=
                        rolesIds.size()
        ) {
            throw new ReglaNegocioException(
                    "Uno o más roles no existen"
            );
        }

        return new HashSet<>(
                rolesEncontrados
        );
    }

    private void validarDatosCreacion(
            UsuarioRequest request
    ) {

        if (
                request.getNombre() == null ||
                        request.getNombre().isBlank()
        ) {
            throw new ReglaNegocioException(
                    "El nombre es obligatorio"
            );
        }

        if (
                request.getUsername() == null ||
                        request.getUsername().isBlank()
        ) {
            throw new ReglaNegocioException(
                    "El nombre de usuario es obligatorio"
            );
        }

        if (
                request.getPassword() == null ||
                        request.getPassword().isBlank()
        ) {
            throw new ReglaNegocioException(
                    "La contraseña es obligatoria"
            );
        }

        if (
                request.getPassword().length() < 8
        ) {
            throw new ReglaNegocioException(
                    "La contraseña debe tener al menos 8 caracteres"
            );
        }
    }

    private String normalizarEmail(
            String email
    ) {

        if (
                email == null ||
                        email.isBlank()
        ) {
            return null;
        }

        return email
                .trim()
                .toLowerCase();
    }

    private UsuarioResponse convertirResponse(
            Usuario usuario
    ) {

        Set<RolResumenResponse> roles =
                usuario.getRoles()
                        .stream()
                        .map(rol ->
                                RolResumenResponse
                                        .builder()
                                        .id(
                                                rol.getId()
                                        )
                                        .nombre(
                                                rol.getNombre()
                                        )
                                        .build()
                        )
                        .collect(
                                Collectors.toSet()
                        );

        return UsuarioResponse
                .builder()
                .id(usuario.getId())
                .nombre(
                        usuario.getNombre()
                )
                .username(
                        usuario.getUsername()
                )
                .email(
                        usuario.getEmail()
                )
                .activo(
                        usuario.getActivo()
                )
                .fechaCreacion(
                        usuario.getFechaCreacion()
                )
                .roles(roles)
                .build();
    }
}

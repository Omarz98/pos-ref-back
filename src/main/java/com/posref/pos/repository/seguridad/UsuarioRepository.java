package com.posref.pos.repository.seguridad;

import com.posref.pos.model.seguridad.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository
        extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(
            String email,
            Long id
    );

    List<Usuario>
    findByActivoTrueOrderByNombreAsc();

    @Query("""
    SELECT DISTINCT u
    FROM Usuario u
    JOIN u.roles r
    WHERE u.activo = true
      AND r.nombre = :nombreRol
      AND r.activo = true
    ORDER BY u.nombre ASC
    """)
    List<Usuario> findTecnicosActivos(
            @Param("nombreRol") String nombreRol
    );
}

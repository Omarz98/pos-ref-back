package com.posref.pos.repository.seguridad;

import com.posref.pos.model.seguridad.Permiso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermisoRepository extends JpaRepository<Permiso, Long> {
    Optional<Permiso> findByNombre(String nombre);
}

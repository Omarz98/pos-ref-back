package com.posref.pos.repository.caja;

import com.posref.pos.model.caja.CajaSesion;
import com.posref.pos.model.caja.EstadoCaja;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CajaSesionRepository
        extends JpaRepository<CajaSesion, Long> {

    Optional<CajaSesion> findFirstByUsuarioIdAndEstadoOrderByFechaAperturaDesc(
            Long usuarioId,
            EstadoCaja estado
    );

    boolean existsByUsuarioIdAndEstado(
            Long usuarioId,
            EstadoCaja estado
    );

    List<CajaSesion> findAllByOrderByFechaAperturaDesc();
}

package com.posref.pos.repository;

import com.posref.pos.model.Ventas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface VentaRepository
        extends JpaRepository<Ventas, Long>,
        JpaSpecificationExecutor<Ventas> {

    boolean existsByOrdenTallerId(
            Long ordenServicioId
    );

    Optional<Ventas> findByOrdenTallerId(
            Long ordenTallerId
    );
}

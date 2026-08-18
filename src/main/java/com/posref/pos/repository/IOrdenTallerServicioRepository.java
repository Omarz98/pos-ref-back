package com.posref.pos.repository;

import com.posref.pos.model.OrdenTallerServicio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IOrdenTallerServicioRepository
        extends JpaRepository<OrdenTallerServicio, Long> {

    List<OrdenTallerServicio> findByOrdenTallerId(Long ordenTallerId);
}

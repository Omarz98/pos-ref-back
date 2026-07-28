package com.posref.pos.repository;

import com.posref.pos.model.MotoServicios;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IMotoServicioRepository extends JpaRepository<MotoServicios, Long> {
}

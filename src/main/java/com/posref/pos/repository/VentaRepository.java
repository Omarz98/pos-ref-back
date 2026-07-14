package com.posref.pos.repository;

import com.posref.pos.model.Ventas;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VentaRepository extends JpaRepository<Ventas, Long> {
}

package com.posref.pos.repository;

import com.posref.pos.model.MotoMarcas;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MotoMarcasRepository extends JpaRepository<MotoMarcas,Long> {
    List<MotoMarcas> findByActivoTrueOrderByNombreAsc();

    boolean existsByNombreIgnoreCase(String nombre);
}

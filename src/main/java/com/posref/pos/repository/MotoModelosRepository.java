package com.posref.pos.repository;

import com.posref.pos.model.MotoModelos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MotoModelosRepository extends JpaRepository<MotoModelos,Long> {
    //List<MotoModelos> findByMarcaId(Long marcaId);
}

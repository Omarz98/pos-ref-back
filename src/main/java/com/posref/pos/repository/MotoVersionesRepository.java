package com.posref.pos.repository;

import com.posref.pos.model.MotoVersiones;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MotoVersionesRepository extends JpaRepository<MotoVersiones,Long> {
    List<MotoVersiones> findByMotoModeloId(Long motoModeloId);
}

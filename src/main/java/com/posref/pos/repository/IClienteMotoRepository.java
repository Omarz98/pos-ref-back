package com.posref.pos.repository;

import com.posref.pos.model.Motocicleta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IClienteMotoRepository extends JpaRepository<Motocicleta, Long> {
    List<Motocicleta> findByClienteIdAndActivoTrue(Long clienteId);
}

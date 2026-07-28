package com.posref.pos.repository;

import com.posref.pos.model.Motocicleta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MotocicletaRepository extends JpaRepository<Motocicleta, Long> {

    List<Motocicleta> findByClienteIdAndActivoTrueOrderByIdDesc(Long clienteId);

    Optional<Motocicleta> findByIdAndActivoTrue(Long id);

    Optional<Motocicleta> findByNumeroSerieIgnoreCase(String numeroSerie);

    Optional<Motocicleta> findByPlacasIgnoreCase(String placas);

    boolean existsByNumeroSerieIgnoreCase(String numeroSerie);

    boolean existsByPlacasIgnoreCase(String placas);

    List<Motocicleta> findByClienteId(Long clienteId);

    List<Motocicleta> findByClienteIdAndActivoTrue(Long clienteId);
}

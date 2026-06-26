package com.posref.pos.repository;

import com.posref.pos.model.Productos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductosRepository extends JpaRepository<Productos,Long> {
    Optional<Productos> findByCodigo(String codigo);

    Optional<Productos> findByCodigoBarras(String codigoBarras);
}

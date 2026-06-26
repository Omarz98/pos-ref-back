package com.posref.pos.repository;

import com.posref.pos.model.ProductoCompatibilidadMoto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductoCompatibilidadMotoRepository extends JpaRepository<ProductoCompatibilidadMoto, Long> {
    List<ProductoCompatibilidadMoto> findByProductoId(Long productoId);

    List<ProductoCompatibilidadMoto> findByMotoVersionId(Long motoVersionId);
}

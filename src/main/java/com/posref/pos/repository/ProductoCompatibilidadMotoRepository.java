package com.posref.pos.repository;

import com.posref.pos.model.ProductoCompatibilidadMoto;
import com.posref.pos.model.Productos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductoCompatibilidadMotoRepository extends JpaRepository<ProductoCompatibilidadMoto, Long> {
    List<ProductoCompatibilidadMoto> findByProductoId(Long productoId);

    List<ProductoCompatibilidadMoto> findByMotoVersionId(Long motoVersionId);

    List<ProductoCompatibilidadMoto>
    findByMotoVersionIdAndActivoTrue(Long motoVersionId);

    boolean existsByProductoIdAndMotoVersionIdAndActivoTrue(
            Long productoId,
            Long motoVersionId
    );

    @Query("""
    SELECT p
    FROM Productos p
    WHERE p.activo = true
      AND (
            p.compatibilidadUniversal = true
            OR EXISTS (
                SELECT pcm.id
                FROM ProductoCompatibilidadMoto pcm
                WHERE pcm.producto = p
                  AND pcm.motoVersion.id = :motoVersionId
                  AND pcm.activo = true
            )
          )
    ORDER BY p.nombre
    """)
    List<Productos> buscarProductosCompatiblesOUniversales(
            @Param("motoVersionId") Long motoVersionId
    );
}

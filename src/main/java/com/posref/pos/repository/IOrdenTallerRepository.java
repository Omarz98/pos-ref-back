package com.posref.pos.repository;

import com.posref.pos.model.EstadoOrdenTrabajo;
import com.posref.pos.model.OrdenTaller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface IOrdenTallerRepository
        extends JpaRepository<OrdenTaller, Long> {

    Optional<OrdenTaller> findByFolio(String folio);

    List<OrdenTaller> findByEstadoOrderByFechaRecepcionDesc(
            EstadoOrdenTrabajo estado
    );

    List<OrdenTaller> findAllByOrderByFechaRecepcionDesc();

    List<OrdenTaller> findByEstadoIn(
            List<EstadoOrdenTrabajo> estados
    );

    long countByEstado(EstadoOrdenTrabajo estado);

    long countByEstadoNotIn(Collection<EstadoOrdenTrabajo> estados);

    List<OrdenTaller>
    findByFechaEntregaEstimadaBetweenAndEstadoNotInOrderByFechaEntregaEstimadaAsc(
            LocalDateTime inicio,
            LocalDateTime fin,
            Collection<EstadoOrdenTrabajo> estados
    );

    long countByFechaEntregaEstimadaBeforeAndEstadoNotIn(
            LocalDateTime fecha,
            Collection<EstadoOrdenTrabajo> estados
    );

    List<OrdenTaller>
    findByMotocicletaIdOrderByFechaRecepcionDesc(Long motocicletaId);

    @Query("""
        SELECT o
        FROM OrdenTaller o
        WHERE (:estado IS NULL OR o.estado = :estado)
          AND (
                :buscar IS NULL
                OR :buscar = ''
                OR LOWER(o.folio) LIKE LOWER(CONCAT('%', :buscar, '%'))
                OR LOWER(o.cliente.nombre) LIKE LOWER(CONCAT('%', :buscar, '%'))
                OR LOWER(COALESCE(o.cliente.telefono, '')) LIKE LOWER(CONCAT('%', :buscar, '%'))
                OR LOWER(COALESCE(o.motocicleta.placas, '')) LIKE LOWER(CONCAT('%', :buscar, '%'))
          )
        ORDER BY o.fechaRecepcion DESC
    """)
    List<OrdenTaller> buscar(
            @Param("estado") EstadoOrdenTrabajo estado,
            @Param("buscar") String buscar
    );
}

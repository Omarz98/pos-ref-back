package com.posref.pos.repository;

import com.posref.pos.model.EstadoOrdenTrabajo;
import com.posref.pos.model.OrdenTaller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IOrdenTallerRepository
        extends JpaRepository<OrdenTaller, Long> {

    Optional<OrdenTaller> findByFolio(String folio);

    List<OrdenTaller> findByEstadoOrderByFechaRecepcionDesc(
            EstadoOrdenTrabajo estado
    );

    List<OrdenTaller> findAllByOrderByFechaRecepcionDesc();

   /* @Query("""
        SELECT ot
        FROM OrdenesTaller ot
        JOIN FETCH ot.clienteId
        JOIN FETCH ot.motoCliente
        LEFT JOIN FETCH ot.servicios
        WHERE ot.id = :id
    """)
    Optional<OrdenTaller> buscarConServicios(@Param("id") Long id);*/

    List<OrdenTaller> findByEstadoIn(
            List<EstadoOrdenTrabajo> estados
    );



}

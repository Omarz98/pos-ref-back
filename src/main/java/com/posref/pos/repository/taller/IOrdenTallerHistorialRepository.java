package com.posref.pos.repository.taller;

import com.posref.pos.model.taller.OrdenTallerHistorial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IOrdenTallerHistorialRepository
        extends JpaRepository<OrdenTallerHistorial, Long> {

    // Historial de eventos de una orden
    List<OrdenTallerHistorial>
    findByOrdenTaller_IdOrderByFechaDesc(Long ordenId);

    // Historial de eventos de todas las órdenes de una moto
    List<OrdenTallerHistorial>
    findByOrdenTaller_Motocicleta_IdOrderByFechaDesc(
            Long motocicletaId
    );
}

package com.posref.pos.repository.taller;

import com.posref.pos.model.taller.OrdenTallerHistorial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IOrdenTallerHistorialRepository
        extends JpaRepository<OrdenTallerHistorial, Long> {

    List<OrdenTallerHistorial>
    findByOrdenTallerIdOrderByFechaDesc(Long ordenTallerId);
}

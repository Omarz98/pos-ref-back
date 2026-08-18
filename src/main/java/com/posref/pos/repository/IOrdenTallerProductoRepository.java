package com.posref.pos.repository;

import com.posref.pos.model.OrdenTallerProducto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IOrdenTallerProductoRepository
        extends JpaRepository<OrdenTallerProducto, Long> {

    List<OrdenTallerProducto> findByOrdenTallerId(Long ordenTallerId);
}

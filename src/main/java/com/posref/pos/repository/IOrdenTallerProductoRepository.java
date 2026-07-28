package com.posref.pos.repository;

import com.posref.pos.model.OrdenTallerProducto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IOrdenTallerProductoRepository extends JpaRepository<OrdenTallerProducto, Long> {
}

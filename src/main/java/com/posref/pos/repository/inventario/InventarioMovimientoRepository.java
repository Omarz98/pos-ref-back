package com.posref.pos.repository.inventario;

import com.posref.pos.model.inventario.InventarioMovimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public interface InventarioMovimientoRepository extends JpaRepository<InventarioMovimiento, Long> {

    List<InventarioMovimiento> findByProductoIdOrderByFechaDesc(Long productoId);

    List<InventarioMovimiento> findAllByOrderByFechaDesc();

}

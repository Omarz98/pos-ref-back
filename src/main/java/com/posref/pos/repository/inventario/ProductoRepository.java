package com.posref.pos.repository.inventario;

import com.posref.pos.model.Productos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Productos, Long> {

    List<Productos> findByActivoTrue();

}

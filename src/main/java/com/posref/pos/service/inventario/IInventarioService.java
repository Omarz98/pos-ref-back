package com.posref.pos.service.inventario;

import com.posref.pos.dto.inventario.InventarioMovimientoResponse;
import com.posref.pos.dto.inventario.InventarioProductoResponse;
import com.posref.pos.dto.inventario.InventarioResumenResponse;
import com.posref.pos.dto.inventario.MovimientoInventarioRequest;

import java.util.List;

public interface IInventarioService {

    List<InventarioProductoResponse> obtenerInventario();

    InventarioProductoResponse obtenerProducto(Long productoId);

    List<InventarioMovimientoResponse> obtenerMovimientos(
            Long productoId
    );

    InventarioMovimientoResponse registrarMovimiento(
            MovimientoInventarioRequest request
    );

    InventarioResumenResponse obtenerResumen();
}

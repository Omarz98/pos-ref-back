package com.posref.pos.dto.inventario;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class InventarioResumenResponse {

    private long totalProductos;

    private long productosStockBajo;

    private long productosAgotados;

    private BigDecimal valorInventario;
}

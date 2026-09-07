package com.posref.pos.dto.inventario;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class InventarioProductoResponse {

    private Long productoId;

    private String codigo;

    private String codigoBarras;

    private String nombre;

    private String descripcion;

    private BigDecimal precioCompra;

    private BigDecimal precioVenta;

    private BigDecimal stockActual;

    private BigDecimal stockMinimo;

    private String unidadMedida;

    private String estado;

}

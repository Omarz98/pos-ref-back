package com.posref.pos.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class ProductoCompatibleResponse {

    private Long id;

    private String codigo;
    private String codigoBarras;
    private String nombre;
    private String descripcion;

    private BigDecimal precioCompra;
    private BigDecimal precioVenta;

    private Integer stockActual;
    private Integer stockMinimo;

    private String unidadMedida;

    private Boolean compatibilidadUniversal;

}

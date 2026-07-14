package com.posref.pos.dto;

import com.posref.pos.model.TipoItemVenta;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DetalleRequest {

    private String id;

    private TipoItemVenta tipo;

    private String nombre;

    private Integer cantidad;

    private BigDecimal precio;
}

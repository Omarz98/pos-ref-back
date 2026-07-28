package com.posref.pos.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MotoServiciosDTO {
    private Long id;
    private String nombre;
    private BigDecimal precioVenta;
    private String codigo;
    private boolean activo;
    private String descripcion;
    private Integer duracionEstimadaMinutos;
    private boolean aplicaIva;
}

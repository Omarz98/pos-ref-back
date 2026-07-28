package com.posref.pos.dto;


import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductoResponse {

    private Long id;

    private Long productoId;

    private String codigo;

    private String productoNombre;

    private Integer cantidad;

    private BigDecimal precioUnitario;

    private BigDecimal subtotal;
}

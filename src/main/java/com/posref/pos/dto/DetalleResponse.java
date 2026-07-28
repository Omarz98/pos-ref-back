package com.posref.pos.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetalleResponse {
    private Long id;
    private String referenciaId;
    private String productoNombre;
    private String tipo;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;

}

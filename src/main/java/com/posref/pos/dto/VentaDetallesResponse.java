package com.posref.pos.dto;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VentaDetallesResponse {

    private Long id;

    private Long productoId;
    private Long servicioId;
    private Long detalleOrdenId;

    private String codigo;
    private String tipo;
    private String nombre;

    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;

}

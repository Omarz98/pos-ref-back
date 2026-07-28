package com.posref.pos.dto;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServicioResponse {

    private Long id;

    private Long servicioId;

    private String servicioNombre;

    private String descripcion;

    private Integer cantidad;

    private BigDecimal precioUnitario;

    private BigDecimal subtotal;

}

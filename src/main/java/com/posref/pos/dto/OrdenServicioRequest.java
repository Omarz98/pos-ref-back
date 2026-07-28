package com.posref.pos.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrdenServicioRequest {

    @NotNull(message = "El servicio es obligatorio")
    private Long servicioId;

    private String descripcion;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser mayor a cero")
    private Integer cantidad;

    @NotNull(message = "El precio unitario es obligatorio")
    @DecimalMin(
            value = "0.00",
            inclusive = true,
            message = "El precio no puede ser negativo"
    )
    private BigDecimal precioUnitario;

    private BigDecimal subtotal;

}

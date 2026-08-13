package com.posref.pos.dto.caja;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
public record AperturaCajaRequest(

        @NotBlank
        String nombreCaja,

        @NotNull
        @DecimalMin("0.00")
        BigDecimal montoInicial,

        String observaciones
) {

}


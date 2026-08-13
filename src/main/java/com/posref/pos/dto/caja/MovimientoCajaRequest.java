package com.posref.pos.dto.caja;

import com.posref.pos.model.caja.TipoMovimientoCaja;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record MovimientoCajaRequest(

        @NotNull
        TipoMovimientoCaja tipo,

        @NotNull
        @DecimalMin("0.01")
        BigDecimal monto,

        @NotBlank
        String concepto
) {
}

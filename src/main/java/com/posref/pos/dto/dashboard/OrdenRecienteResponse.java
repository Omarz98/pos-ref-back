package com.posref.pos.dto.dashboard;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrdenRecienteResponse(
        Long id,

        String cliente,

        String moto,

        String estado,

        BigDecimal total,

        LocalDateTime fecha
) {
}

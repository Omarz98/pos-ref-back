package com.posref.pos.dto.dashboard;

import java.math.BigDecimal;
import java.time.LocalDate;
public record VentaDiaResponse(
        LocalDate fecha,

        BigDecimal total
) {
}

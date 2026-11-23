package com.posref.pos.dto.reportes;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ReporteVentaDiariaResponse(
        LocalDate fecha,
        BigDecimal totalVentas,
        Long cantidadVentas
) {}

package com.posref.pos.dto.reportes;

import java.math.BigDecimal;

public record ReporteIngresoCajaResponse(
        String metodo,
        BigDecimal cobrosPos,
        BigDecimal anticiposTaller,
        BigDecimal totalIngresos
) {}

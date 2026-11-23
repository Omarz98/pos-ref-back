package com.posref.pos.dto.reportes;

import java.math.BigDecimal;

public record ReporteVentasResumenResponse(
        BigDecimal totalVentas,
        BigDecimal pagosPos,
        BigDecimal anticiposAplicados,
        BigDecimal totalCubierto,
        BigDecimal saldoPendiente,
        Long cantidadVentas,
        Long ventasPagadas,
        Long ventasPendientes,
        BigDecimal ticketPromedio
) {}

package com.posref.pos.service.reportes;

import java.math.BigDecimal;

public record ReporteMetodoPagoResponse(
        String metodo,
        BigDecimal total,
        Long cantidadPagos
) {}

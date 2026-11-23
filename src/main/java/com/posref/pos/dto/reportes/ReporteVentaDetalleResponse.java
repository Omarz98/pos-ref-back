package com.posref.pos.dto.reportes;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ReporteVentaDetalleResponse(
        Long id,
        LocalDateTime fecha,
        Long clienteId,
        String cliente,
        String tipoVenta,
        BigDecimal subtotal,
        BigDecimal iva,
        BigDecimal total,
        BigDecimal anticipoAplicado,
        BigDecimal pagosPos,
        BigDecimal totalCubierto,
        BigDecimal saldoPendiente,
        String estado
) {}
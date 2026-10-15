package com.posref.pos.dto.taller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record HistorialMotoDTO(
        Long id,
        String folio,
        LocalDateTime fechaRecepcion,
        String estado,
        Integer kilometraje,
        String fallaReportada,
        String diagnosticoInicial,
        BigDecimal total,
        List<DetalleHistorialDTO> servicios,
        List<DetalleHistorialDTO> productos
) {}

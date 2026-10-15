package com.posref.pos.dto.taller;

import java.math.BigDecimal;

public record DetalleHistorialDTO(
        Long id,
        String nombre,
        BigDecimal cantidad,
        BigDecimal precio
) {}

package com.posref.pos.dto;

import com.posref.pos.model.MetodoPago;

import java.math.BigDecimal;

public record PagoAnticipoRequest(
        MetodoPago metodoPago,
        BigDecimal monto
) {
}
package com.posref.pos.dto;

import com.posref.pos.model.MetodoPago;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PagoRequest {
    private MetodoPago metodo;

    private BigDecimal monto;
}

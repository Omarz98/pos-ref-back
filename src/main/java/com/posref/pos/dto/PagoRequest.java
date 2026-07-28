package com.posref.pos.dto;

import com.posref.pos.model.MetodoPago;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagoRequest {
    private MetodoPago metodo;

    private BigDecimal monto;
}

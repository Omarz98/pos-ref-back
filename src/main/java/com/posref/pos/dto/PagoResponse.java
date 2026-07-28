package com.posref.pos.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PagoResponse {
    private Long id;
    private String metodoPago;
    private BigDecimal monto;
    private LocalDateTime fecha;
    private Long ventaId;
}

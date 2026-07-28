package com.posref.pos.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VentaResponse {
    private Long id;
    private Long clienteId;
    private String clienteNombre;
    private LocalDateTime fecha;
    private BigDecimal subtotal;
    private BigDecimal iva;
    private BigDecimal total;
    private BigDecimal totalPagado;
    private BigDecimal saldoPendiente;
    private String estado;

    private List<DetalleResponse> items;
    private List<PagoResponse> pagos;
}

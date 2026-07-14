package com.posref.pos.dto;

import com.posref.pos.dto.DetalleRequest;
import com.posref.pos.dto.PagoRequest;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class VentaRequest {

    private Long  clienteId;
    private BigDecimal subtotal;
    private BigDecimal iva;
    private BigDecimal total;
    private BigDecimal totalPagado;
    private BigDecimal saldoPendiente;

    private List<DetalleRequest> items;
    private List<PagoRequest> pagos;
}
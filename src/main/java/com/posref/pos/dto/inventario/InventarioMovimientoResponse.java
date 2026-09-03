package com.posref.pos.dto.inventario;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class InventarioMovimientoResponse {
    private Long id;

    private Long productoId;
    private String productoCodigo;
    private String productoNombre;

    private Long categoriaId;
    private String categoriaNombre;

    private String tipoMovimiento;
    private BigDecimal cantidad;

    private BigDecimal stockAnterior;
    private BigDecimal stockNuevo;

    private String motivo;
    private String referencia;

    private LocalDateTime fechaMovimiento;
}

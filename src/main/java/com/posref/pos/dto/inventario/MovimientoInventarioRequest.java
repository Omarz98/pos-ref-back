package com.posref.pos.dto.inventario;

import com.posref.pos.model.inventario.TipoMovimientoInventario;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class MovimientoInventarioRequest {

    private Long productoId;

    private TipoMovimientoInventario tipo;

    private BigDecimal cantidad;

    private BigDecimal costoUnitario;

    private String motivo;

    private String referenciaTipo;

    private Long referenciaId;

    private Long usuarioId;

}

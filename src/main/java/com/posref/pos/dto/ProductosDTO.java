package com.posref.pos.dto;

import com.posref.pos.model.Categorias;
import com.posref.pos.model.Marcas;
import com.posref.pos.model.Proveedores;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductosDTO {

    private Long id;
    private String codigo;
    private String codigoBarras;
    private String nombre;
    private String descripcion;
    private Long categoriaId;
    private Long marcaId;
    private Long proveedorId;

    private BigDecimal precioCompra;
    private BigDecimal precioVenta;

    private Integer stock;
    private Integer stockMinimo;

    private String unidadMedida;

    private boolean activo;

    private LocalDateTime fechaCreacion;
}

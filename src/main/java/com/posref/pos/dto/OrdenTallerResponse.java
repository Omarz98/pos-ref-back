package com.posref.pos.dto;

import com.posref.pos.model.EstadoOrdenTrabajo;
import com.posref.pos.model.NivelCombustible;
import com.posref.pos.model.PrioridadOrdenTaller;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrdenTallerResponse {

    private Long id;

    private String folio;

    private Long clienteId;

    private String clienteNombre;

    private Long motoClienteId;

    private String motocicleta;

    private LocalDateTime fechaRecepcion;

    private LocalDateTime fechaEntregaEstimada;

    private Integer kilometraje;

    private NivelCombustible nivelCombustible;

    private PrioridadOrdenTaller prioridad;

    private EstadoOrdenTrabajo estado;

    private String fallaReportada;

    private String diagnosticoInicial;

    private String observaciones;

    private BigDecimal subtotalServicios;

    private BigDecimal subtotalProductos;

    private BigDecimal subtotal;

    private BigDecimal iva;

    private BigDecimal total;

    private BigDecimal anticipo;

    private BigDecimal saldoPendiente;

    @Builder.Default
    private List<ServicioResponse> servicios = new ArrayList<>();

    @Builder.Default
    private List<ProductoResponse> productos = new ArrayList<>();

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ServicioResponse {

        private Long id;

        private Long servicioId;

        private String servicioNombre;

        private String descripcion;

        private Integer cantidad;

        private BigDecimal precioUnitario;

        private BigDecimal subtotal;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductoResponse {

        private Long id;

        private Long productoId;

        private String codigo;

        private String productoNombre;

        private Integer cantidad;

        private BigDecimal precioUnitario;

        private BigDecimal subtotal;
    }
}
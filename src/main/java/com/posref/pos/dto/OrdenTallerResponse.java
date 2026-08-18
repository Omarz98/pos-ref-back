package com.posref.pos.dto;

import com.posref.pos.dto.taller.HistorialOrdenResponse;
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
    private String clienteTelefono;
    private String clienteEmail;

    private Long motoClienteId;
    private String motocicleta;
    private String placas;
    private String color;
    private String numeroSerie;

    private Long tecnicoId;
    private String tecnicoNombre;

    private LocalDateTime fechaRecepcion;
    private LocalDateTime fechaEntregaEstimada;
    private LocalDateTime fechaEntregaReal;

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

    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;

    private Boolean atrasada;

    private Long ventaId;
    private String estadoVenta;

    @Builder.Default
    private List<ServicioResponse> servicios = new ArrayList<>();

    @Builder.Default
    private List<ProductoResponse> productos = new ArrayList<>();

    @Builder.Default
    private List<HistorialOrdenResponse> historial = new ArrayList<>();

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ServicioResponse {
        private Long id;
        private Long servicioId;
        private String codigo;
        private String servicioNombre;
        private String descripcion;
        private Integer cantidad;
        private BigDecimal precioUnitario;
        private BigDecimal subtotal;
        private Boolean aplicaIva;
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
        private String codigoBarras;
        private String productoNombre;
        private Integer cantidad;
        private BigDecimal precioUnitario;
        private BigDecimal subtotal;
        private Integer stockActual;
    }
}

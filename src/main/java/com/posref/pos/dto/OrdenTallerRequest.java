package com.posref.pos.dto;

import com.posref.pos.model.EstadoOrdenTrabajo;
import com.posref.pos.model.NivelCombustible;
import com.posref.pos.model.PrioridadOrdenTaller;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class OrdenTallerRequest {

    @NotNull(message = "El cliente es obligatorio")
    private Long clienteId;

    @NotNull(message = "La motocicleta es obligatoria")
    private Long motoClienteId;

    @NotNull(message = "La fecha de recepción es obligatoria")
    private LocalDateTime fechaRecepcion;

    private LocalDateTime fechaEntregaEstimada;

    private Integer kilometraje;

    private NivelCombustible nivelCombustible;

    private PrioridadOrdenTaller prioridad;

    private EstadoOrdenTrabajo estado;

    @NotBlank(message = "La falla reportada es obligatoria")
    private String fallaReportada;

    private String diagnosticoInicial;

    private String observaciones;

    private BigDecimal subtotalServicios;

    private BigDecimal subtotalProductos;

    private BigDecimal subtotal;

    private BigDecimal iva;

    private BigDecimal total;

    @DecimalMin(
            value = "0.00",
            inclusive = true,
            message = "El anticipo no puede ser negativo"
    )
    private BigDecimal anticipo;

    private BigDecimal saldoPendiente;

    @Valid
    @Builder.Default
    private List<OrdenServicioRequest> servicios = new ArrayList<>();

    @Valid
    @Builder.Default
    private List<OrdenProductoRequest> productos = new ArrayList<>();

}

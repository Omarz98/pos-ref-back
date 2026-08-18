package com.posref.pos.dto.taller;

import com.posref.pos.model.NivelCombustible;
import com.posref.pos.model.PrioridadOrdenTaller;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActualizarOrdenTallerRequest {

    private Long tecnicoId;
    private LocalDateTime fechaEntregaEstimada;
    private Integer kilometraje;
    private NivelCombustible nivelCombustible;
    private PrioridadOrdenTaller prioridad;
    private String fallaReportada;
    private String diagnosticoInicial;
    private String observaciones;
    private Long usuarioId;
}

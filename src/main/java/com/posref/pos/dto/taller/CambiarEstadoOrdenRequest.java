package com.posref.pos.dto.taller;

import com.posref.pos.model.EstadoOrdenTrabajo;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CambiarEstadoOrdenRequest {

    @NotNull(message = "El nuevo estado es obligatorio")
    private EstadoOrdenTrabajo estado;

    private String observacion;

    private Long usuarioId;
}

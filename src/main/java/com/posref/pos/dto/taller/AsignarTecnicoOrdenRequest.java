package com.posref.pos.dto.taller;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AsignarTecnicoOrdenRequest {

    @NotNull(message = "El técnico es obligatorio")
    private Long tecnicoId;

    private Long usuarioId;
}

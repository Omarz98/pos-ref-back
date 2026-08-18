package com.posref.pos.dto.taller;

import com.posref.pos.model.EstadoOrdenTrabajo;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistorialOrdenResponse {

    private Long id;
    private Long usuarioId;
    private String usuarioNombre;
    private EstadoOrdenTrabajo estadoAnterior;
    private EstadoOrdenTrabajo estadoNuevo;
    private String tipo;
    private String descripcion;
    private LocalDateTime fecha;
}

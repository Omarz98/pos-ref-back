package com.posref.pos.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteMotoResponse {
    private Long id;

    private Long clienteId;
    private String clienteNombre;

    private Long modeloId;
    private String modeloNombre;

    private Long versionId;
    private String versionNombre;

    private Integer anio;
    private String placa;
    private String color;
    private String numeroSerie;
    private Integer kilometraje;
    private Boolean activo;
}

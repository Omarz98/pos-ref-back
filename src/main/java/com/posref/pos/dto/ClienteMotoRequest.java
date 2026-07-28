package com.posref.pos.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClienteMotoRequest {

    private Long modeloId;

    private Long versionId;

    private Long motoVersionId;

    private Integer anio;

    private String placas;

    private String color;

    private String numeroSerie;

    private Integer kilometrajeActual;

    private Boolean activo;

}

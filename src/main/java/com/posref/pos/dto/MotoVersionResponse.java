package com.posref.pos.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MotoVersionResponse {

    private Long id;
    private Long motoModeloId;

    private Integer anio;
    private String cilindraje;
    private String version;

    private String descripcionCompleta;

}

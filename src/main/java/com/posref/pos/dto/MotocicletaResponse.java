package com.posref.pos.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class MotocicletaResponse {

    private Long id;

    private Long clienteId;
    private String clienteNombre;

    private Long motoMarcaId;
    private String motoMarcaNombre;

    private Long motoModeloId;
    private String motoModeloNombre;

    private Long motoVersionId;
    private Integer anio;
    private String cilindraje;
    private String version;

    private String numeroSerie;
    private String numeroMotor;
    private String placas;
    private String color;

    private Integer kilometrajeActual;
    private String observaciones;

    private Boolean activo;

    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;

}

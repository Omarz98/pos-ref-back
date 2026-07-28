package com.posref.pos.dto;


import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

@Getter
@Setter
public class MotocicletaRequest {

    private Long clienteId;

    private Long motoVersionId;

    private String numeroSerie;

    private String numeroMotor;

    private String placas;

    private String color;

    private Integer kilometrajeActual;

    private String observaciones;
}

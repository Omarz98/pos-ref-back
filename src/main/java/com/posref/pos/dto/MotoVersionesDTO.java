package com.posref.pos.dto;

import com.posref.pos.model.MotoModelos;
import jakarta.persistence.Entity;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MotoVersionesDTO {

    private Long id;
    private Long motoModeloId;
    private Integer anio;
    private String cilindraje;
    private String version;
    private Boolean activo;
}

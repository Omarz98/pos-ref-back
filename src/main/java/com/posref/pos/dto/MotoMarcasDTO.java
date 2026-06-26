package com.posref.pos.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MotoMarcasDTO {
    private Long id;
    private String nombre;
    private boolean activo;
}

package com.posref.pos.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoriasDTO {

    private Long id;
    private String nombre;
    private String descripcion;
    private boolean activo;
}

package com.posref.pos.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MotoModelosDTO {

    private Long id;
    private Long motoMarcaId;
    private String nombre;
    private boolean activo;

}

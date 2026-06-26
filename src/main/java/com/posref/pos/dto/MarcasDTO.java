package com.posref.pos.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MarcasDTO {
    private Long id;
    private String nombre;
    private boolean activo;
}

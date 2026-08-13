package com.posref.pos.dto.rol;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RolResponse {

    private Long id;

    private String nombre;

    private String descripcion;

    private Boolean activo;

}

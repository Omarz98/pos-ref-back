package com.posref.pos.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProveedoresDTO {

    private Long id;
    private String nombre;
    private String telefono;
    private String email;
    private String direccion;
    private String contacto;
    private boolean activo;

}

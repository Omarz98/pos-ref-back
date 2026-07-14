package com.posref.pos.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MotoServiciosDTO {
    private Long id;
    private String nombre;
    private String precioVenta;
    private String codigo;
    private boolean activo;
}

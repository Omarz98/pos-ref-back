package com.posref.pos.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientesDTO {

    private Long id;
    private String nombre;
    private String telefono;
    private String email;
    private String direccion;
    private String rfc;
    private boolean activo;
    private LocalDateTime fechaCreacion;

}

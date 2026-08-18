package com.posref.pos.dto.taller;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TecnicoTallerResponse {
    private Long id;
    private String nombre;
    private String username;
    private String email;
}

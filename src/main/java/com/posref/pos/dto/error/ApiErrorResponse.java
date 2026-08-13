package com.posref.pos.dto.error;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiErrorResponse {

    private LocalDateTime fecha;

    private Integer status;

    private String mensaje;

}

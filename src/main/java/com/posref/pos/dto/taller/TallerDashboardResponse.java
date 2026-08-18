package com.posref.pos.dto.taller;

import com.posref.pos.dto.OrdenTallerResponse;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TallerDashboardResponse {

    private long recibidas;
    private long diagnostico;
    private long esperandoAutorizacion;
    private long autorizadas;
    private long reparacion;
    private long esperandoRefacciones;
    private long pruebas;
    private long terminadas;
    private long listasEntrega;
    private long pagoParcial;
    private long atrasadas;
    private long totalActivas;

    @Builder.Default
    private List<OrdenTallerResponse> proximasEntregas =
            new ArrayList<>();
}

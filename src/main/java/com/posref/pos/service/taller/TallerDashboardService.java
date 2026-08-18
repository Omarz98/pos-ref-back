package com.posref.pos.service.taller;

import com.posref.pos.dto.OrdenTallerResponse;
import com.posref.pos.dto.taller.TallerDashboardResponse;
import com.posref.pos.model.EstadoOrdenTrabajo;
import com.posref.pos.model.OrdenTaller;
import com.posref.pos.repository.IOrdenTallerRepository;
import com.posref.pos.service.OrdenTallerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TallerDashboardService {

    private final IOrdenTallerRepository ordenTallerRepository;
    private final OrdenTallerService ordenTallerService;

    private static final Set<EstadoOrdenTrabajo> ESTADOS_CERRADOS =
            EnumSet.of(
                    EstadoOrdenTrabajo.ENTREGADA,
                    EstadoOrdenTrabajo.CANCELADA
            );

    @Transactional(readOnly = true)
    public TallerDashboardResponse obtenerDashboard() {

        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime hasta = ahora.plusDays(7);

        List<OrdenTaller> proximas =
                ordenTallerRepository
                        .findByFechaEntregaEstimadaBetweenAndEstadoNotInOrderByFechaEntregaEstimadaAsc(
                                ahora,
                                hasta,
                                ESTADOS_CERRADOS
                        );

        List<OrdenTallerResponse> proximasResponse =
                proximas.stream()
                        .limit(10)
                        .map(ordenTallerService::convertirResponsePublico)
                        .toList();

        return TallerDashboardResponse.builder()
                .recibidas(
                        ordenTallerRepository.countByEstado(
                                EstadoOrdenTrabajo.RECIBIDA
                        )
                )
                .diagnostico(
                        ordenTallerRepository.countByEstado(
                                EstadoOrdenTrabajo.EN_DIAGNOSTICO
                        )
                )
                .esperandoAutorizacion(
                        ordenTallerRepository.countByEstado(
                                EstadoOrdenTrabajo.ESPERANDO_AUTORIZACION
                        )
                )
                .autorizadas(
                        ordenTallerRepository.countByEstado(
                                EstadoOrdenTrabajo.AUTORIZADA
                        )
                )
                .reparacion(
                        ordenTallerRepository.countByEstado(
                                EstadoOrdenTrabajo.EN_REPARACION
                        )
                )
                .esperandoRefacciones(
                        ordenTallerRepository.countByEstado(
                                EstadoOrdenTrabajo.ESPERANDO_REFACCIONES
                        )
                )
                .pruebas(
                        ordenTallerRepository.countByEstado(
                                EstadoOrdenTrabajo.EN_PRUEBAS
                        )
                )
                .terminadas(
                        ordenTallerRepository.countByEstado(
                                EstadoOrdenTrabajo.TERMINADA
                        )
                )
                .listasEntrega(
                        ordenTallerRepository.countByEstado(
                                EstadoOrdenTrabajo.LISTA_PARA_ENTREGA
                        )
                )
                .pagoParcial(
                        ordenTallerRepository.countByEstado(
                                EstadoOrdenTrabajo.PAGO_PARCIAL
                        )
                )
                .atrasadas(
                        ordenTallerRepository
                                .countByFechaEntregaEstimadaBeforeAndEstadoNotIn(
                                        ahora,
                                        ESTADOS_CERRADOS
                                )
                )
                .totalActivas(
                        ordenTallerRepository.countByEstadoNotIn(
                                ESTADOS_CERRADOS
                        )
                )
                .proximasEntregas(proximasResponse)
                .build();
    }
}

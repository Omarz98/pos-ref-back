package com.posref.pos.dto.dashboard;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record DashboardResponse(


        BigDecimal ventasHoy,
        BigDecimal ventasMes,
        BigDecimal ticketPromedio,
        BigDecimal saldoPendiente,

        Long ordenesActivas,
        Long ordenesPendientes,
        Long ordenesReparacion,
        Long ordenesTerminadas,

        BigDecimal ingresosTaller,

        Long productosBajos,
        Long productosAgotados,

        Long clientesHoy,

        List<VentaDiaResponse> ventasUltimos7Dias,

        List<OrdenEstadoResponse> ordenesPorEstado,

        List<RankingResponse> productosMasVendidos,

        List<RankingResponse> serviciosMasSolicitados,

        List<OrdenRecienteResponse> ordenesRecientes

) {
}

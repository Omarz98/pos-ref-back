package com.posref.pos.service;

import com.posref.pos.dto.dashboard.*;
import com.posref.pos.repository.DashboardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService implements IDashboardService{

    private final DashboardRepository dashboardRepository;


    @Override
    @Transactional(readOnly = true)
    public DashboardResponse obtenerDashboard() {

        return new DashboardResponse(

                dashboardRepository.ventasHoy(),

                dashboardRepository.ventasMes(),

                dashboardRepository.ticketPromedio(),

                dashboardRepository.saldoPendiente(),

                dashboardRepository.ordenesActivas(),

                dashboardRepository.ordenesPendientes(),

                dashboardRepository.ordenesReparacion(),

                dashboardRepository.ordenesTerminadas(),

                dashboardRepository.ingresosTaller(),

                dashboardRepository.productosBajos(),

                dashboardRepository.productosAgotados(),

                dashboardRepository.clientesHoy(),

                obtenerVentasUltimos7Dias(),

                obtenerOrdenesPorEstado(),

                obtenerProductosMasVendidos(),

                obtenerServiciosMasSolicitados(),

                List.of()
        );
    }


    // =========================================================
    // VENTAS 7 DÍAS
    // =========================================================

    private List<VentaDiaResponse> obtenerVentasUltimos7Dias() {

        List<Object[]> resultados =
                dashboardRepository.ventasUltimos7Dias();

        Map<LocalDate, BigDecimal> ventas =
                resultados.stream()
                        .collect(
                                Collectors.toMap(
                                        row -> convertirFecha(row[0]),
                                        row -> convertirBigDecimal(row[1])
                                )
                        );

        List<VentaDiaResponse> lista = new ArrayList<>();

        for (int i = 6; i >= 0; i--) {

            LocalDate fecha =
                    LocalDate.now().minusDays(i);

            lista.add(
                    new VentaDiaResponse(
                            fecha,
                            ventas.getOrDefault(
                                    fecha,
                                    BigDecimal.ZERO
                            )
                    )
            );
        }

        return lista;
    }


    // =========================================================
    // ÓRDENES POR ESTADO
    // =========================================================

    private List<OrdenEstadoResponse> obtenerOrdenesPorEstado() {

        return dashboardRepository
                .ordenesPorEstado()
                .stream()
                .map(row ->
                        new OrdenEstadoResponse(
                                row[0].toString(),
                                ((Number) row[1]).longValue()
                        )
                )
                .toList();
    }


    // =========================================================
    // PRODUCTOS
    // =========================================================

    private List<RankingResponse> obtenerProductosMasVendidos() {

        return convertirRanking(
                dashboardRepository.productosMasVendidos()
        );
    }


    // =========================================================
    // SERVICIOS
    // =========================================================

    private List<RankingResponse> obtenerServiciosMasSolicitados() {

        return convertirRanking(
                dashboardRepository.serviciosMasSolicitados()
        );
    }


    // =========================================================
    // CONVERTIR RANKING
    // =========================================================

    private List<RankingResponse> convertirRanking(
            List<Object[]> resultados
    ) {

        return resultados
                .stream()
                .map(row ->

                        new RankingResponse(

                                row[0].toString(),

                                row[1].toString(),

                                ((Number) row[2]).longValue(),

                                convertirBigDecimal(row[3])

                        )

                )
                .toList();
    }


    // =========================================================
    // UTILIDADES
    // =========================================================

    private BigDecimal convertirBigDecimal(Object valor) {

        if (valor == null) {
            return BigDecimal.ZERO;
        }

        if (valor instanceof BigDecimal bigDecimal) {
            return bigDecimal;
        }

        return new BigDecimal(
                valor.toString()
        );
    }


    private LocalDate convertirFecha(Object valor) {

        if (valor instanceof Date sqlDate) {
            return sqlDate.toLocalDate();
        }

        if (valor instanceof LocalDate localDate) {
            return localDate;
        }

        return LocalDate.parse(
                valor.toString()
        );
    }

}

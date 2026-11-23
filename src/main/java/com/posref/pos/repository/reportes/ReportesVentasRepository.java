package com.posref.pos.repository.reportes;

import com.posref.pos.dto.reportes.ReporteVentaDiariaResponse;
import com.posref.pos.dto.reportes.ReporteVentasResumenResponse;
import com.posref.pos.service.reportes.ReporteMetodoPagoResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ReportesVentasRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public ReporteVentasResumenResponse obtenerResumen(
            LocalDate desde,
            LocalDate hasta
    ) {

        Object[] row = (Object[]) entityManager
                .createNativeQuery("""
                SELECT
                    COALESCE(SUM(v.total), 0),

                    COALESCE(SUM(v.total_pagado), 0),

                    COALESCE(SUM(v.anticipo_aplicado), 0),

                    COALESCE(
                        SUM(
                            v.total_pagado +
                            v.anticipo_aplicado
                        ), 0
                    ),

                    COALESCE(SUM(v.saldo_pendiente), 0),

                    COUNT(*),

                    COALESCE(SUM(
                        CASE
                            WHEN v.estado = 'PAGADA'
                            THEN 1 ELSE 0
                        END
                    ), 0),

                    COALESCE(SUM(
                        CASE
                            WHEN v.estado = 'PENDIENTE'
                            THEN 1 ELSE 0
                        END
                    ), 0),

                    COALESCE(AVG(v.total), 0)

                FROM ventas v

                WHERE v.fecha >= :desde
                  AND v.fecha < :hasta

                  AND v.estado IN (
                      'PAGADA',
                      'PENDIENTE'
                  )
            """)
                .setParameter("desde", desde.atStartOfDay())
                .setParameter("hasta", hasta.plusDays(1).atStartOfDay())
                .getSingleResult();

        return new ReporteVentasResumenResponse(
                decimal(row[0]),
                decimal(row[1]),
                decimal(row[2]),
                decimal(row[3]),
                decimal(row[4]),
                numero(row[5]),
                numero(row[6]),
                numero(row[7]),
                decimal(row[8])
        );
    }

    private BigDecimal decimal(Object valor) {
        return valor == null
                ? BigDecimal.ZERO
                : new BigDecimal(valor.toString());
    }

    private Long numero(Object valor) {
        return valor == null
                ? 0L
                : ((Number) valor).longValue();
    }

    @SuppressWarnings("unchecked")
    public List<ReporteMetodoPagoResponse> obtenerMetodosPago(
            LocalDate desde,
            LocalDate hasta
    ) {

        List<Object[]> resultados = entityManager
                .createNativeQuery("""
                SELECT
                    vp.metodo,
                    COALESCE(SUM(vp.monto), 0),
                    COUNT(vp.id)

                FROM venta_pago vp

                INNER JOIN ventas v
                    ON v.id = vp.venta_id

                WHERE vp.fecha >= :desde
                  AND vp.fecha < :hasta

                  AND v.estado IN (
                      'PAGADA',
                      'PENDIENTE'
                  )

                GROUP BY vp.metodo

                ORDER BY SUM(vp.monto) DESC
            """)
                .setParameter("desde", desde.atStartOfDay())
                .setParameter("hasta", hasta.plusDays(1).atStartOfDay())
                .getResultList();

        return resultados.stream()
                .map(row -> new ReporteMetodoPagoResponse(
                        row[0].toString(),
                        decimal(row[1]),
                        numero(row[2])
                ))
                .toList();
    }

    @SuppressWarnings("unchecked")
    public List<ReporteVentaDiariaResponse> obtenerVentasDiarias(
            LocalDate desde,
            LocalDate hasta
    ) {

        List<Object[]> resultados = entityManager
                .createNativeQuery("""
                SELECT
                    DATE(v.fecha),
                    COALESCE(SUM(v.total), 0),
                    COUNT(v.id)
                FROM ventas v
                WHERE v.fecha >= :desde
                  AND v.fecha < :hasta
                  AND v.estado IN ('PAGADA', 'PENDIENTE')
                GROUP BY DATE(v.fecha)
                ORDER BY DATE(v.fecha)
            """)
                .setParameter("desde", desde.atStartOfDay())
                .setParameter("hasta", hasta.plusDays(1).atStartOfDay())
                .getResultList();

        Map<LocalDate, ReporteVentaDiariaResponse> porFecha =
                new HashMap<>();

        for (Object[] row : resultados) {

            LocalDate fecha = row[0] instanceof java.sql.Date sqlDate
                    ? sqlDate.toLocalDate()
                    : LocalDate.parse(row[0].toString());

            porFecha.put(
                    fecha,
                    new ReporteVentaDiariaResponse(
                            fecha,
                            decimal(row[1]),
                            numero(row[2])
                    )
            );
        }

        List<ReporteVentaDiariaResponse> respuesta =
                new ArrayList<>();

        for (LocalDate fecha = desde;
             !fecha.isAfter(hasta);
             fecha = fecha.plusDays(1)) {

            respuesta.add(
                    porFecha.getOrDefault(
                            fecha,
                            new ReporteVentaDiariaResponse(
                                    fecha,
                                    BigDecimal.ZERO,
                                    0L
                            )
                    )
            );
        }

        return respuesta;
    }
}
package com.posref.pos.repository.reportes;

import com.posref.pos.model.EstadoVenta;
import com.posref.pos.model.Ventas;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;

public final class VentaReporteSpecifications {

    private VentaReporteSpecifications() {}

    public static Specification<Ventas> filtrar(
            LocalDate desde,
            LocalDate hasta,
            EstadoVenta estado,
            Long ventaId
    ) {

        return (root, query, cb) -> {

            var condiciones = cb.conjunction();

            if (desde != null) {
                LocalDateTime inicio = desde.atStartOfDay();

                condiciones = cb.and(
                        condiciones,
                        cb.greaterThanOrEqualTo(
                                root.get("fecha"),
                                inicio
                        )
                );
            }

            if (hasta != null) {
                LocalDateTime finExclusivo =
                        hasta.plusDays(1).atStartOfDay();

                condiciones = cb.and(
                        condiciones,
                        cb.lessThan(
                                root.get("fecha"),
                                finExclusivo
                        )
                );
            }

            if (estado != null) {
                condiciones = cb.and(
                        condiciones,
                        cb.equal(
                                root.get("estado"),
                                estado
                        )
                );
            }

            if (ventaId != null) {
                condiciones = cb.and(
                        condiciones,
                        cb.equal(
                                root.get("id"),
                                ventaId
                        )
                );
            }

            return condiciones;
        };
    }
}
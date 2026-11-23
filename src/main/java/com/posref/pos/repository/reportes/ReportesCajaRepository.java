package com.posref.pos.repository.reportes;

import com.posref.pos.dto.reportes.ReporteIngresoCajaResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public class ReportesCajaRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @SuppressWarnings("unchecked")
    public List<ReporteIngresoCajaResponse> obtenerIngresos(
            LocalDate desde,
            LocalDate hasta
    ) {

        List<Object[]> resultados = entityManager
                .createNativeQuery("""
                    SELECT
                        m.metodo,

                        COALESCE(SUM(
                            CASE
                                WHEN m.tipo = 'VENTA'
                                THEN m.monto
                                ELSE 0
                            END
                        ), 0) AS cobros_pos,

                        COALESCE(SUM(
                            CASE
                                WHEN m.tipo = 'ENTRADA'
                                 AND m.referencia_operacion
                                     LIKE 'ANTICIPO-TALLER-%'
                                THEN m.monto
                                ELSE 0
                            END
                        ), 0) AS anticipos_taller

                    FROM caja_movimientos m

                    WHERE m.fecha >= :desde
                      AND m.fecha < :hasta

                      AND (
                          m.tipo = 'VENTA'

                          OR (
                              m.tipo = 'ENTRADA'
                              AND m.referencia_operacion
                                  LIKE 'ANTICIPO-TALLER-%'
                          )
                      )

                    GROUP BY m.metodo
                    ORDER BY m.metodo
                """)
                .setParameter("desde", desde.atStartOfDay())
                .setParameter("hasta", hasta.plusDays(1).atStartOfDay())
                .getResultList();

        return resultados.stream()
                .map(row -> {

                    BigDecimal cobrosPos = decimal(row[1]);
                    BigDecimal anticipos = decimal(row[2]);

                    return new ReporteIngresoCajaResponse(
                            row[0].toString(),
                            cobrosPos,
                            anticipos,
                            cobrosPos.add(anticipos)
                    );
                })
                .toList();
    }

    private BigDecimal decimal(Object valor) {
        return valor == null
                ? BigDecimal.ZERO
                : new BigDecimal(valor.toString());
    }
}
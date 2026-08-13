package com.posref.pos.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public class DashboardRepository {

    @PersistenceContext
    private EntityManager entityManager;


    // =====================================================
    // VENTAS HOY
    // =====================================================

    public BigDecimal ventasHoy() {

        Object result = entityManager
                .createNativeQuery("""
                    SELECT COALESCE(SUM(monto), 0)
                    FROM venta_pago
                    WHERE DATE(fecha) = CURDATE()
                """)
                .getSingleResult();

        return toBigDecimal(result);
    }


    // =====================================================
    // VENTAS DEL MES
    // =====================================================

    public BigDecimal ventasMes() {

        Object result = entityManager
                .createNativeQuery("""
                    SELECT COALESCE(SUM(total_pagado), 0)
                    FROM ventas
                    WHERE YEAR(fecha) = YEAR(CURDATE())
                      AND MONTH(fecha) = MONTH(CURDATE())
                """)
                .getSingleResult();

        return toBigDecimal(result);
    }


    // =====================================================
    // TICKET PROMEDIO HOY
    // =====================================================

    public BigDecimal ticketPromedio() {

        Object result = entityManager
                .createNativeQuery("""
                    SELECT COALESCE(AVG(total_pagado), 0)
                    FROM ventas
                    WHERE DATE(fecha) = CURDATE()
                """)
                .getSingleResult();

        return toBigDecimal(result);
    }


    // =====================================================
    // SALDO PENDIENTE
    // =====================================================

    public BigDecimal saldoPendiente() {

        Object result = entityManager
                .createNativeQuery("""
                    SELECT COALESCE(SUM(saldo_pendiente), 0)
                    FROM ventas
                    WHERE saldo_pendiente > 0
                """)
                .getSingleResult();

        return toBigDecimal(result);
    }


    // =====================================================
    // CLIENTES ATENDIDOS HOY
    // =====================================================

    public Long clientesHoy() {

        Object result = entityManager
                .createNativeQuery("""
                    SELECT COUNT(DISTINCT cliente_id)
                    FROM ventas
                    WHERE DATE(fecha) = CURDATE()
                      AND cliente_id IS NOT NULL
                """)
                .getSingleResult();

        return toLong(result);
    }


    // =====================================================
    // VENTAS ÚLTIMOS 7 DÍAS
    // =====================================================

    @SuppressWarnings("unchecked")
    public List<Object[]> ventasUltimos7Dias() {

        return (List<Object[]>) entityManager
                .createNativeQuery("""
                SELECT
                    DATE(fecha) AS fecha,
                    COALESCE(SUM(total_pagado), 0) AS total
                FROM ventas
                WHERE fecha >= DATE_SUB(CURDATE(), INTERVAL 6 DAY)
                GROUP BY DATE(fecha)
                ORDER BY DATE(fecha)
            """)
                .getResultList();
    }


    // =====================================================
    // PRODUCTOS BAJOS
    // =====================================================

    public Long productosBajos() {

        Object result = entityManager
                .createNativeQuery("""
                    SELECT COUNT(*)
                    FROM productos
                    WHERE activo = 1
                      AND stock_actual > 0
                      AND stock_actual <= stock_minimo
                """)
                .getSingleResult();

        return toLong(result);
    }


    // =====================================================
    // PRODUCTOS AGOTADOS
    // =====================================================

    public Long productosAgotados() {

        Object result = entityManager
                .createNativeQuery("""
                    SELECT COUNT(*)
                    FROM productos
                    WHERE activo = 1
                      AND stock_actual <= 0
                """)
                .getSingleResult();

        return toLong(result);
    }


    // =====================================================
    // PRODUCTOS MÁS VENDIDOS
    // =====================================================

    @SuppressWarnings("unchecked")
    public List<Object[]> productosMasVendidos() {

        return (List<Object[]>) entityManager
                .createNativeQuery("""
                SELECT
                    p.codigo,
                    p.nombre,
                    COALESCE(SUM(vd.cantidad), 0) AS cantidad,
                    COALESCE(
                        SUM(vd.cantidad * vd.precio_unitario),
                        0
                    ) AS total
                FROM venta_detalle vd
                INNER JOIN productos p
                    ON p.codigo = vd.referencia_id
                INNER JOIN ventas v
                    ON v.id = vd.venta_id
                WHERE vd.referencia_id IS NOT NULL
                  AND vd.tipo = 'PRODUCTO'
                  AND v.fecha >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)
                GROUP BY
                    p.id,
                    p.nombre
                ORDER BY cantidad DESC
                LIMIT 5
            """)
                .getResultList();
    }


    // =====================================================
    // SERVICIOS MÁS SOLICITADOS
    // =====================================================

    @SuppressWarnings("unchecked")
    public List<Object[]> serviciosMasSolicitados() {

        return (List<Object[]>) entityManager
                .createNativeQuery("""
                SELECT
                    s.id,
                    s.nombre,
                    COALESCE(SUM(ots.cantidad), 0) AS cantidad,
                    COALESCE(SUM(ots.subtotal), 0) AS total
                FROM orden_taller_servicios ots
                INNER JOIN moto_servicios s
                    ON s.id = ots.servicio_id
                INNER JOIN ordenes_taller ot
                    ON ot.id = ots.orden_taller_id
                WHERE ot.fecha_creacion >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)
                  AND ot.estado NOT IN ('CANCELADA', 'RECHAZADA')
                GROUP BY
                    s.id,
                    s.nombre
                ORDER BY cantidad DESC
                LIMIT 5
            """)
                .getResultList();
    }


    // =====================================================
    // ÓRDENES POR ESTADO
    // =====================================================

    @SuppressWarnings("unchecked")
    public List<Object[]> ordenesPorEstado() {

        return (List<Object[]>) entityManager
                .createNativeQuery("""
                SELECT
                    estado,
                    COUNT(*)
                FROM ordenes_taller
                GROUP BY estado
                ORDER BY COUNT(*) DESC
            """)
                .getResultList();
    }


    // =====================================================
    // TOTAL ÓRDENES ACTIVAS
    // =====================================================

    public Long ordenesActivas() {

        Object result = entityManager
                .createNativeQuery("""
                    SELECT COUNT(*)
                    FROM ordenes_taller
                    WHERE estado NOT IN (
                        'ENTREGADA',
                        'CANCELADA'
                    )
                """)
                .getSingleResult();

        return toLong(result);
    }


    public Long ordenesPendientes() {

        return contarOrdenesEstado("PENDIENTE");
    }


    public Long ordenesReparacion() {

        return contarOrdenesEstado("EN_REPARACION");
    }


    public Long ordenesTerminadas() {

        return contarOrdenesEstado("TERMINADA");
    }


    private Long contarOrdenesEstado(String estado) {

        Object result = entityManager
                .createNativeQuery("""
                    SELECT COUNT(*)
                    FROM ordenes_taller
                    WHERE estado = :estado
                """)
                .setParameter("estado", estado)
                .getSingleResult();

        return toLong(result);
    }


    // =====================================================
    // INGRESOS DEL TALLER
    // =====================================================

    public BigDecimal ingresosTaller() {

        Object result = entityManager
                .createNativeQuery("""
                    SELECT COALESCE(SUM(v.total), 0)
                    FROM ventas v
                    WHERE v.orden_taller_id IS NOT NULL
                      AND YEAR(v.fecha) = YEAR(CURDATE())
                      AND MONTH(v.fecha) = MONTH(CURDATE())
                """)
                .getSingleResult();

        return toBigDecimal(result);
    }


    // =====================================================
    // CONVERSORES
    // =====================================================

    private BigDecimal toBigDecimal(Object valor) {

        if (valor == null) {
            return BigDecimal.ZERO;
        }

        if (valor instanceof BigDecimal bigDecimal) {
            return bigDecimal;
        }

        return new BigDecimal(valor.toString());
    }


    private Long toLong(Object valor) {

        if (valor == null) {
            return 0L;
        }

        if (valor instanceof Number number) {
            return number.longValue();
        }

        return Long.parseLong(valor.toString());
    }
}
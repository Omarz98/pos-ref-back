package com.posref.pos.service.reportes;

import com.posref.pos.dto.reportes.ReporteVentaDetalleResponse;
import com.posref.pos.model.Ventas;
import com.posref.pos.model.EstadoVenta;
import com.posref.pos.repository.VentaRepository;
import com.posref.pos.repository.reportes.VentaReporteSpecifications;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ReporteVentasDetalleService {

    private final VentaRepository ventaRepository;

    @Transactional(readOnly = true)
    public Page<ReporteVentaDetalleResponse> obtenerDetalle(
            LocalDate desde,
            LocalDate hasta,
            EstadoVenta estado,
            Long ventaId,
            int page,
            int size
    ) {

        if (desde != null && hasta != null
                && desde.isAfter(hasta)) {
            throw new IllegalArgumentException(
                    "La fecha inicial no puede superar la final"
            );
        }

        if (page < 0) {
            throw new IllegalArgumentException(
                    "La página no puede ser negativa"
            );
        }

        if (size != 10 && size != 25 && size != 50) {
            throw new IllegalArgumentException(
                    "El tamaño debe ser 10, 25 o 50"
            );
        }

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(
                        Sort.Order.desc("fecha"),
                        Sort.Order.desc("id")
                )
        );

        return ventaRepository.findAll(
                VentaReporteSpecifications.filtrar(
                        desde,
                        hasta,
                        estado,
                        ventaId
                ),
                pageable
        ).map(this::convertirResponse);
    }

    private ReporteVentaDetalleResponse convertirResponse(
            Ventas venta
    ) {

        BigDecimal total = cero(venta.getTotal());

        BigDecimal anticipo =
                cero(venta.getAnticipoAplicado());

        BigDecimal pagosPos =
                cero(venta.getTotalPagado());

        BigDecimal saldo =
                cero(venta.getSaldoPendiente());

        BigDecimal totalCubierto =
                anticipo.add(pagosPos);

        String tipoVenta = venta.getOrdenTaller() != null
                ? "TALLER"
                : "MOSTRADOR";

        return new ReporteVentaDetalleResponse(
                venta.getId(),
                venta.getFecha(),
                venta.getCliente().getId(),
                venta.getCliente().getNombre(),
                tipoVenta,
                cero(venta.getSubtotal()),
                cero(venta.getIva()),
                total,
                anticipo,
                pagosPos,
                totalCubierto,
                saldo,
                venta.getEstado() != null
                        ? venta.getEstado().name()
                        : null
        );
    }

    private BigDecimal cero(BigDecimal valor) {
        return valor != null ? valor : BigDecimal.ZERO;
    }
}

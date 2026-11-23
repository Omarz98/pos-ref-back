package com.posref.pos.service.reportes;

import com.posref.pos.dto.reportes.ReporteVentaDiariaResponse;
import com.posref.pos.dto.reportes.ReporteVentasResumenResponse;
import com.posref.pos.repository.reportes.ReportesVentasRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportesVentasService {

    private final ReportesVentasRepository repository;

    @Transactional(readOnly = true)
    public ReporteVentasResumenResponse obtenerResumen(
            LocalDate desde,
            LocalDate hasta
    ) {

        if (desde == null || hasta == null) {
            throw new IllegalArgumentException(
                    "Debes indicar ambas fechas"
            );
        }

        if (desde.isAfter(hasta)) {
            throw new IllegalArgumentException(
                    "La fecha inicial no puede ser mayor que la final"
            );
        }

        return repository.obtenerResumen(desde, hasta);
    }

    @Transactional(readOnly = true)
    public List<ReporteMetodoPagoResponse> obtenerMetodosPago(
            LocalDate desde,
            LocalDate hasta
    ) {
        if (desde == null || hasta == null
                || desde.isAfter(hasta)) {
            throw new IllegalArgumentException(
                    "El rango de fechas no es válido"
            );
        }

        return repository.obtenerMetodosPago(desde, hasta);
    }

    @Transactional(readOnly = true)
    public List<ReporteVentaDiariaResponse> obtenerVentasDiarias(
            LocalDate desde,
            LocalDate hasta
    ) {
        if (desde == null || hasta == null
                || desde.isAfter(hasta)) {
            throw new IllegalArgumentException(
                    "El rango de fechas no es válido"
            );
        }

        if (java.time.temporal.ChronoUnit.DAYS.between(desde, hasta) > 366) {
            throw new IllegalArgumentException(
                    "El rango diario no puede superar 367 días"
            );
        }

        return repository.obtenerVentasDiarias(desde, hasta);
    }
}

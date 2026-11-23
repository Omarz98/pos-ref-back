package com.posref.pos.service.reportes;

import com.posref.pos.dto.reportes.ReporteIngresoCajaResponse;
import com.posref.pos.repository.reportes.ReportesCajaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportesCajaService {

    private final ReportesCajaRepository repository;

    @Transactional(readOnly = true)
    public List<ReporteIngresoCajaResponse> obtenerIngresos(
            LocalDate desde,
            LocalDate hasta
    ) {

        if (desde == null || hasta == null
                || desde.isAfter(hasta)) {
            throw new IllegalArgumentException(
                    "El rango de fechas no es válido"
            );
        }

        return repository.obtenerIngresos(desde, hasta);
    }
}

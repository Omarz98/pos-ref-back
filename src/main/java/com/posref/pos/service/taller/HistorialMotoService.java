package com.posref.pos.service.taller;

import com.posref.pos.dto.taller.HistorialMotoDTO;
import com.posref.pos.model.OrdenTaller;
import com.posref.pos.repository.IOrdenTallerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
public class HistorialMotoService {

    private final IOrdenTallerRepository ordenRepository;

    public HistorialMotoService(IOrdenTallerRepository ordenRepository) {
        this.ordenRepository = ordenRepository;
    }

    @Transactional(readOnly = true)
    public List<HistorialMotoDTO> obtenerHistorial(Long motoId) {

        List<OrdenTaller> ordenes = ordenRepository
                .findByMotocicleta_IdOrderByFechaRecepcionDesc(motoId);

        return ordenes.stream()
                .map(this::convertirDTO)
                .toList();
    }

    private HistorialMotoDTO convertirDTO(OrdenTaller orden) {

        return new HistorialMotoDTO(
                orden.getId(),
                orden.getFolio(),
                orden.getFechaRecepcion(),
                orden.getEstado().name(),
                orden.getKilometraje(),
                orden.getFallaReportada(),
                orden.getDiagnosticoInicial(),
                orden.getTotal(),
                List.of(),
                List.of()
        );
    }
}

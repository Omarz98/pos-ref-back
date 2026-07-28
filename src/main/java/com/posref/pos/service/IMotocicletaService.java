package com.posref.pos.service;

import com.posref.pos.dto.MarcasDTO;
import com.posref.pos.dto.MotocicletaRequest;
import com.posref.pos.dto.MotocicletaResponse;

import java.util.List;

public interface IMotocicletaService {

    MotocicletaResponse guardar(MotocicletaRequest request);

    MotocicletaResponse actualizar(
            Long id,
            MotocicletaRequest request
    );

    MotocicletaResponse obtenerPorId(Long id);

    List<MotocicletaResponse> obtenerPorCliente(Long clienteId);

    void desactivar(Long id);

    List<MotocicletaResponse> traerMotocicletas();

}

package com.posref.pos.service;

import com.posref.pos.dto.OrdenTallerRequest;
import com.posref.pos.dto.OrdenTallerResponse;
import com.posref.pos.model.EstadoOrdenTrabajo;
import com.posref.pos.repository.IOrdenTallerRepository;

import java.util.List;

public interface IOrdenTallerService {

    OrdenTallerResponse guardar(OrdenTallerRequest request);

    OrdenTallerResponse obtenerPorId(Long id);

    List<OrdenTallerResponse> obtenerTodas();

    List<OrdenTallerResponse> obtenerPorEstado(
            EstadoOrdenTrabajo estado
    );

    List<OrdenTallerResponse> obtenerOrdenesPendientesDePago();

}

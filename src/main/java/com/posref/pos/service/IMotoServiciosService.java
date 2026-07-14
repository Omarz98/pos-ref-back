package com.posref.pos.service;

import com.posref.pos.dto.MotoServiciosDTO;

import java.util.List;

public interface IMotoServiciosService {
    List<MotoServiciosDTO> traerServicios();
    MotoServiciosDTO crearServicio(MotoServiciosDTO servicioDto);
    MotoServiciosDTO actualizarServicio(Long id, MotoServiciosDTO servicioDto);
    void eliminarServicio(Long id);
}

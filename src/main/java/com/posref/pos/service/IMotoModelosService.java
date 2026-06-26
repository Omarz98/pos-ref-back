package com.posref.pos.service;

import com.posref.pos.dto.MotoModelosDTO;
import com.posref.pos.dto.ProductosDTO;

import java.util.List;

public interface IMotoModelosService {
    List<MotoModelosDTO> traerMotoModelos();
    MotoModelosDTO crearMotoModelo(MotoModelosDTO motoModeloDto);
    MotoModelosDTO actualizarMotoModelo(Long id, MotoModelosDTO motoModeloDto);
    void eliminarMotoModelo(Long id);
}

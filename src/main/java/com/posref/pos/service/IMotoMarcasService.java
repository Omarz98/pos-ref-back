package com.posref.pos.service;

import com.posref.pos.dto.MotoMarcasDTO;
import com.posref.pos.dto.MotoModelosDTO;

import java.util.List;

public interface IMotoMarcasService{
    List<MotoMarcasDTO> traerMotoMarcas();
    MotoMarcasDTO crearMotoMarca(MotoMarcasDTO motoMarcaDto);
    MotoMarcasDTO actualizarMotoMarca(Long id, MotoMarcasDTO motoMarcaDto);
    void eliminarMotoMarca(Long id);
}

package com.posref.pos.service;

import com.posref.pos.dto.MarcasDTO;
import com.posref.pos.dto.MotoVersionesDTO;

import java.util.List;

public interface IMotoVersionesService {
    List<MotoVersionesDTO> traerMotoVersiones();
    MotoVersionesDTO crearMotoVersion(MotoVersionesDTO motoVersionDto);
    MotoVersionesDTO actualizarMotoVersion(Long id, MotoVersionesDTO motoVersionDto);
    void eliminarMotoVersion(Long id);
}

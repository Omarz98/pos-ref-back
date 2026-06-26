package com.posref.pos.service;

import com.posref.pos.dto.CategoriasDTO;
import com.posref.pos.dto.MarcasDTO;

import java.util.List;

public interface IMarcasService{
    List<MarcasDTO> traerMarcas();
    MarcasDTO crearMarca(MarcasDTO MarcaDto);
    MarcasDTO actualizarMarca(Long id,MarcasDTO MarcaDto);
    void eliminarMarca(Long id);
}

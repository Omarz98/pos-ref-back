package com.posref.pos.service;

import com.posref.pos.dto.CategoriasDTO;

import java.util.List;

public interface ICategoriasService {
    List<CategoriasDTO> traerCategorias();
    CategoriasDTO crearCategoria(CategoriasDTO categoriaDto);
    CategoriasDTO actualizarCategoria(Long id, CategoriasDTO categoriaDto);
    void eliminarCategoria(Long id);
}

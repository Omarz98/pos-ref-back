package com.posref.pos.service;

import com.posref.pos.dto.CategoriasDTO;
import com.posref.pos.exception.NotFoundException;
import com.posref.pos.mapper.Mapper;
import com.posref.pos.model.Categorias;
import com.posref.pos.repository.CategoriasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriasService implements ICategoriasService{

    @Autowired
    private CategoriasRepository repo;

    @Override
    public List<CategoriasDTO> traerCategorias(){
        return repo.findAll().stream().map(Mapper::toDTO).toList();
    }

    @Override
    public CategoriasDTO crearCategoria(CategoriasDTO categoriaDto){
        Categorias categoria = Categorias.builder()
                .nombre(categoriaDto.getNombre())
                .descripcion(categoriaDto.getDescripcion())
                .activo(categoriaDto.isActivo())
                .build();
        return Mapper.toDTO(repo.save(categoria));
    }

    @Override
    public CategoriasDTO actualizarCategoria(Long id, CategoriasDTO categoriaDto){
        Categorias categorias = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Categoria no encontrado"));

        categorias.setNombre(categoriaDto.getNombre());
        categorias.setDescripcion(categoriaDto.getDescripcion());
        categorias.setActivo(categoriaDto.isActivo());

        return Mapper.toDTO(repo.save(categorias));
    }

    @Override
    public void eliminarCategoria(Long id) {
        if (!repo.existsById(id)) {
            throw new NotFoundException("Categoria no encontrado para eliminar");
        }

        repo.deleteById(id);
    }
}

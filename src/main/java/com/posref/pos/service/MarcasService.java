package com.posref.pos.service;

import com.posref.pos.dto.MarcasDTO;
import com.posref.pos.dto.ProveedoresDTO;
import com.posref.pos.exception.NotFoundException;
import com.posref.pos.mapper.Mapper;
import com.posref.pos.model.Marcas;
import com.posref.pos.model.Proveedores;
import com.posref.pos.repository.MarcasRepository;
import com.posref.pos.repository.ProveedoresRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MarcasService implements IMarcasService{

    @Autowired
    private MarcasRepository repo;

    @Override
    public List<MarcasDTO> traerMarcas(){
        return repo.findAll().stream().map(Mapper::toDTO).toList();
    }

    @Override
    public MarcasDTO crearMarca(MarcasDTO marcaDto){
        Marcas marca = Marcas.builder()
                .nombre(marcaDto.getNombre())
                .activo(marcaDto.isActivo())
                .build();
        return Mapper.toDTO(repo.save(marca));
    }

    @Override
    public MarcasDTO actualizarMarca(Long id, MarcasDTO marcaDto){
        Marcas marca = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Marca no encontrada"));

        marca.setNombre(marcaDto.getNombre());
        marca.setActivo(marcaDto.isActivo());

        return Mapper.toDTO(repo.save(marca));
    }

    @Override
    public void eliminarMarca(Long id) {
        if (!repo.existsById(id)) {
            throw new NotFoundException("MArca no encontrado para eliminar");
        }

        repo.deleteById(id);
    }

}

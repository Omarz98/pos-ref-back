package com.posref.pos.service;

import com.posref.pos.dto.MarcasDTO;
import com.posref.pos.dto.MotoMarcasDTO;
import com.posref.pos.exception.NotFoundException;
import com.posref.pos.mapper.Mapper;
import com.posref.pos.model.Marcas;
import com.posref.pos.model.MotoMarcas;
import com.posref.pos.repository.MarcasRepository;
import com.posref.pos.repository.MotoMarcasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MotoMarcasService implements IMotoMarcasService{

    @Autowired
    private MotoMarcasRepository repo;

    @Override
    public List<MotoMarcasDTO> traerMotoMarcas(){
        return repo.findAll().stream().map(Mapper::toDTO).toList();
    }

    @Override
    public MotoMarcasDTO crearMotoMarca(MotoMarcasDTO marcaDto){
        MotoMarcas motomarca = MotoMarcas.builder()
                .nombre(marcaDto.getNombre())
                .activo(marcaDto.isActivo())
                .build();
        return Mapper.toDTO(repo.save(motomarca));
    }

    @Override
    public MotoMarcasDTO actualizarMotoMarca(Long id, MotoMarcasDTO motoMarcaDto){
        MotoMarcas motomarca = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Marca de moto no encontrada"));

        motomarca.setNombre(motoMarcaDto.getNombre());
        motomarca.setActivo(motoMarcaDto.isActivo());

        return Mapper.toDTO(repo.save(motomarca));
    }

    @Override
    public void eliminarMotoMarca(Long id) {
        if (!repo.existsById(id)) {
            throw new NotFoundException("MArca no encontrado para eliminar");
        }

        repo.deleteById(id);
    }


}

package com.posref.pos.service;

import com.posref.pos.dto.MotoModelosDTO;
import com.posref.pos.exception.NotFoundException;
import com.posref.pos.mapper.Mapper;
import com.posref.pos.model.MotoMarcas;
import com.posref.pos.model.MotoModelos;
import com.posref.pos.repository.MotoMarcasRepository;
import com.posref.pos.repository.MotoModelosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MotoModelosService implements IMotoModelosService{
    @Autowired
    private MotoModelosRepository repo;
    @Autowired
    private MotoMarcasRepository motoMarcaRepo;

    @Override
    public List<MotoModelosDTO> traerMotoModelos(){
        return repo.findAll().stream().map(Mapper::toDTO).toList();
    }

    @Override
    public MotoModelosDTO crearMotoModelo(MotoModelosDTO motoModelosDto){

        if (motoModelosDto.getMotoMarcaId() == null) throw new RuntimeException("Debe indicar la marca");

        //Buscar la marca
        MotoMarcas marca = motoMarcaRepo.findById(motoModelosDto.getMotoMarcaId()).orElseThrow(() -> new NotFoundException("MotoModelo no encontrada"));
        if (marca == null) {
            throw new NotFoundException("MotoMarca no encontrada");
        }



        MotoModelos motoModelos = MotoModelos.builder()
                .id(motoModelosDto.getId())
                .motoMarca(marca)
                .nombre(motoModelosDto.getNombre())
                .activo(true)
                .build();


        return Mapper.toDTO(repo.save(motoModelos));
    }

    @Override
    public MotoModelosDTO actualizarMotoModelo(Long id, MotoModelosDTO motoModeloDto){

        //buscar si MotoModelosDTO existe para actualizarla
        MotoModelos m = repo.findById(id).orElseThrow(() -> new NotFoundException("MotoModelo no encontrada"));
        if (m == null) throw new RuntimeException("MotoModelo no encontrada");

        m.setId(motoModeloDto.getId());

        if (motoModeloDto.getMotoMarcaId()!=null) {
            MotoMarcas motomarca = motoMarcaRepo.findById(motoModeloDto.getMotoMarcaId()).orElseThrow(() -> new NotFoundException("MotoModelo no encontrada"));
            if (motomarca == null) throw new NotFoundException("MotoMarca  no encontrada");
            m.setMotoMarca(motomarca);
        }

        m.setNombre(motoModeloDto.getNombre());
        m.setActivo(true);

        repo.save(m);

        MotoModelosDTO motoModelo = Mapper.toDTO(m);

        return motoModelo;

        //return Mapper.toDTO(repo.save(proveedor));
    }

    @Override
    public void eliminarMotoModelo(Long id) {
        //if (!repo.existsById(id)) {
          //  throw new NotFoundException("Categoria no encontrado para eliminar");
        //}

        //repo.deleteById(id);
        MotoModelos m = repo.findById(id).orElseThrow(() -> new NotFoundException("MotoModelo no encontrada"));
        if (m == null) throw new RuntimeException("MotoModelo no encontrada");
        repo.delete(m);
    }
}

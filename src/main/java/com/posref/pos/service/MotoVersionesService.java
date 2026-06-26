package com.posref.pos.service;

import com.posref.pos.dto.MotoModelosDTO;
import com.posref.pos.dto.MotoVersionesDTO;
import com.posref.pos.exception.NotFoundException;
import com.posref.pos.mapper.Mapper;
import com.posref.pos.model.MotoMarcas;
import com.posref.pos.model.MotoModelos;
import com.posref.pos.model.MotoVersiones;
import com.posref.pos.repository.MotoModelosRepository;
import com.posref.pos.repository.MotoVersionesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MotoVersionesService implements IMotoVersionesService{

    @Autowired
    private MotoVersionesRepository motoverRepo;

    @Autowired
    private MotoModelosRepository motoModeloRepo;

    @Override
    public List<MotoVersionesDTO> traerMotoVersiones(){
        return motoverRepo.findAll().stream().map(Mapper::toDTO).toList();
    }

    @Override
    public MotoVersionesDTO crearMotoVersion(MotoVersionesDTO motoVersionesDto){

        if (motoVersionesDto.getMotoModeloId() == null) throw new RuntimeException("Debe indicar el modelo");

        //Buscar el modelo
        MotoModelos modelo = motoModeloRepo.findById(motoVersionesDto.getMotoModeloId()).orElseThrow(() -> new NotFoundException("MotoVersion no encontrada"));
        if (modelo == null) {
            throw new NotFoundException("MotoModelo no encontrada");
        }

        MotoVersiones motoVersiones = MotoVersiones.builder()
                .id(motoVersionesDto.getId())
                .motoModelo(modelo)
                .anio(motoVersionesDto.getAnio())
                .cilindraje(motoVersionesDto.getCilindraje())
                .version(motoVersionesDto.getVersion())
                .activo(true)
                .build();


        return Mapper.toDTO(motoverRepo.save(motoVersiones));
    }

    @Override
    public MotoVersionesDTO actualizarMotoVersion(Long id, MotoVersionesDTO motoVersionDto){

        //buscar si MotoVersionesDTO existe para actualizarla
        MotoVersiones m = motoverRepo.findById(id).orElseThrow(() -> new NotFoundException("MotoVersion no encontrada"));
        if (m == null) throw new RuntimeException("MotoVersion no encontrada");

        m.setId(motoVersionDto.getId());

        if (motoVersionDto.getMotoModeloId()!=null) {
            MotoModelos motomodelo = motoModeloRepo.findById(motoVersionDto.getMotoModeloId()).orElseThrow(() -> new NotFoundException("MotoVersion no encontrada"));
            if (motomodelo == null) throw new NotFoundException("MotoMarca  no encontrada");
            m.setMotoModelo(motomodelo);
        }

        m.setVersion(motoVersionDto.getVersion());
        m.setAnio(motoVersionDto.getAnio());
        m.setCilindraje(motoVersionDto.getCilindraje());


        m.setActivo(true);

        motoverRepo.save(m);

        MotoVersionesDTO motoVersion = Mapper.toDTO(m);

        return motoVersion;

        //return Mapper.toDTO(repo.save(proveedor));
    }

    @Override
    public void eliminarMotoVersion(Long id) {
        //if (!repo.existsById(id)) {
        //  throw new NotFoundException("Categoria no encontrado para eliminar");
        //}

        //repo.deleteById(id);
        MotoVersiones m = motoverRepo.findById(id).orElseThrow(() -> new NotFoundException("MotoVersion no encontrada"));
        if (m == null) throw new RuntimeException("MotoModelo no encontrada");
        motoverRepo.delete(m);
    }

}

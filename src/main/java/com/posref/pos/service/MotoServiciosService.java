package com.posref.pos.service;

import com.posref.pos.dto.MotoServiciosDTO;
import com.posref.pos.exception.NotFoundException;
import com.posref.pos.mapper.Mapper;
import com.posref.pos.model.MotoServicios;
import com.posref.pos.repository.MotoServiciosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MotoServiciosService implements IMotoServiciosService {
    @Autowired
    private MotoServiciosRepository repo;

    @Override
    public List<MotoServiciosDTO> traerServicios() {
        return repo.findAll().stream().map(Mapper::toDTO).toList();
    }

    @Override
    public MotoServiciosDTO crearServicio(MotoServiciosDTO servicioDto) {
        MotoServicios servicio = MotoServicios.builder()
                .nombre(servicioDto.getNombre())
                .activo(servicioDto.isActivo())
                .codigo(servicioDto.getCodigo())
                .precioVenta(servicioDto.getPrecioVenta())
                .build();
        return Mapper.toDTO(repo.save(servicio));
    }

    @Override
    public MotoServiciosDTO actualizarServicio(Long id, MotoServiciosDTO servicioDto) {
        MotoServicios servicio = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Servicio no encontrado"));

        servicio.setNombre(servicioDto.getNombre());
        servicio.setCodigo(servicioDto.getCodigo());
        servicio.setPrecioVenta(servicioDto.getPrecioVenta());
        servicio.setActivo(servicioDto.isActivo());

        return Mapper.toDTO(repo.save(servicio));
    }

    @Override
    public void eliminarServicio(Long id) {
        if (!repo.existsById(id)) {
            throw new NotFoundException("Servicio no encontrado para eliminar");
        }

        repo.deleteById(id);
    }
}

package com.posref.pos.service;

import com.posref.pos.dto.ProveedoresDTO;
import com.posref.pos.exception.NotFoundException;
import com.posref.pos.mapper.Mapper;
import com.posref.pos.model.Proveedores;
import com.posref.pos.repository.ProveedoresRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProveedoresService implements IProveedoresService{
    @Autowired
    private ProveedoresRepository repo;

    @Override
    public List<ProveedoresDTO> traerProveedores(){
        return repo.findAll().stream().map(Mapper::toDTO).toList();
    }

    @Override
    public ProveedoresDTO crearProveedor(ProveedoresDTO proveedoresDto){
        Proveedores proveedor = Proveedores.builder()
                .nombre(proveedoresDto.getNombre())
                .telefono(proveedoresDto.getTelefono())
                .email(proveedoresDto.getEmail())
                .direccion(proveedoresDto.getDireccion())
                .contacto(proveedoresDto.getContacto())
                .activo(proveedoresDto.isActivo())
                .build();
        return Mapper.toDTO(repo.save(proveedor));
    }

    @Override
    public ProveedoresDTO actualizarProveedor(Long id, ProveedoresDTO proveedorDto){
        Proveedores proveedor = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Proveedor no encontrado"));

        proveedor.setNombre(proveedorDto.getNombre());
        proveedor.setTelefono(proveedorDto.getTelefono());
        proveedor.setEmail(proveedorDto.getEmail());
        proveedor.setDireccion(proveedorDto.getDireccion());
        proveedor.setContacto(proveedorDto.getContacto());
        proveedor.setActivo(proveedorDto.isActivo());

        return Mapper.toDTO(repo.save(proveedor));
    }

    @Override
    public void eliminarProveedor(Long id) {
        if (!repo.existsById(id)) {
            throw new NotFoundException("Categoria no encontrado para eliminar");
        }

        repo.deleteById(id);
    }
}

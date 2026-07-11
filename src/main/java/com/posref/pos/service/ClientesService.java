package com.posref.pos.service;

import com.posref.pos.dto.ClientesDTO;
import com.posref.pos.exception.NotFoundException;
import com.posref.pos.mapper.Mapper;
import com.posref.pos.model.Categorias;
import com.posref.pos.model.Clientes;
import com.posref.pos.model.Productos;
import com.posref.pos.repository.ClientesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClientesService implements IClientesService {

    @Autowired
    ClientesRepository clienteRepo;

    @Override
    public List<ClientesDTO> traerClientes() {
        return clienteRepo.findAll().stream().map(Mapper::toDTO).toList();
    }

    @Override
    public ClientesDTO crearCliente(ClientesDTO clienteDto) {
        Clientes cliente = Clientes.builder()
                .nombre(clienteDto.getNombre())
                .telefono(clienteDto.getTelefono())
                .email(clienteDto.getEmail())
                .direccion(clienteDto.getDireccion())
                .rfc(clienteDto.getRfc())
                .activo(true)
                .fechaCreacion(LocalDateTime.now())
                .build();
        return Mapper.toDTO(clienteRepo.save(cliente));
    }

    @Override
    public ClientesDTO actualizarCliente(Long id, ClientesDTO clienteDto) {
        Clientes cliente = clienteRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado"));
        cliente.setNombre(clienteDto.getNombre());
        cliente.setDireccion(clienteDto.getDireccion());
        cliente.setEmail(clienteDto.getEmail());
        cliente.setRfc(clienteDto.getRfc());
        cliente.setActivo(clienteDto.isActivo());
        cliente.setFechaCreacion(LocalDateTime.now());
        cliente.setTelefono(clienteDto.getTelefono());

        return Mapper.toDTO(clienteRepo.save(cliente));
    }

    @Override
    public void eliminarCliente(Long id) {
        if (!clienteRepo.existsById(id)) {
            throw new NotFoundException("Cliente no encontrado para eliminar");
        }

        clienteRepo.deleteById(id);
    }
}

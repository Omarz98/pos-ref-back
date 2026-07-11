package com.posref.pos.service;

import com.posref.pos.dto.ClientesDTO;

import java.util.List;

public interface IClientesService {
    List<ClientesDTO> traerClientes();
    ClientesDTO crearCliente(ClientesDTO clienteDto);
    ClientesDTO actualizarCliente(Long id, ClientesDTO clienteDto);
    void eliminarCliente(Long id);
}

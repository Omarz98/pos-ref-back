package com.posref.pos.service;

import com.posref.pos.dto.ClienteMotoRequest;
import com.posref.pos.dto.ClienteMotoResponse;
import com.posref.pos.dto.ClientesDTO;
import com.posref.pos.model.Motocicleta;

import java.util.List;

public interface IClientesService {
    List<ClientesDTO> traerClientes();
    ClientesDTO crearCliente(ClientesDTO clienteDto);
    ClientesDTO actualizarCliente(Long id, ClientesDTO clienteDto);
    void eliminarCliente(Long id);
    ClienteMotoResponse  agregarMoto( Long clienteId, ClienteMotoRequest request);

    List<ClienteMotoResponse> obtenerMotos(Long clienteId);
}

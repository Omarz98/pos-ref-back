package com.posref.pos.service;

import com.posref.pos.dto.ClienteMotoRequest;
import com.posref.pos.dto.ClienteMotoResponse;
import com.posref.pos.dto.ClientesDTO;
import com.posref.pos.exception.NotFoundException;
import com.posref.pos.mapper.Mapper;
import com.posref.pos.model.*;


import com.posref.pos.repository.ClientesRepository;
import com.posref.pos.repository.MotoModelosRepository;
import com.posref.pos.repository.MotoVersionesRepository;
import com.posref.pos.repository.MotocicletaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientesService implements IClientesService {

    @Autowired
    ClientesRepository clienteRepo;

    @Autowired
    private MotoModelosRepository modeloRepository;
    @Autowired
    private MotoVersionesRepository versionRepository;
    @Autowired
    private MotocicletaRepository clienteMotoRepository;

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

    @Override
    @Transactional
    public ClienteMotoResponse agregarMoto(
            Long clienteId,
            ClienteMotoRequest request
    ) {

        Clientes cliente = clienteRepo
                .findById(clienteId)
                .orElseThrow(() ->
                        new RuntimeException("Cliente no encontrado")
                );

        if (request.getMotoVersionId() == null) {
            throw new IllegalArgumentException(
                    "El campo motoVersionId es obligatorio"
            );
        }

        MotoVersiones version = versionRepository
                .findById(request.getMotoVersionId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Versión no encontrada con id: "
                                        + request.getMotoVersionId()
                        )
                );

        Motocicleta moto = Motocicleta.builder()
                .cliente(cliente)
                .motoVersion(version)
                .placas(request.getPlacas())
                .color(request.getColor())
                .numeroSerie(request.getNumeroSerie())
                .kilometrajeActual(
                        request.getKilometrajeActual() != null
                                ? request.getKilometrajeActual()
                                : 0
                )
                .activo(
                        request.getActivo() != null
                                ? request.getActivo()
                                : true
                )
                .build();

        Motocicleta guardada =
                clienteMotoRepository.saveAndFlush(moto);

        return convertirResponse(guardada);
    }

    @Override
    @Transactional
    public List<ClienteMotoResponse> obtenerMotos(
            Long clienteId
    ) {

        return clienteMotoRepository
                .findByClienteIdAndActivoTrue(clienteId)
                .stream()
                .map(this::convertirResponse)
                .collect(Collectors.toList());
    }

    private ClienteMotoResponse convertirResponse(
            Motocicleta moto
    ) {

        return ClienteMotoResponse.builder()
                .id(moto.getId())

                .clienteId(
                        moto.getCliente() != null
                                ? moto.getCliente().getId()
                                : null
                )
                .clienteNombre(
                        moto.getCliente() != null
                                ? moto.getCliente().getNombre()
                                : null
                )



                .versionId(
                        moto.getMotoVersion() != null
                                ? moto.getMotoVersion().getId()
                                : null
                )



                .placa(moto.getPlacas())
                .color(moto.getColor())
                .numeroSerie(moto.getNumeroSerie())
                .kilometraje(moto.getKilometrajeActual())
                .activo(moto.getActivo())
                .build();
    }
}

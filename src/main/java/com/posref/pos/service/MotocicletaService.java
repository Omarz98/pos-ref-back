package com.posref.pos.service;

import com.posref.pos.dto.MotocicletaRequest;
import com.posref.pos.dto.MotocicletaResponse;
import com.posref.pos.exception.RecursoNoEncontradoException;
import com.posref.pos.exception.ReglaNegocioException;
import com.posref.pos.mapper.Mapper;
import com.posref.pos.mapper.MotocicletaMapper;
import com.posref.pos.model.Clientes;
import com.posref.pos.model.MotoVersiones;
import com.posref.pos.model.Motocicleta;
import com.posref.pos.repository.ClientesRepository;
import com.posref.pos.repository.MotoVersionesRepository;
import com.posref.pos.repository.MotocicletaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@RequiredArgsConstructor
public class MotocicletaService implements IMotocicletaService{

    private final MotocicletaRepository motocicletaRepository;
    private final ClientesRepository clientesRepository;
    private final MotoVersionesRepository motoVersionesRepository;
    private final MotocicletaMapper motocicletaMapper;

    @Override
    @Transactional
    public MotocicletaResponse guardar(MotocicletaRequest request) {

        if (request.getClienteId() == null) {
            throw new IllegalArgumentException(
                    "El clienteId es obligatorio"
            );
        }

        if (request.getMotoVersionId() == null) {
            throw new IllegalArgumentException(
                    "El motoVersionId es obligatorio"
            );
        }

        Clientes cliente = clientesRepository
                .findById(request.getClienteId())
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "No existe el cliente con id: "
                                        + request.getClienteId()
                        )
                );

        MotoVersiones motoVersion = motoVersionesRepository
                .findById(request.getMotoVersionId())
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "No existe la versión de motocicleta con id: "
                                        + request.getMotoVersionId()
                        )
                );

        validarDatosUnicos(
                request.getNumeroSerie(),
                request.getPlacas(),
                null
        );

        Motocicleta motocicleta = Motocicleta.builder()
                .cliente(cliente)
                .motoVersion(motoVersion)
                .numeroSerie(normalizar(request.getNumeroSerie()))
                .numeroMotor(normalizar(request.getNumeroMotor()))
                .placas(normalizar(request.getPlacas()))
                .color(normalizar(request.getColor()))
                .kilometrajeActual(
                        request.getKilometrajeActual() != null
                                ? request.getKilometrajeActual()
                                : 0
                )
                .observaciones(request.getObservaciones())
                .activo(true)
                .build();

        System.out.println(
                "request.motoVersionId = "
                        + request.getMotoVersionId()
        );

        System.out.println(
                "motoVersion obtenida = "
                        + motoVersion.getId()
        );

        System.out.println(
                "motoVersion asignada = "
                        + (
                        motocicleta.getMotoVersion() != null
                                ? motocicleta.getMotoVersion().getId()
                                : null
                )
        );

        if (motocicleta.getMotoVersion() == null) {
            throw new IllegalStateException(
                    "La motocicleta se construyó sin motoVersion"
            );
        }

        Motocicleta guardada =
                motocicletaRepository.saveAndFlush(motocicleta);

        return motocicletaMapper.toResponse(guardada);
    }

    @Override
    @Transactional
    public MotocicletaResponse actualizar(
            Long id,
            MotocicletaRequest request
    ) {

        Motocicleta motocicleta = motocicletaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe la motocicleta con id: " + id
                ));

        Clientes cliente = clientesRepository.findById(request.getClienteId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe el cliente con id: "
                                + request.getClienteId()
                ));

        MotoVersiones motoVersion =
                motoVersionesRepository.findById(request.getMotoVersionId())
                        .orElseThrow(() ->
                                new RecursoNoEncontradoException(
                                        "No existe la versión de motocicleta con id: "
                                                + request.getMotoVersionId()
                                )
                        );

        validarDatosUnicos(
                request.getNumeroSerie(),
                request.getPlacas(),
                motocicleta.getId()
        );

        motocicleta.setCliente(cliente);
        motocicleta.setMotoVersion(motoVersion);
        motocicleta.setNumeroSerie(
                normalizar(request.getNumeroSerie())
        );
        motocicleta.setNumeroMotor(
                normalizar(request.getNumeroMotor())
        );
        motocicleta.setPlacas(
                normalizar(request.getPlacas())
        );
        motocicleta.setColor(
                normalizar(request.getColor())
        );
        motocicleta.setKilometrajeActual(
                request.getKilometrajeActual() != null
                        ? request.getKilometrajeActual()
                        : 0
        );
        motocicleta.setObservaciones(request.getObservaciones());

        return motocicletaMapper.toResponse(
                motocicletaRepository.save(motocicleta)
        );
    }

    @Override
    @Transactional
    public MotocicletaResponse obtenerPorId(Long id) {

        Motocicleta motocicleta =
                motocicletaRepository.findByIdAndActivoTrue(id)
                        .orElseThrow(() ->
                                new RecursoNoEncontradoException(
                                        "No existe una motocicleta activa con id: "
                                                + id
                                )
                        );

        return motocicletaMapper.toResponse(motocicleta);
    }

    @Override
    @Transactional
    public List<MotocicletaResponse> obtenerPorCliente(Long clienteId) {

        return motocicletaRepository
                .findByClienteIdAndActivoTrueOrderByIdDesc(clienteId)
                .stream()
                .map(motocicletaMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public void desactivar(Long id) {

        Motocicleta motocicleta = motocicletaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe la motocicleta con id: " + id
                ));

        motocicleta.setActivo(false);
        motocicletaRepository.save(motocicleta);
    }

    @Override
    public List<MotocicletaResponse> traerMotocicletas() {
        return motocicletaRepository.findAll().stream().map(motocicletaMapper::toResponse).toList();


    }

    private void validarDatosUnicos(
            String numeroSerie,
            String placas,
            Long motocicletaActualId
    ) {

        String serieNormalizada = normalizar(numeroSerie);
        String placasNormalizadas = normalizar(placas);

        if (serieNormalizada != null) {
            motocicletaRepository
                    .findByNumeroSerieIgnoreCase(serieNormalizada)
                    .filter(m -> !m.getId().equals(motocicletaActualId))
                    .ifPresent(m -> {
                        throw new ReglaNegocioException(
                                "Ya existe una motocicleta con el número de serie: "
                                        + serieNormalizada
                        );
                    });
        }

        if (placasNormalizadas != null) {
            motocicletaRepository
                    .findByPlacasIgnoreCase(placasNormalizadas)
                    .filter(m -> !m.getId().equals(motocicletaActualId))
                    .ifPresent(m -> {
                        throw new ReglaNegocioException(
                                "Ya existe una motocicleta con las placas: "
                                        + placasNormalizadas
                        );
                    });
        }
    }

    private String normalizar(String valor) {

        if (valor == null || valor.isBlank()) {
            return null;
        }

        return valor.trim().toUpperCase();
    }
}

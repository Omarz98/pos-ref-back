package com.posref.pos.service;

import com.posref.pos.dto.*;
import com.posref.pos.dto.taller.ActualizarOrdenTallerRequest;
import com.posref.pos.dto.taller.AsignarTecnicoOrdenRequest;
import com.posref.pos.dto.taller.CambiarEstadoOrdenRequest;
import com.posref.pos.model.EstadoOrdenTrabajo;

import java.util.List;

public interface IOrdenTallerService {

    OrdenTallerResponse guardar(OrdenTallerRequest request);

    OrdenTallerResponse obtenerPorId(Long id);

    List<OrdenTallerResponse> obtenerTodas();

    List<OrdenTallerResponse> obtenerPorEstado(
            EstadoOrdenTrabajo estado
    );

    List<OrdenTallerResponse> buscar(
            EstadoOrdenTrabajo estado,
            String buscar
    );

    List<OrdenTallerResponse> obtenerOrdenesPendientesDePago();

    List<OrdenTallerResponse> historialMotocicleta(Long motoId);

    OrdenTallerResponse actualizar(
            Long id,
            ActualizarOrdenTallerRequest request
    );

    OrdenTallerResponse cambiarEstado(
            Long id,
            CambiarEstadoOrdenRequest request
    );

    OrdenTallerResponse asignarTecnico(
            Long id,
            AsignarTecnicoOrdenRequest request
    );

    OrdenTallerResponse agregarServicio(
            Long id,
            OrdenServicioRequest request,
            Long usuarioId
    );

    OrdenTallerResponse eliminarServicio(
            Long id,
            Long detalleId,
            Long usuarioId
    );

    OrdenTallerResponse agregarProducto(
            Long id,
            OrdenProductoRequest request,
            Long usuarioId
    );

    OrdenTallerResponse eliminarProducto(
            Long id,
            Long detalleId,
            Long usuarioId
    );
}

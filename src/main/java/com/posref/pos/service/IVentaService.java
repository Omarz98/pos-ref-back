package com.posref.pos.service;

import com.posref.pos.dto.CobroVentaRequest;
import com.posref.pos.dto.VentaResponse;
import com.posref.pos.model.Ventas;
import com.posref.pos.dto.VentaRequest;

import java.util.List;

public interface IVentaService {
    VentaResponse guardarVenta(VentaRequest request);

    //List<Ventas> obtenerVentas();

    List<VentaResponse> obtenerVentas();

    Ventas obtenerPorId(Long id);

    VentaResponse cobrarVenta(Long ventaId, CobroVentaRequest request);
}

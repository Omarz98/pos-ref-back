package com.posref.pos.service;

import com.posref.pos.model.Ventas;
import com.posref.pos.dto.VentaRequest;

import java.util.List;

public interface IVentaService {
    Ventas guardarVenta(VentaRequest request);

    List<Ventas> obtenerVentas();

    Ventas obtenerPorId(Long id);
}

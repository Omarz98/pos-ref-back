package com.posref.pos.service.caja;

import com.posref.pos.dto.caja.*;
import com.posref.pos.model.VentaPago;
import com.posref.pos.model.Ventas;

import java.math.BigDecimal;
import java.util.List;

public interface ICajaService {

    CajaResponse abrirCaja(
            Long usuarioId,
            AperturaCajaRequest request
    );

    CajaResponse obtenerCajaAbierta(Long usuarioId);

    CajaResponse cerrarCaja(
            Long usuarioId,
            CierreCajaRequest request
    );

    CajaResponse registrarMovimiento(
            Long usuarioId,
            MovimientoCajaRequest request
    );

    List<CajaResponse> obtenerHistorial();

    void registrarVenta(
            Long usuarioId,
            Ventas venta,
            List<VentaPago> pagos,
            BigDecimal cambio
    );
}
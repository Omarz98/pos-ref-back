package com.posref.pos.dto.caja;

import com.posref.pos.model.caja.EstadoCaja;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CajaResponse(
        Long id,
        String nombreCaja,
        Long usuarioId,
        String usuario,
        LocalDateTime fechaApertura,
        LocalDateTime fechaCierre,
        BigDecimal montoInicial,
        BigDecimal ventasEfectivo,
        BigDecimal ventasTarjeta,
        BigDecimal ventasTransferencia,
        BigDecimal entradas,
        BigDecimal retiros,
        BigDecimal devoluciones,
        BigDecimal efectivoEsperado,
        BigDecimal efectivoContado,
        BigDecimal diferencia,
        EstadoCaja estado,
        String observacionesApertura,
        String observacionesCierre
) {
}

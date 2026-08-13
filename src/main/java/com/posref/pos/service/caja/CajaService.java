package com.posref.pos.service.caja;

import com.posref.pos.dto.VentaRequest;
import com.posref.pos.dto.VentaResponse;
import com.posref.pos.dto.caja.*;
import com.posref.pos.model.MetodoPago;
import com.posref.pos.model.VentaPago;
import com.posref.pos.model.Ventas;
import com.posref.pos.model.caja.*;
import com.posref.pos.model.seguridad.Usuario;
import com.posref.pos.repository.caja.CajaSesionRepository;
import com.posref.pos.repository.caja.MovimientoCajaRepository;
import com.posref.pos.repository.seguridad.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CajaService implements ICajaService {

    private final CajaSesionRepository cajaSesionRepository;
    private final MovimientoCajaRepository movimientoCajaRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public CajaResponse abrirCaja(
            Long usuarioId,
            AperturaCajaRequest request
    ) {
        boolean yaTieneCajaAbierta =
                cajaSesionRepository.existsByUsuarioIdAndEstado(
                        usuarioId,
                        EstadoCaja.ABIERTA
                );

        if (yaTieneCajaAbierta) {
            throw new RuntimeException(
                    "El usuario ya tiene una caja abierta"
            );
        }

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() ->
                        new RuntimeException("Usuario no encontrado")
                );

        CajaSesion sesion = CajaSesion.builder()
                .usuario(usuario)
                .nombreCaja(request.nombreCaja())
                .fechaApertura(LocalDateTime.now())
                .montoInicial(request.montoInicial())
                .efectivoEsperado(request.montoInicial())
                .estado(EstadoCaja.ABIERTA)
                .observacionesApertura(request.observaciones())
                .build();

        CajaSesion guardada = cajaSesionRepository.save(sesion);

        MovimientoCaja apertura = MovimientoCaja.builder()
                .cajaSesion(guardada)
                .tipo(TipoMovimientoCaja.APERTURA)
                .metodo(MetodoMovimientoCaja.EFECTIVO)
                .monto(request.montoInicial())
                .concepto("Fondo inicial de caja")
                .fecha(LocalDateTime.now())
                .build();

        movimientoCajaRepository.save(apertura);

        return convertirResponse(guardada);
    }

    @Override
    @Transactional(readOnly = true)
    public CajaResponse obtenerCajaAbierta(Long usuarioId) {
        CajaSesion sesion = buscarCajaAbierta(usuarioId);
        return convertirResponse(sesion);
    }

    @Override
    @Transactional
    public CajaResponse registrarMovimiento(
            Long usuarioId,
            MovimientoCajaRequest request
    ) {
        if (request.tipo() != TipoMovimientoCaja.ENTRADA
                && request.tipo() != TipoMovimientoCaja.RETIRO) {
            throw new RuntimeException(
                    "Solo se permiten movimientos manuales de ENTRADA o RETIRO"
            );
        }

        CajaSesion sesion = buscarCajaAbierta(usuarioId);

        MovimientoCaja movimiento = MovimientoCaja.builder()
                .cajaSesion(sesion)
                .tipo(request.tipo())
                .metodo(MetodoMovimientoCaja.EFECTIVO)
                .monto(request.monto())
                .concepto(request.concepto())
                .fecha(LocalDateTime.now())
                .build();

        movimientoCajaRepository.save(movimiento);

        actualizarEfectivoEsperado(sesion);

        return convertirResponse(sesion);
    }

    @Override
    @Transactional
    public CajaResponse cerrarCaja(
            Long usuarioId,
            CierreCajaRequest request
    ) {
        CajaSesion sesion = buscarCajaAbierta(usuarioId);

        BigDecimal efectivoEsperado =
                calcularEfectivoEsperado(sesion.getId());

        BigDecimal diferencia = request.efectivoContado()
                .subtract(efectivoEsperado);

        sesion.setEfectivoEsperado(efectivoEsperado);
        sesion.setEfectivoContado(request.efectivoContado());
        sesion.setDiferencia(diferencia);
        sesion.setFechaCierre(LocalDateTime.now());
        sesion.setObservacionesCierre(request.observaciones());
        sesion.setEstado(EstadoCaja.CERRADA);

        CajaSesion cerrada = cajaSesionRepository.save(sesion);

        MovimientoCaja cierre = MovimientoCaja.builder()
                .cajaSesion(cerrada)
                .tipo(TipoMovimientoCaja.CIERRE)
                .metodo(MetodoMovimientoCaja.EFECTIVO)
                .monto(request.efectivoContado())
                .concepto("Cierre de caja")
                .fecha(LocalDateTime.now())
                .build();

        movimientoCajaRepository.save(cierre);

        return convertirResponse(cerrada);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CajaResponse> obtenerHistorial() {
        return cajaSesionRepository
                .findAllByOrderByFechaAperturaDesc()
                .stream()
                .map(this::convertirResponse)
                .toList();
    }

    private CajaSesion buscarCajaAbierta(Long usuarioId) {
        return cajaSesionRepository
                .findFirstByUsuarioIdAndEstadoOrderByFechaAperturaDesc(
                        usuarioId,
                        EstadoCaja.ABIERTA
                )
                .orElseThrow(() ->
                        new RuntimeException(
                                "No existe una caja abierta para el usuario"
                        )
                );
    }

    private void actualizarEfectivoEsperado(CajaSesion sesion) {
        BigDecimal esperado =
                calcularEfectivoEsperado(sesion.getId());

        sesion.setEfectivoEsperado(esperado);
        cajaSesionRepository.save(sesion);
    }

    private BigDecimal calcularEfectivoEsperado(Long sesionId) {
        BigDecimal apertura = sumar(
                sesionId,
                TipoMovimientoCaja.APERTURA,
                MetodoMovimientoCaja.EFECTIVO
        );

        BigDecimal ventas = sumar(
                sesionId,
                TipoMovimientoCaja.VENTA,
                MetodoMovimientoCaja.EFECTIVO
        );

        BigDecimal entradas = sumar(
                sesionId,
                TipoMovimientoCaja.ENTRADA,
                MetodoMovimientoCaja.EFECTIVO
        );

        BigDecimal retiros = sumar(
                sesionId,
                TipoMovimientoCaja.RETIRO,
                MetodoMovimientoCaja.EFECTIVO
        );

        BigDecimal devoluciones = sumar(
                sesionId,
                TipoMovimientoCaja.DEVOLUCION,
                MetodoMovimientoCaja.EFECTIVO
        );

        return apertura
                .add(ventas)
                .add(entradas)
                .subtract(retiros)
                .subtract(devoluciones);
    }

    private BigDecimal sumar(
            Long sesionId,
            TipoMovimientoCaja tipo,
            MetodoMovimientoCaja metodo
    ) {
        BigDecimal resultado =
                movimientoCajaRepository.sumarPorTipoYMetodo(
                        sesionId,
                        tipo,
                        metodo
                );

        return resultado != null ? resultado : BigDecimal.ZERO;
    }

    private CajaResponse convertirResponse(CajaSesion sesion) {
        Long id = sesion.getId();

        BigDecimal ventasEfectivo = sumar(
                id,
                TipoMovimientoCaja.VENTA,
                MetodoMovimientoCaja.EFECTIVO
        );

        BigDecimal ventasTarjeta = sumar(
                id,
                TipoMovimientoCaja.VENTA,
                MetodoMovimientoCaja.TARJETA
        );

        BigDecimal ventasTransferencia = sumar(
                id,
                TipoMovimientoCaja.VENTA,
                MetodoMovimientoCaja.TRANSFERENCIA
        );

        BigDecimal entradas = sumar(
                id,
                TipoMovimientoCaja.ENTRADA,
                MetodoMovimientoCaja.EFECTIVO
        );

        BigDecimal retiros = sumar(
                id,
                TipoMovimientoCaja.RETIRO,
                MetodoMovimientoCaja.EFECTIVO
        );

        BigDecimal devoluciones = sumar(
                id,
                TipoMovimientoCaja.DEVOLUCION,
                MetodoMovimientoCaja.EFECTIVO
        );

        BigDecimal efectivoEsperado =
                sesion.getEstado() == EstadoCaja.ABIERTA
                        ? calcularEfectivoEsperado(id)
                        : sesion.getEfectivoEsperado();

        return new CajaResponse(
                sesion.getId(),
                sesion.getNombreCaja(),
                sesion.getUsuario().getId(),
                sesion.getUsuario().getUsername(),
                sesion.getFechaApertura(),
                sesion.getFechaCierre(),
                sesion.getMontoInicial(),
                ventasEfectivo,
                ventasTarjeta,
                ventasTransferencia,
                entradas,
                retiros,
                devoluciones,
                efectivoEsperado,
                sesion.getEfectivoContado(),
                sesion.getDiferencia(),
                sesion.getEstado(),
                sesion.getObservacionesApertura(),
                sesion.getObservacionesCierre()
        );
    }

    @Override
    @Transactional
    public void registrarVenta(
            Long usuarioId,
            Ventas venta,
            List<VentaPago> pagos,
            BigDecimal cambio
    ) {
        CajaSesion sesion = buscarCajaAbierta(usuarioId);

        BigDecimal cambioPendiente =
                cambio != null
                        ? cambio
                        : BigDecimal.ZERO;

        for (VentaPago pago : pagos) {

            MetodoMovimientoCaja metodo =
                    convertirMetodoPago(pago.getMetodo());

            BigDecimal montoMovimiento =
                    pago.getMonto() != null
                            ? pago.getMonto()
                            : BigDecimal.ZERO;

            /*
             * El cambio únicamente afecta al efectivo.
             */
            if (metodo == MetodoMovimientoCaja.EFECTIVO
                    && cambioPendiente.compareTo(BigDecimal.ZERO) > 0) {

                if (montoMovimiento.compareTo(cambioPendiente) >= 0) {
                    montoMovimiento =
                            montoMovimiento.subtract(cambioPendiente);

                    cambioPendiente = BigDecimal.ZERO;
                } else {
                    /*
                     * Permite soportar varios pagos en efectivo.
                     */
                    cambioPendiente =
                            cambioPendiente.subtract(montoMovimiento);

                    montoMovimiento = BigDecimal.ZERO;
                }
            }

            if (montoMovimiento.compareTo(BigDecimal.ZERO) <= 0) {
                continue;
            }

            MovimientoCaja movimiento = MovimientoCaja.builder()
                    .cajaSesion(sesion)
                    .venta(venta)
                    .tipo(TipoMovimientoCaja.VENTA)
                    .metodo(metodo)
                    .monto(montoMovimiento)
                    .concepto("Venta #" + venta.getId())
                    .fecha(LocalDateTime.now())
                    .build();

            movimientoCajaRepository.save(movimiento);
        }

        actualizarEfectivoEsperado(sesion);
    }

    private MetodoMovimientoCaja convertirMetodoPago(
            MetodoPago metodoPago
    ) {
        return switch (metodoPago) {
            case EFECTIVO ->
                    MetodoMovimientoCaja.EFECTIVO;

            case TARJETA ->
                    MetodoMovimientoCaja.TARJETA;

            case TRANSFERENCIA ->
                    MetodoMovimientoCaja.TRANSFERENCIA;
        };
    }

}

package com.posref.pos.service;

import com.posref.pos.dto.*;
import com.posref.pos.mapper.Mapper;
import com.posref.pos.model.*;
import com.posref.pos.repository.*;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class VentaService implements IVentaService{

    private final VentaRepository ventaRepository;

    private final ProductosRepository productoRepository;

    private final ClientesRepository clienteRepository;

    private final IOrdenTallerRepository ordenTallerRepository;

    private IMotoServicioRepository motoServicioRepository;

    @Override
    public VentaResponse guardarVenta(VentaRequest request) {



        Ventas venta = new Ventas();



        venta.setFecha(LocalDateTime.now());
        venta.setSubtotal(request.getSubtotal());
        venta.setIva(request.getIva());
        venta.setTotal(request.getTotal());

        List<VentaDetalle> detalles = new ArrayList<>();

        for (DetalleRequest item : request.getItems()) {

            VentaDetalle detalle = new VentaDetalle();

            detalle.setVenta(venta);
            detalle.setReferenciaId(String.valueOf(item.getCodigo()));
            detalle.setDescripcion(item.getNombre());
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(item.getPrecio());

            detalle.setSubtotal(
                    item.getPrecio()
                            .multiply(BigDecimal.valueOf(item.getCantidad()))
            );

            detalle.setTipo(item.getTipo());

            detalles.add(detalle);

            System.out.println("ID recibido: " + item.getProductoId());
            System.out.println("Tipo recibido: " + item.getTipo());


            if (item.getTipo() == TipoItemVenta.PRODUCTO) {

                Productos producto =
                        productoRepository.findById(item.getProductoId())
                                .orElseThrow(() ->
                                        new RuntimeException(
                                                "Producto no encontrado: " + item.getProductoId()
                                        )
                                );

                if (producto.getStockActual() < item.getCantidad()) {
                    throw new RuntimeException(
                            "Stock insuficiente para "
                                    + producto.getNombre()
                    );
                }

                producto.setStockActual(
                        producto.getStockActual() - item.getCantidad()
                );

                productoRepository.save(producto);
            }
            /*MotoServicios servicio = null;
            if (item.getTipo() == TipoItemVenta.SERVICIO) {
                servicio = motoServicioRepository.findById(item.getServicioId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Servicio no encontrado: " + item.getId()
                                )
                        );
            }
            assert servicio != null;
            motoServicioRepository.save(servicio);*/
        }

        venta.setDetalles(detalles);

        List<VentaPago> pagos = new ArrayList<>();

        BigDecimal totalPagado = BigDecimal.ZERO;

        for (PagoRequest pagoDto : request.getPagos()) {

            VentaPago pago = new VentaPago();

            pago.setVenta(venta);
            pago.setMetodo(pagoDto.getMetodo());
            pago.setMonto(pagoDto.getMonto());
            pago.setFecha(venta.getFecha());

            totalPagado =
                    totalPagado.add(pagoDto.getMonto());

            pagos.add(pago);
        }

        venta.setPagos(pagos);

        venta.setTotalPagado(totalPagado);

        BigDecimal saldo =
                venta.getTotal().subtract(totalPagado);

        venta.setSaldoPendiente(
                saldo.compareTo(BigDecimal.ZERO) > 0
                        ? saldo
                        : BigDecimal.ZERO
        );

        venta.setEstado(
                saldo.compareTo(BigDecimal.ZERO) <= 0
                        ? EstadoVenta.PAGADA
                        : EstadoVenta.PENDIENTE
        );
        System.out.println("saldo"+ saldo);
        System.out.println("Estado orden"+ venta.getEstado());



        Clientes cliente = clienteRepository
                .findById(Long.valueOf(request.getClienteId()))
                .orElseThrow(() ->
                        new RuntimeException(
                                "Cliente no encontrado: " + request.getClienteId()
                        )
                );

        venta.setCliente(cliente);

        OrdenTaller orden = null;

        if (request.getOrdenServicioId() != null) {

            orden = ordenTallerRepository
                    .findById(request.getOrdenServicioId())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Orden de servicio no encontrada"
                            )
                    );

            /*if (ventaRepository.existsByOrdenTallerId(orden.getId())) {
                throw new RuntimeException(
                        "La orden ya tiene una venta registrada"
                );
            }*/

            orden.setSaldoPendiente(venta.getSaldoPendiente());

            if (orden.getEstado() == EstadoOrdenTrabajo.CANCELADA) {
                throw new RuntimeException(
                        "No se puede cobrar una orden cancelada"
                );
            }

            if (orden.getEstado() == EstadoOrdenTrabajo.PAGADA) {
                throw new RuntimeException(
                        "La orden ya se encuentra pagada"
                );
            }


        }
        //revisar el id de la venta existe? despues checar si esta pagada
        /*if (venta.getSaldoPendiente() != null
                && venta.getSaldoPendiente().compareTo(BigDecimal.ZERO) <= 0) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "La venta ya está pagada"
            );
        }*/
        if (ventaRepository.existsByOrdenTallerId(orden.getId())){
            throw new RuntimeException(
                    "La orden ya esta registrada"
            );
        }else {
            //validar si se tiene que ingresar todo el objeto o solo el ordenTallerId
            venta.setOrdenTaller(orden);
        }
        //return ventaRepository.save(venta);
        return Mapper.convertirVentaResponse(ventaRepository.save(venta));
    }

    @Override
    @Transactional(readOnly = true)
    public List<VentaResponse> obtenerVentas() {

        //List<Ventas> ventas = ventaRepository.findAll();

        /*return ventas.stream()
                .map(this::convertirVentaResponse)
                .collect(Collectors.toList());*/
        return ventaRepository.findAll().stream().map(Mapper::convertirVentaResponse).toList();
    }

    @Override
    public Ventas obtenerPorId(Long id) {
        return ventaRepository.findById(id)
                .orElseThrow();
    }

    @Override
    @Transactional
    public VentaResponse cobrarVenta(Long ventaId, CobroVentaRequest request) {

        Ventas venta = ventaRepository.findById(ventaId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Venta no encontrada con id: " + ventaId
                        )
                );

        validarVentaPendiente(venta);
        validarPagos(request);

        BigDecimal nuevoPago = calcularNuevoPago(request.getPagos());

        BigDecimal saldoActual = obtenerSaldoActual(venta);

        if (nuevoPago.compareTo(saldoActual) > 0) {
            throw new IllegalArgumentException(
                    "El pago no puede ser mayor al saldo pendiente. " +
                            "Saldo actual: " + saldoActual +
                            ", pago recibido: " + nuevoPago
            );
        }

        for (PagoRequest pagoRequest : request.getPagos()) {

            VentaPago pago = VentaPago.builder()
                    .venta(venta)
                    .metodo(pagoRequest.getMetodo())
                    .monto(pagoRequest.getMonto())
                    .fecha(LocalDateTime.now())
                    .build();

            venta.agregarPago(pago);
        }

        BigDecimal totalPagadoActual =
                venta.getTotalPagado() != null
                        ? venta.getTotalPagado()
                        : BigDecimal.ZERO;

        BigDecimal nuevoTotalPagado =
                totalPagadoActual.add(nuevoPago);

        BigDecimal nuevoSaldo =
                venta.getTotal().subtract(nuevoTotalPagado);

        if (nuevoSaldo.compareTo(BigDecimal.ZERO) < 0) {
            nuevoSaldo = BigDecimal.ZERO;
        }

        venta.setTotalPagado(nuevoTotalPagado);
        venta.setSaldoPendiente(nuevoSaldo);

        if (nuevoSaldo.compareTo(BigDecimal.ZERO) == 0) {
            venta.setEstado(EstadoVenta.valueOf("PAGADA"));
        } else {
            venta.setEstado(EstadoVenta.valueOf("PENDIENTE"));
        }

        Ventas ventaActualizada = ventaRepository.save(venta);

        return Mapper.convertirVentaResponse(ventaActualizada);
    }


    private void validarVentaPendiente(Ventas venta) {
        if ("PAGADA".equalsIgnoreCase(String.valueOf(venta.getEstado()))) {
            throw new IllegalStateException(
                    "La venta " + venta.getId() + " ya se encuentra pagada"
            );
        }
    }

    private void validarPagos(CobroVentaRequest request) {
        if (request == null ||
                request.getPagos() == null ||
                request.getPagos().isEmpty()) {

            throw new IllegalArgumentException(
                    "Debe proporcionar al menos un método de pago"
            );
        }

        for (PagoRequest pago : request.getPagos()) {

            if (pago.getMetodo() == null ) {

                throw new IllegalArgumentException(
                        "El método de pago es obligatorio"
                );
            }

            if (pago.getMonto() == null ||
                    pago.getMonto().compareTo(BigDecimal.ZERO) <= 0) {

                throw new IllegalArgumentException(
                        "El monto de cada pago debe ser mayor a cero"
                );
            }

            validarMetodoPago(String.valueOf(pago.getMetodo()));
        }
    }

    private void validarMetodoPago(String metodo) {
        boolean valido =
                "EFECTIVO".equalsIgnoreCase(metodo) ||
                        "TARJETA".equalsIgnoreCase(metodo) ||
                        "TRANSFERENCIA".equalsIgnoreCase(metodo);

        if (!valido) {
            throw new IllegalArgumentException(
                    "Método de pago no válido: " + metodo
            );
        }
    }

    private BigDecimal calcularNuevoPago(List<PagoRequest> pagos) {
        return pagos.stream()
                .map(PagoRequest::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal obtenerSaldoActual(Ventas venta) {
        if (venta.getSaldoPendiente() != null) {
            return venta.getSaldoPendiente();
        }

        BigDecimal totalPagado =
                venta.getTotalPagado() != null
                        ? venta.getTotalPagado()
                        : BigDecimal.ZERO;

        return venta.getTotal().subtract(totalPagado);
    }
}
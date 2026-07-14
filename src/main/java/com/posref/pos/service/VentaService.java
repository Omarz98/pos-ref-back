package com.posref.pos.service;

import com.posref.pos.dto.DetalleRequest;
import com.posref.pos.dto.PagoRequest;
import com.posref.pos.dto.VentaRequest;
import com.posref.pos.model.*;
import com.posref.pos.repository.ClientesRepository;
import com.posref.pos.repository.ProductosRepository;
import com.posref.pos.repository.VentaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class VentaService implements IVentaService{

    private final VentaRepository ventaRepository;

    private final ProductosRepository productoRepository;

    private final ClientesRepository clienteRepository;

    @Override
    public Ventas guardarVenta(VentaRequest request) {

        Ventas venta = new Ventas();

        venta.setFecha(LocalDateTime.now());
        venta.setSubtotal(request.getSubtotal());
        venta.setIva(request.getIva());
        venta.setTotal(request.getTotal());

        List<VentaDetalle> detalles = new ArrayList<>();

        for (DetalleRequest item : request.getItems()) {

            VentaDetalle detalle = new VentaDetalle();

            detalle.setVenta(venta);
            detalle.setReferenciaId(item.getId());
            detalle.setDescripcion(item.getNombre());
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(item.getPrecio());

            detalle.setSubtotal(
                    item.getPrecio()
                            .multiply(BigDecimal.valueOf(item.getCantidad()))
            );

            detalle.setTipo(item.getTipo());

            detalles.add(detalle);

            System.out.println("ID recibido: " + item.getId());
            System.out.println("Tipo recibido: " + item.getTipo());

            if (item.getTipo() == TipoItemVenta.PRODUCTO) {

                Productos producto =
                        productoRepository.findByCodigo(item.getId())
                                .orElseThrow(() ->
                                        new RuntimeException(
                                                "Producto no encontrado: " + item.getId()
                                        )
                                );

                if (producto.getStock() < item.getCantidad()) {
                    throw new RuntimeException(
                            "Stock insuficiente para "
                                    + producto.getNombre()
                    );
                }

                producto.setStock(
                        producto.getStock() - item.getCantidad()
                );

                productoRepository.save(producto);
            }
        }

        venta.setDetalles(detalles);

        List<VentaPago> pagos = new ArrayList<>();

        BigDecimal totalPagado = BigDecimal.ZERO;

        for (PagoRequest pagoDto : request.getPagos()) {

            VentaPago pago = new VentaPago();

            pago.setVenta(venta);
            pago.setMetodo(pagoDto.getMetodo());
            pago.setMonto(pagoDto.getMonto());

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

        Clientes cliente = clienteRepository
                .findById(Long.valueOf(request.getClienteId()))
                .orElseThrow(() ->
                        new RuntimeException(
                                "Cliente no encontrado: " + request.getClienteId()
                        )
                );

        venta.setCliente(cliente);

        return ventaRepository.save(venta);
    }

    @Override
    public List<Ventas> obtenerVentas() {
        return ventaRepository.findAll();
    }

    @Override
    public Ventas obtenerPorId(Long id) {
        return ventaRepository.findById(id)
                .orElseThrow();
    }
}
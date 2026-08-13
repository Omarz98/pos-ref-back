package com.posref.pos.service;

import com.posref.pos.dto.*;
import com.posref.pos.mapper.Mapper;
import com.posref.pos.model.*;
import com.posref.pos.model.seguridad.Usuario;
import com.posref.pos.repository.*;

import com.posref.pos.repository.seguridad.UsuarioRepository;
import com.posref.pos.service.caja.CajaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
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

    private final UsuarioRepository usuarioRepository;
    private final CajaService cajaService;

//    @Override
//    public VentaResponse guardarVenta(VentaRequest request) {
//
//
//
//        Ventas venta = new Ventas();
//
//
//
//        venta.setFecha(LocalDateTime.now());
//        venta.setSubtotal(request.getSubtotal());
//        venta.setIva(request.getIva());
//        venta.setTotal(request.getTotal());
//
//        List<VentaDetalle> detalles = new ArrayList<>();
//
//        for (DetalleRequest item : request.getItems()) {
//
//            VentaDetalle detalle = new VentaDetalle();
//
//            detalle.setVenta(venta);
//            detalle.setReferenciaId(String.valueOf(item.getCodigo()));
//            detalle.setDescripcion(item.getNombre());
//            detalle.setCantidad(item.getCantidad());
//            detalle.setPrecioUnitario(item.getPrecio());
//
//            detalle.setSubtotal(
//                    item.getPrecio()
//                            .multiply(BigDecimal.valueOf(item.getCantidad()))
//            );
//
//            detalle.setTipo(item.getTipo());
//
//            detalles.add(detalle);
//
//            System.out.println("ID recibido: " + item.getProductoId());
//            System.out.println("Tipo recibido: " + item.getTipo());
//
//
//            if (item.getTipo() == TipoItemVenta.PRODUCTO) {
//
//                Productos producto =
//                        productoRepository.findById(item.getProductoId())
//                                .orElseThrow(() ->
//                                        new RuntimeException(
//                                                "Producto no encontrado: " + item.getProductoId()
//                                        )
//                                );
//
//                if (producto.getStockActual() < item.getCantidad()) {
//                    throw new RuntimeException(
//                            "Stock insuficiente para "
//                                    + producto.getNombre()
//                    );
//                }
//
//                producto.setStockActual(
//                        producto.getStockActual() - item.getCantidad()
//                );
//
//                productoRepository.save(producto);
//            }
//            /*MotoServicios servicio = null;
//            if (item.getTipo() == TipoItemVenta.SERVICIO) {
//                servicio = motoServicioRepository.findById(item.getServicioId())
//                        .orElseThrow(() ->
//                                new RuntimeException(
//                                        "Servicio no encontrado: " + item.getId()
//                                )
//                        );
//            }
//            assert servicio != null;
//            motoServicioRepository.save(servicio);*/
//        }
//
//        venta.setDetalles(detalles);
//
//        List<VentaPago> pagos = new ArrayList<>();
//
//        BigDecimal totalPagado = BigDecimal.ZERO;
//
//        for (PagoRequest pagoDto : request.getPagos()) {
//
//            VentaPago pago = new VentaPago();
//
//            pago.setVenta(venta);
//            pago.setMetodo(pagoDto.getMetodo());
//            pago.setMonto(pagoDto.getMonto());
//            pago.setFecha(venta.getFecha());
//
//            totalPagado =
//                    totalPagado.add(pagoDto.getMonto());
//
//            pagos.add(pago);
//        }
//
//        venta.setPagos(pagos);
//
//        venta.setTotalPagado(totalPagado);
//
//        BigDecimal saldo =
//                venta.getTotal().subtract(totalPagado);
//
//        venta.setSaldoPendiente(
//                saldo.compareTo(BigDecimal.ZERO) > 0
//                        ? saldo
//                        : BigDecimal.ZERO
//        );
//
//        venta.setEstado(
//                saldo.compareTo(BigDecimal.ZERO) <= 0
//                        ? EstadoVenta.PAGADA
//                        : EstadoVenta.PENDIENTE
//        );
//        System.out.println("saldo"+ saldo);
//        System.out.println("Estado orden"+ venta.getEstado());
//
//
//
//        Clientes cliente = clienteRepository
//                .findById(Long.valueOf(request.getClienteId()))
//                .orElseThrow(() ->
//                        new RuntimeException(
//                                "Cliente no encontrado: " + request.getClienteId()
//                        )
//                );
//
//        venta.setCliente(cliente);
//
//        OrdenTaller orden = null;
//
//        if (request.getOrdenServicioId() != null) {
//
//            orden = ordenTallerRepository
//                    .findById(request.getOrdenServicioId())
//                    .orElseThrow(() ->
//                            new RuntimeException(
//                                    "Orden de servicio no encontrada"
//                            )
//                    );
//
//            /*if (ventaRepository.existsByOrdenTallerId(orden.getId())) {
//                throw new RuntimeException(
//                        "La orden ya tiene una venta registrada"
//                );
//            }*/
//
//            orden.setSaldoPendiente(venta.getSaldoPendiente());
//
//            if (orden.getEstado() == EstadoOrdenTrabajo.CANCELADA) {
//                throw new RuntimeException(
//                        "No se puede cobrar una orden cancelada"
//                );
//            }
//
//            if (orden.getEstado() == EstadoOrdenTrabajo.PAGADA) {
//                throw new RuntimeException(
//                        "La orden ya se encuentra pagada"
//                );
//            }
//
//
//        }
//        //revisar el id de la venta existe? despues checar si esta pagada
//        /*if (venta.getSaldoPendiente() != null
//                && venta.getSaldoPendiente().compareTo(BigDecimal.ZERO) <= 0) {
//
//            throw new ResponseStatusException(
//                    HttpStatus.CONFLICT,
//                    "La venta ya está pagada"
//            );
//        }*/
//        if (ventaRepository.existsByOrdenTallerId(orden.getId())){
//            throw new RuntimeException(
//                    "La orden ya esta registrada"
//            );
//        }else {
//            //validar si se tiene que ingresar todo el objeto o solo el ordenTallerId
//            venta.setOrdenTaller(orden);
//        }
//        //return ventaRepository.save(venta);
//        return Mapper.convertirVentaResponse(ventaRepository.save(venta));
//    }

    @Override
    @Transactional
    public VentaResponse guardarVenta(VentaRequest request) {

        /*
         * 1. Obtener el usuario autenticado.
         */
        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        Usuario usuario = usuarioRepository
                .findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Usuario autenticado no encontrado: " + username
                        )
                );

        /*
         * 2. Validar que el usuario tenga una caja abierta.
         *
         * Si no existe una caja abierta, este método debe lanzar
         * una excepción y no se registra la venta.
         */
        cajaService.obtenerCajaAbierta(usuario.getId());

        /*
         * 3. Validaciones generales de la solicitud.
         */
        if (request == null) {
            throw new RuntimeException(
                    "La información de la venta es obligatoria"
            );
        }

        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new RuntimeException(
                    "La venta debe contener al menos un producto o servicio"
            );
        }

        if (request.getPagos() == null || request.getPagos().isEmpty()) {
            throw new RuntimeException(
                    "Debe registrar al menos un método de pago"
            );
        }

        if (request.getTotal() == null
                || request.getTotal().compareTo(BigDecimal.ZERO) <= 0) {

            throw new RuntimeException(
                    "El total de la venta debe ser mayor a cero"
            );
        }

        /*
         * 4. Buscar al cliente.
         */
        if (request.getClienteId() == null) {
            throw new RuntimeException(
                    "Debe seleccionar un cliente"
            );
        }

        Clientes cliente = clienteRepository
                .findById(Long.valueOf(request.getClienteId()))
                .orElseThrow(() ->
                        new RuntimeException(
                                "Cliente no encontrado: "
                                        + request.getClienteId()
                        )
                );

        /*
         * 5. Crear la venta principal.
         */
        Ventas venta = new Ventas();

        venta.setFecha(LocalDateTime.now());
        venta.setCliente(cliente);
        venta.setSubtotal(request.getSubtotal());
        venta.setIva(request.getIva());
        venta.setTotal(request.getTotal());

        /*
         * 6. Procesar una orden de taller solamente cuando venga
         * informada en la solicitud.
         */
        OrdenTaller orden = null;

        if (request.getOrdenServicioId() != null) {

            orden = ordenTallerRepository
                    .findById(request.getOrdenServicioId())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Orden de servicio no encontrada: "
                                            + request.getOrdenServicioId()
                            )
                    );

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

            /*
             * Validar que no exista una venta previa para la orden.
             */
            if (ventaRepository.existsByOrdenTallerId(orden.getId())) {
                throw new RuntimeException(
                        "La orden ya tiene una venta registrada"
                );
            }

            venta.setOrdenTaller(orden);
        }

        /*
         * 7. Construir los detalles y descontar inventario.
         */
        List<VentaDetalle> detalles = new ArrayList<>();

        for (DetalleRequest item : request.getItems()) {

            if (item.getTipo() == null) {
                throw new RuntimeException(
                        "El tipo del artículo es obligatorio"
                );
            }

            if (item.getCantidad() == null || item.getCantidad() <= 0) {
                throw new RuntimeException(
                        "La cantidad debe ser mayor a cero para: "
                                + item.getNombre()
                );
            }

            if (item.getPrecio() == null
                    || item.getPrecio().compareTo(BigDecimal.ZERO) < 0) {

                throw new RuntimeException(
                        "El precio no es válido para: "
                                + item.getNombre()
                );
            }

            VentaDetalle detalle = new VentaDetalle();

            detalle.setVenta(venta);
            detalle.setReferenciaId(
                    item.getCodigo() != null
                            ? String.valueOf(item.getCodigo())
                            : null
            );
            detalle.setDescripcion(item.getNombre());
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(item.getPrecio());
            detalle.setTipo(item.getTipo());

            BigDecimal subtotalDetalle = item.getPrecio()
                    .multiply(
                            BigDecimal.valueOf(item.getCantidad())
                    );

            detalle.setSubtotal(subtotalDetalle);

            /*
             * Descontar inventario únicamente para productos.
             */
            if (item.getTipo() == TipoItemVenta.PRODUCTO) {

                if (item.getProductoId() == null) {
                    throw new RuntimeException(
                            "El producto no contiene un identificador: "
                                    + item.getNombre()
                    );
                }

                Productos producto = productoRepository
                        .findById(item.getProductoId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Producto no encontrado: "
                                                + item.getProductoId()
                                )
                        );

                if (producto.getStockActual() == null) {
                    throw new RuntimeException(
                            "El producto no tiene stock configurado: "
                                    + producto.getNombre()
                    );
                }

                if (producto.getStockActual() < item.getCantidad()) {
                    throw new RuntimeException(
                            "Stock insuficiente para "
                                    + producto.getNombre()
                                    + ". Disponible: "
                                    + producto.getStockActual()
                                    + ", solicitado: "
                                    + item.getCantidad()
                    );
                }

                producto.setStockActual(
                        producto.getStockActual()
                                - item.getCantidad()
                );

                /*
                 * No es indispensable llamar save() si Productos
                 * está administrado por JPA y el método es transaccional.
                 * Puede conservarse para hacerlo explícito.
                 */
                productoRepository.save(producto);
            }

            /*
             * Para SERVICIO no se descuenta inventario.
             * El detalle conserva el servicioId o código como referencia.
             */
            detalles.add(detalle);
        }

        venta.setDetalles(detalles);

        /*
         * 8. Construir los pagos.
         */
        List<VentaPago> pagos = new ArrayList<>();
        BigDecimal totalPagado = BigDecimal.ZERO;

        for (PagoRequest pagoDto : request.getPagos()) {

            if (pagoDto.getMetodo() == null) {
                throw new RuntimeException(
                        "El método de pago es obligatorio"
                );
            }

            if (pagoDto.getMonto() == null
                    || pagoDto.getMonto().compareTo(BigDecimal.ZERO) <= 0) {

                throw new RuntimeException(
                        "El monto del pago debe ser mayor a cero"
                );
            }

            VentaPago pago = new VentaPago();

            pago.setVenta(venta);
            pago.setMetodo(pagoDto.getMetodo());
            pago.setMonto(pagoDto.getMonto());
            pago.setFecha(venta.getFecha());

            pagos.add(pago);

            totalPagado = totalPagado.add(
                    pagoDto.getMonto()
            );
        }

        venta.setPagos(pagos);
        venta.setTotalPagado(totalPagado);

        /*
         * 9. Calcular saldo pendiente.
         */
        BigDecimal saldoCalculado = venta.getTotal()
                .subtract(totalPagado);

        BigDecimal saldoPendiente =
                saldoCalculado.compareTo(BigDecimal.ZERO) > 0
                        ? saldoCalculado
                        : BigDecimal.ZERO;

        venta.setSaldoPendiente(saldoPendiente);

        venta.setEstado(
                saldoPendiente.compareTo(BigDecimal.ZERO) == 0
                        ? EstadoVenta.PAGADA
                        : EstadoVenta.PENDIENTE
        );

        /*
         * 10. Actualizar la orden relacionada.
         */
        if (orden != null) {

            orden.setSaldoPendiente(saldoPendiente);

            if (saldoPendiente.compareTo(BigDecimal.ZERO) == 0) {
                orden.setEstado(EstadoOrdenTrabajo.PAGADA);
            }

            ordenTallerRepository.save(orden);
        }

        /*
         * 11. Guardar la venta.
         *
         * Para que detalles y pagos se guarden automáticamente,
         * las relaciones de Ventas deben tener cascade = CascadeType.ALL.
         */
        Ventas ventaGuardada = ventaRepository.save(venta);

        /*
         * 12. Calcular cambio.
         *
         * Cuando el total pagado supera el total de la venta,
         * la diferencia corresponde al cambio que se devuelve.
         */
        BigDecimal cambio =
                totalPagado.compareTo(venta.getTotal()) > 0
                        ? totalPagado.subtract(venta.getTotal())
                        : BigDecimal.ZERO;

        /*
         * 13. Registrar los pagos como movimientos de caja.
         *
         * El servicio descontará el cambio únicamente del pago
         * realizado en efectivo.
         */
        cajaService.registrarVenta(
                usuario.getId(),
                ventaGuardada,
                ventaGuardada.getPagos(),
                cambio
        );

        /*
         * 14. Regresar un DTO para evitar errores de serialización
         * con relaciones LAZY y ByteBuddyInterceptor.
         */
        return Mapper.convertirVentaResponse(ventaGuardada);
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

//    @Override
//    @Transactional
//    public VentaResponse cobrarVenta(Long ventaId, CobroVentaRequest request) {
//
//        Ventas venta = ventaRepository.findById(ventaId)
//                .orElseThrow(() ->
//                        new RuntimeException(
//                                "Venta no encontrada con id: " + ventaId
//                        )
//                );
//
//        validarVentaPendiente(venta);
//        validarPagos(request);
//
//        BigDecimal nuevoPago = calcularNuevoPago(request.getPagos());
//
//        BigDecimal saldoActual = obtenerSaldoActual(venta);
//
//        if (nuevoPago.compareTo(saldoActual) > 0) {
//            throw new IllegalArgumentException(
//                    "El pago no puede ser mayor al saldo pendiente. " +
//                            "Saldo actual: " + saldoActual +
//                            ", pago recibido: " + nuevoPago
//            );
//        }
//
//        for (PagoRequest pagoRequest : request.getPagos()) {
//
//            VentaPago pago = VentaPago.builder()
//                    .venta(venta)
//                    .metodo(pagoRequest.getMetodo())
//                    .monto(pagoRequest.getMonto())
//                    .fecha(LocalDateTime.now())
//                    .build();
//
//            venta.agregarPago(pago);
//        }
//
//        BigDecimal totalPagadoActual =
//                venta.getTotalPagado() != null
//                        ? venta.getTotalPagado()
//                        : BigDecimal.ZERO;
//
//        BigDecimal nuevoTotalPagado =
//                totalPagadoActual.add(nuevoPago);
//
//        BigDecimal nuevoSaldo =
//                venta.getTotal().subtract(nuevoTotalPagado);
//
//        if (nuevoSaldo.compareTo(BigDecimal.ZERO) < 0) {
//            nuevoSaldo = BigDecimal.ZERO;
//        }
//
//        venta.setTotalPagado(nuevoTotalPagado);
//        venta.setSaldoPendiente(nuevoSaldo);
//
//        if (nuevoSaldo.compareTo(BigDecimal.ZERO) == 0) {
//            venta.setEstado(EstadoVenta.valueOf("PAGADA"));
//        } else {
//            venta.setEstado(EstadoVenta.valueOf("PENDIENTE"));
//        }
//
//        Ventas ventaActualizada = ventaRepository.save(venta);
//
//        return Mapper.convertirVentaResponse(ventaActualizada);
//    }

    @Override
    @Transactional
    public VentaResponse cobrarVenta(
            Long ventaId,
            CobroVentaRequest request
    ) {

        /*
         * 1. Obtener usuario autenticado
         */
        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        Usuario usuario = usuarioRepository
                .findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Usuario autenticado no encontrado: " + username
                        )
                );

        /*
         * 2. Validar que exista una caja abierta
         */
        cajaService.obtenerCajaAbierta(usuario.getId());

        /*
         * 3. Buscar venta
         */
        Ventas venta = ventaRepository
                .findById(ventaId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Venta no encontrada con id: " + ventaId
                        )
                );

        /*
         * 4. Validaciones
         */
        validarVentaPendiente(venta);
        validarPagos(request);

        /*
         * 5. Calcular cuánto está entregando el cliente
         *
         * Ejemplo:
         *
         * saldo pendiente = 18
         * cliente entrega = 20
         *
         * nuevoPago = 20
         */
        BigDecimal nuevoPago =
                calcularNuevoPago(request.getPagos());

        /*
         * 6. Obtener saldo actual
         */
        BigDecimal saldoActual =
                obtenerSaldoActual(venta);

        /*
         * 7. Validar saldo
         */
        if (saldoActual == null
                || saldoActual.compareTo(BigDecimal.ZERO) <= 0) {

            throw new RuntimeException(
                    "La venta no tiene saldo pendiente"
            );
        }

        /*
         * 8. Calcular cambio
         *
         * saldo = 18
         * recibido = 20
         *
         * cambio = 2
         */
        BigDecimal cambio =
                nuevoPago.compareTo(saldoActual) > 0
                        ? nuevoPago.subtract(saldoActual)
                        : BigDecimal.ZERO;

        /*
         * 9. El importe realmente aplicado a la venta
         *
         * Si debe 18 y entrega 20:
         *
         * pagoAplicado = 18
         */
        BigDecimal pagoAplicado =
                nuevoPago.min(saldoActual);

        /*
         * 10. Validar que el cambio pueda salir del efectivo.
         *
         * El cambio solamente debe generarse cuando
         * existe suficiente EFECTIVO.
         *
         * Ejemplo incorrecto:
         *
         * saldo = 18
         * tarjeta = 20
         *
         * No podemos devolver $2 de cambio de una tarjeta.
         */
        if (cambio.compareTo(BigDecimal.ZERO) > 0) {

            BigDecimal efectivoRecibido =
                    request.getPagos()
                            .stream()
                            .filter(pago ->
                                    pago.getMetodo() != null
                                            && "EFECTIVO".equals(
                                            pago.getMetodo().name()
                                    )
                            )
                            .map(PagoRequest::getMonto)
                            .filter(monto -> monto != null)
                            .reduce(
                                    BigDecimal.ZERO,
                                    BigDecimal::add
                            );

            if (efectivoRecibido.compareTo(cambio) < 0) {

                throw new IllegalArgumentException(
                        "El cambio debe poder descontarse del pago en efectivo. "
                                + "Cambio: " + cambio
                                + ", efectivo recibido: "
                                + efectivoRecibido
                );
            }
        }

        /*
         * 11. Lista únicamente de pagos generados
         *     durante este cobro.
         *
         * IMPORTANTE:
         *
         * No utilizamos posteriormente venta.getPagos()
         * para caja porque ahí también están los pagos
         * anteriores y podríamos duplicar movimientos.
         */
        List<VentaPago> nuevosPagos =
                new ArrayList<>();

        /*
         * Cambio que todavía falta descontar
         * de los pagos en efectivo.
         */
        BigDecimal cambioPendiente =
                cambio;

        /*
         * 12. Construir pagos
         */
        for (PagoRequest pagoRequest : request.getPagos()) {

            if (pagoRequest.getMetodo() == null) {
                throw new RuntimeException(
                        "El método de pago es obligatorio"
                );
            }

            if (pagoRequest.getMonto() == null
                    || pagoRequest.getMonto()
                    .compareTo(BigDecimal.ZERO) <= 0) {

                throw new RuntimeException(
                        "El monto del pago debe ser mayor a cero"
                );
            }

            /*
             * Monto recibido mediante este método.
             */
            BigDecimal montoPago =
                    pagoRequest.getMonto();

            /*
             * Si es efectivo y existe cambio,
             * descontarlo.
             */
            if ("EFECTIVO".equals(
                    pagoRequest.getMetodo().name()
            )
                    && cambioPendiente.compareTo(
                    BigDecimal.ZERO
            ) > 0) {

                /*
                 * Determinar cuánto cambio podemos
                 * descontar de este pago.
                 */
                BigDecimal descuentoCambio =
                        montoPago.min(cambioPendiente);

                /*
                 * Monto neto aplicado
                 */
                montoPago =
                        montoPago.subtract(
                                descuentoCambio
                        );

                /*
                 * Reducimos el cambio pendiente
                 */
                cambioPendiente =
                        cambioPendiente.subtract(
                                descuentoCambio
                        );
            }

            /*
             * Si después de descontar cambio
             * queda algún monto, registramos el pago.
             */
            if (montoPago.compareTo(
                    BigDecimal.ZERO
            ) > 0) {

                VentaPago pago =
                        VentaPago.builder()
                                .venta(venta)
                                .metodo(
                                        pagoRequest.getMetodo()
                                )
                                .monto(montoPago)
                                .fecha(
                                        LocalDateTime.now()
                                )
                                .build();

                /*
                 * Agregar a la venta
                 */
                venta.agregarPago(pago);

                /*
                 * Guardar únicamente como nuevo pago
                 * para posteriormente mandarlo a caja.
                 */
                nuevosPagos.add(pago);
            }
        }

        /*
         * 13. Validación adicional
         *
         * En teoría debería quedar en cero porque
         * anteriormente validamos que exista suficiente
         * efectivo para devolver el cambio.
         */
        if (cambioPendiente.compareTo(
                BigDecimal.ZERO
        ) > 0) {

            throw new RuntimeException(
                    "No fue posible descontar completamente "
                            + "el cambio del pago en efectivo. "
                            + "Cambio pendiente: "
                            + cambioPendiente
            );
        }

        /*
         * 14. Obtener total pagado anteriormente
         */
        BigDecimal totalPagadoActual =
                venta.getTotalPagado() != null
                        ? venta.getTotalPagado()
                        : BigDecimal.ZERO;

        /*
         * 15. Actualizar total pagado
         *
         * IMPORTANTE:
         *
         * No sumamos nuevoPago porque ahí viene
         * incluido el cambio.
         *
         * Ejemplo:
         *
         * totalPagadoActual = 82
         * saldo = 18
         * entrega = 20
         * cambio = 2
         *
         * pagoAplicado = 18
         *
         * nuevoTotalPagado = 100
         *
         * NO 102.
         */
        BigDecimal nuevoTotalPagado =
                totalPagadoActual.add(
                        pagoAplicado
                );

        /*
         * 16. Calcular nuevo saldo
         */
        BigDecimal nuevoSaldo =
                saldoActual.subtract(
                        pagoAplicado
                );

        if (nuevoSaldo.compareTo(
                BigDecimal.ZERO
        ) < 0) {

            nuevoSaldo =
                    BigDecimal.ZERO;
        }

        /*
         * 17. Actualizar venta
         */
        venta.setTotalPagado(
                nuevoTotalPagado
        );

        venta.setSaldoPendiente(
                nuevoSaldo
        );

        /*
         * 18. Actualizar estado
         */
        venta.setEstado(
                nuevoSaldo.compareTo(
                        BigDecimal.ZERO
                ) == 0
                        ? EstadoVenta.PAGADA
                        : EstadoVenta.PENDIENTE
        );

        /*
         * 19. Actualizar orden de taller
         *     cuando la venta provenga de una orden.
         */
        if (venta.getOrdenTaller() != null) {

            OrdenTaller orden =
                    venta.getOrdenTaller();

            orden.setSaldoPendiente(
                    nuevoSaldo
            );

            if (nuevoSaldo.compareTo(
                    BigDecimal.ZERO
            ) == 0) {

                orden.setEstado(
                        EstadoOrdenTrabajo.PAGADA
                );
            }

            ordenTallerRepository.save(
                    orden
            );
        }

        /*
         * 20. Guardar venta
         *
         * Si Ventas.pagos tiene CascadeType.ALL,
         * los nuevos VentaPago se guardarán junto
         * con la venta.
         */
        Ventas ventaActualizada =
                ventaRepository.save(
                        venta
                );

        /*
         * 21. Registrar movimiento de caja
         *
         * MUY IMPORTANTE:
         *
         * nuevosPagos YA tienen descontado el cambio.
         *
         * Por ejemplo:
         *
         * recibido = 20
         * saldo = 18
         * cambio = 2
         *
         * VentaPago guardado:
         *
         * EFECTIVO = 18
         *
         * Por eso aquí mandamos cambio = ZERO,
         * para no descontarlo dos veces.
         */
        cajaService.registrarVenta(
                usuario.getId(),
                ventaActualizada,
                nuevosPagos,
                BigDecimal.ZERO
        );

        /*
         * 22. Regresar DTO
         */
        return Mapper.convertirVentaResponse(
                ventaActualizada
        );
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
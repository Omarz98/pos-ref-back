package com.posref.pos.service;

import com.posref.pos.dto.*;
import com.posref.pos.dto.taller.ActualizarOrdenTallerRequest;
import com.posref.pos.dto.taller.AsignarTecnicoOrdenRequest;
import com.posref.pos.dto.taller.CambiarEstadoOrdenRequest;
import com.posref.pos.dto.taller.HistorialOrdenResponse;
import com.posref.pos.model.*;
import com.posref.pos.model.seguridad.Usuario;
import com.posref.pos.model.taller.OrdenTallerHistorial;
import com.posref.pos.repository.*;
import com.posref.pos.repository.seguridad.UsuarioRepository;
import com.posref.pos.repository.taller.IOrdenTallerHistorialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OrdenTallerService
        implements IOrdenTallerService {

    private static final BigDecimal PORCENTAJE_IVA =
            new BigDecimal("0.16");

    private final IOrdenTallerRepository ordenTallerRepository;
    private final IOrdenTallerServicioRepository ordenTallerServicioRepository;
    private final IOrdenTallerProductoRepository ordenTallerProductoRepository;
    private final IOrdenTallerHistorialRepository historialRepository;

    private final ClientesRepository clienteRepository;
    private final IClienteMotoRepository clienteMotoRepository;
    private final IMotoServicioRepository motoServicioRepository;
    private final ProductosRepository productoRepository;
    private final UsuarioRepository usuarioRepository;
    private final VentaRepository ventaRepository;

    @Override
    @Transactional
    public OrdenTallerResponse guardar(
            OrdenTallerRequest request
    ) {

        validarContenidoOrden(request);

        Clientes cliente = clienteRepository
                .findById(request.getClienteId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "No existe el cliente con id "
                                        + request.getClienteId()
                        )
                );

        Motocicleta motocicleta = clienteMotoRepository
                .findById(request.getMotoClienteId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "No existe la motocicleta con id "
                                        + request.getMotoClienteId()
                        )
                );

        validarPropietarioMoto(cliente, motocicleta);

        LocalDateTime ahora = LocalDateTime.now();

        OrdenTaller orden = OrdenTaller.builder()
                .folio(generarFolio())
                .cliente(cliente)
                .motocicleta(motocicleta)
                .fechaRecepcion(
                        request.getFechaRecepcion() != null
                                ? request.getFechaRecepcion()
                                : ahora
                )
                .fechaEntregaEstimada(
                        request.getFechaEntregaEstimada()
                )
                .kilometraje(request.getKilometraje())
                .nivelCombustible(request.getNivelCombustible())
                .prioridad(
                        request.getPrioridad() != null
                                ? request.getPrioridad()
                                : PrioridadOrdenTaller.NORMAL
                )
                .estado(EstadoOrdenTrabajo.RECIBIDA)
                .fallaReportada(request.getFallaReportada())
                .diagnosticoInicial(
                        request.getDiagnosticoInicial()
                )
                .observaciones(request.getObservaciones())
                .subtotalServicios(BigDecimal.ZERO)
                .subtotalProductos(BigDecimal.ZERO)
                .subtotal(BigDecimal.ZERO)
                .iva(BigDecimal.ZERO)
                .total(BigDecimal.ZERO)
                .anticipo(
                        normalizarImporte(request.getAnticipo())
                )
                .saldoPendiente(BigDecimal.ZERO)
                .fechaCreacion(ahora)
                .fechaActualizacion(ahora)
                .build();

        agregarServiciosIniciales(
                orden,
                request.getServicios()
        );

        agregarProductosIniciales(
                orden,
                request.getProductos()
        );

        calcularTotales(orden);
        validarAnticipo(orden);

        OrdenTaller guardada =
                ordenTallerRepository.save(orden);

        registrarHistorial(
                guardada,
                null,
                null,
                EstadoOrdenTrabajo.RECIBIDA,
                "CREACION",
                "Orden de trabajo creada"
        );

        return convertirResponse(guardada);
    }

    @Override
    @Transactional(readOnly = true)
    public OrdenTallerResponse obtenerPorId(Long id) {
        return convertirResponse(buscarOrden(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdenTallerResponse> obtenerTodas() {
        return ordenTallerRepository
                .findAllByOrderByFechaRecepcionDesc()
                .stream()
                .map(this::convertirResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdenTallerResponse> obtenerPorEstado(
            EstadoOrdenTrabajo estado
    ) {
        return ordenTallerRepository
                .findByEstadoOrderByFechaRecepcionDesc(estado)
                .stream()
                .map(this::convertirResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdenTallerResponse> buscar(
            EstadoOrdenTrabajo estado,
            String buscar
    ) {
        return ordenTallerRepository
                .buscar(
                        estado,
                        buscar == null ? "" : buscar.trim()
                )
                .stream()
                .map(this::convertirResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdenTallerResponse>
    obtenerOrdenesPendientesDePago() {

        List<EstadoOrdenTrabajo> estados =
                Arrays.asList(
                        EstadoOrdenTrabajo.TERMINADA,
                        EstadoOrdenTrabajo.LISTA_PARA_ENTREGA,
                        EstadoOrdenTrabajo.PAGO_PARCIAL
                );

        return ordenTallerRepository
                .findByEstadoIn(estados)
                .stream()
                .map(this::convertirResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdenTallerResponse>
    historialMotocicleta(Long motoId) {

        return ordenTallerRepository
                .findByMotocicletaIdOrderByFechaRecepcionDesc(
                        motoId
                )
                .stream()
                .map(this::convertirResponse)
                .toList();
    }

    @Override
    @Transactional
    public OrdenTallerResponse actualizar(
            Long id,
            ActualizarOrdenTallerRequest request
    ) {

        OrdenTaller orden = buscarOrden(id);
        validarOrdenEditable(orden);

        if (request.getTecnicoId() != null) {
            Usuario tecnico = buscarUsuario(
                    request.getTecnicoId()
            );
            orden.setTecnico(tecnico);
        }

        if (request.getFechaEntregaEstimada() != null) {
            orden.setFechaEntregaEstimada(
                    request.getFechaEntregaEstimada()
            );
        }

        if (request.getKilometraje() != null) {
            orden.setKilometraje(request.getKilometraje());
        }

        if (request.getNivelCombustible() != null) {
            orden.setNivelCombustible(
                    request.getNivelCombustible()
            );
        }

        if (request.getPrioridad() != null) {
            orden.setPrioridad(request.getPrioridad());
        }

        if (
                request.getFallaReportada() != null
                        && !request
                        .getFallaReportada()
                        .isBlank()
        ) {
            orden.setFallaReportada(
                    request.getFallaReportada()
            );
        }

        orden.setDiagnosticoInicial(
                request.getDiagnosticoInicial()
        );

        orden.setObservaciones(
                request.getObservaciones()
        );

        orden.setFechaActualizacion(
                LocalDateTime.now()
        );

        OrdenTaller guardada =
                ordenTallerRepository.save(orden);

        registrarHistorial(
                guardada,
                obtenerUsuarioOpcional(
                        request.getUsuarioId()
                ),
                guardada.getEstado(),
                guardada.getEstado(),
                "EDICION",
                "Datos generales actualizados"
        );

        return convertirResponse(guardada);
    }

    @Override
    @Transactional
    public OrdenTallerResponse cambiarEstado(
            Long id,
            CambiarEstadoOrdenRequest request
    ) {

        OrdenTaller orden = buscarOrden(id);

        EstadoOrdenTrabajo anterior =
                orden.getEstado();

        EstadoOrdenTrabajo nuevo =
                request.getEstado();

        validarTransicion(
                orden,
                anterior,
                nuevo
        );

        orden.setEstado(nuevo);
        orden.setFechaActualizacion(
                LocalDateTime.now()
        );

        if (
                nuevo
                        == EstadoOrdenTrabajo.ENTREGADA
        ) {
            orden.setFechaEntregaReal(
                    LocalDateTime.now()
            );
        }

        OrdenTaller guardada =
                ordenTallerRepository.save(orden);

        registrarHistorial(
                guardada,
                obtenerUsuarioOpcional(
                        request.getUsuarioId()
                ),
                anterior,
                nuevo,
                "CAMBIO_ESTADO",
                request.getObservacion() == null
                        || request
                        .getObservacion()
                        .isBlank()
                        ? "Cambio de estado"
                        : request.getObservacion()
        );

        return convertirResponse(guardada);
    }

    @Override
    @Transactional
    public OrdenTallerResponse asignarTecnico(
            Long id,
            AsignarTecnicoOrdenRequest request
    ) {

        OrdenTaller orden = buscarOrden(id);
        validarOrdenEditable(orden);

        Usuario tecnico =
                buscarUsuario(
                        request.getTecnicoId()
                );

        orden.setTecnico(tecnico);
        orden.setFechaActualizacion(
                LocalDateTime.now()
        );

        OrdenTaller guardada =
                ordenTallerRepository.save(orden);

        registrarHistorial(
                guardada,
                obtenerUsuarioOpcional(
                        request.getUsuarioId()
                ),
                guardada.getEstado(),
                guardada.getEstado(),
                "ASIGNACION_TECNICO",
                "Técnico asignado: "
                        + tecnico.getNombre()
        );

        return convertirResponse(guardada);
    }

    @Override
    @Transactional
    public OrdenTallerResponse agregarServicio(
            Long id,
            OrdenServicioRequest request,
            Long usuarioId
    ) {

        OrdenTaller orden = buscarOrden(id);
        validarOrdenEditable(orden);

        MotoServicios servicio =
                motoServicioRepository
                        .findById(
                                request.getServicioId()
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "No existe el servicio con id "
                                                + request
                                                .getServicioId()
                                )
                        );

        int cantidad =
                request.getCantidad() == null
                        ? 1
                        : request.getCantidad();

        validarCantidad(cantidad);

        BigDecimal precio =
                obtenerPrecioServicio(
                        request,
                        servicio
                );

        BigDecimal subtotal =
                precio
                        .multiply(
                                BigDecimal.valueOf(
                                        cantidad
                                )
                        )
                        .setScale(
                                2,
                                RoundingMode.HALF_UP
                        );

        OrdenTallerServicio detalle =
                OrdenTallerServicio.builder()
                        .ordenTaller(orden)
                        .servicio(servicio)
                        .descripcion(
                                request.getDescripcion()
                        )
                        .cantidad(cantidad)
                        .precioUnitario(precio)
                        .subtotal(subtotal)
                        .build();

        ordenTallerServicioRepository
                .save(detalle);

        recalcularDesdeBase(orden);

        registrarHistorial(
                orden,
                obtenerUsuarioOpcional(usuarioId),
                orden.getEstado(),
                orden.getEstado(),
                "SERVICIO_AGREGADO",
                "Servicio agregado: "
                        + servicio.getNombre()
        );

        return convertirResponse(orden);
    }

    @Override
    @Transactional
    public OrdenTallerResponse eliminarServicio(
            Long id,
            Long detalleId,
            Long usuarioId
    ) {

        OrdenTaller orden = buscarOrden(id);
        validarOrdenEditable(orden);

        OrdenTallerServicio detalle =
                ordenTallerServicioRepository
                        .findById(detalleId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "No existe el detalle de servicio "
                                                + detalleId
                                )
                        );

        validarDetallePerteneceOrden(
                id,
                detalle.getOrdenTaller().getId()
        );

        String nombre =
                detalle.getServicio() != null
                        ? detalle
                        .getServicio()
                        .getNombre()
                        : "";

        ordenTallerServicioRepository
                .delete(detalle);

        ordenTallerServicioRepository.flush();

        recalcularDesdeBase(orden);

        registrarHistorial(
                orden,
                obtenerUsuarioOpcional(usuarioId),
                orden.getEstado(),
                orden.getEstado(),
                "SERVICIO_ELIMINADO",
                "Servicio eliminado: " + nombre
        );

        return convertirResponse(orden);
    }

    @Override
    @Transactional
    public OrdenTallerResponse agregarProducto(
            Long id,
            OrdenProductoRequest request,
            Long usuarioId
    ) {

        OrdenTaller orden = buscarOrden(id);
        validarOrdenEditable(orden);

        Productos producto =
                productoRepository
                        .findById(
                                request.getProductoId()
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "No existe el producto con id "
                                                + request
                                                .getProductoId()
                                )
                        );

        int cantidad =
                request.getCantidad() == null
                        ? 1
                        : request.getCantidad();

        validarCantidad(cantidad);
        validarStock(producto, cantidad);

        BigDecimal precio =
                obtenerPrecioProducto(
                        request,
                        producto
                );

        BigDecimal subtotal =
                precio
                        .multiply(
                                BigDecimal.valueOf(
                                        cantidad
                                )
                        )
                        .setScale(
                                2,
                                RoundingMode.HALF_UP
                        );

        OrdenTallerProducto detalle =
                OrdenTallerProducto.builder()
                        .ordenTaller(orden)
                        .producto(producto)
                        .cantidad(cantidad)
                        .precioUnitario(precio)
                        .subtotal(subtotal)
                        .build();

        /*
         * IMPORTANTE:
         * aquí NO se descuenta stock.
         *
         * La orden de taller presupone/reserva la pieza.
         * El descuento definitivo debe mantenerse
         * en el flujo de venta/POS.
         */
        ordenTallerProductoRepository
                .save(detalle);

        recalcularDesdeBase(orden);

        registrarHistorial(
                orden,
                obtenerUsuarioOpcional(usuarioId),
                orden.getEstado(),
                orden.getEstado(),
                "PRODUCTO_AGREGADO",
                "Refacción agregada: "
                        + producto.getNombre()
        );

        return convertirResponse(orden);
    }

    @Override
    @Transactional
    public OrdenTallerResponse eliminarProducto(
            Long id,
            Long detalleId,
            Long usuarioId
    ) {

        OrdenTaller orden = buscarOrden(id);
        validarOrdenEditable(orden);

        OrdenTallerProducto detalle =
                ordenTallerProductoRepository
                        .findById(detalleId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "No existe el detalle de producto "
                                                + detalleId
                                )
                        );

        validarDetallePerteneceOrden(
                id,
                detalle.getOrdenTaller().getId()
        );

        String nombre =
                detalle.getProducto() != null
                        ? detalle
                        .getProducto()
                        .getNombre()
                        : "";

        ordenTallerProductoRepository
                .delete(detalle);

        ordenTallerProductoRepository.flush();

        recalcularDesdeBase(orden);

        registrarHistorial(
                orden,
                obtenerUsuarioOpcional(usuarioId),
                orden.getEstado(),
                orden.getEstado(),
                "PRODUCTO_ELIMINADO",
                "Refacción eliminada: " + nombre
        );

        return convertirResponse(orden);
    }

    private void agregarServiciosIniciales(
            OrdenTaller orden,
            List<OrdenServicioRequest> servicios
    ) {

        if (servicios == null) {
            return;
        }

        for (
                OrdenServicioRequest request
                : servicios
        ) {

            MotoServicios servicio =
                    motoServicioRepository
                            .findById(
                                    request.getServicioId()
                            )
                            .orElseThrow(() ->
                                    new IllegalArgumentException(
                                            "No existe el servicio con id "
                                                    + request
                                                    .getServicioId()
                                    )
                            );

            int cantidad =
                    request.getCantidad() == null
                            ? 1
                            : request.getCantidad();

            validarCantidad(cantidad);

            BigDecimal precio =
                    obtenerPrecioServicio(
                            request,
                            servicio
                    );

            BigDecimal subtotal =
                    precio.multiply(
                                    BigDecimal.valueOf(
                                            cantidad
                                    )
                            )
                            .setScale(
                                    2,
                                    RoundingMode.HALF_UP
                            );

            OrdenTallerServicio detalle =
                    OrdenTallerServicio.builder()
                            .servicio(servicio)
                            .descripcion(
                                    request.getDescripcion()
                            )
                            .cantidad(cantidad)
                            .precioUnitario(precio)
                            .subtotal(subtotal)
                            .build();

            orden.agregarServicio(detalle);
        }
    }

    private void agregarProductosIniciales(
            OrdenTaller orden,
            List<OrdenProductoRequest> productos
    ) {

        if (productos == null) {
            return;
        }

        for (
                OrdenProductoRequest request
                : productos
        ) {

            Productos producto =
                    productoRepository
                            .findById(
                                    request.getProductoId()
                            )
                            .orElseThrow(() ->
                                    new IllegalArgumentException(
                                            "No existe el producto con id "
                                                    + request
                                                    .getProductoId()
                                    )
                            );

            int cantidad =
                    request.getCantidad() == null
                            ? 1
                            : request.getCantidad();

            validarCantidad(cantidad);
            validarStock(producto, cantidad);

            BigDecimal precio =
                    obtenerPrecioProducto(
                            request,
                            producto
                    );

            BigDecimal subtotal =
                    precio.multiply(
                                    BigDecimal.valueOf(
                                            cantidad
                                    )
                            )
                            .setScale(
                                    2,
                                    RoundingMode.HALF_UP
                            );

            OrdenTallerProducto detalle =
                    OrdenTallerProducto.builder()
                            .producto(producto)
                            .cantidad(cantidad)
                            .precioUnitario(precio)
                            .subtotal(subtotal)
                            .build();

            orden.agregarProducto(detalle);

            // NO descontar stock aquí.
        }
    }

    private BigDecimal obtenerPrecioServicio(
            OrdenServicioRequest request,
            MotoServicios servicio
    ) {

        if (
                request.getPrecioUnitario() != null
        ) {
            return normalizarImporte(
                    request.getPrecioUnitario()
            );
        }

        return normalizarImporte(
                servicio.getPrecioVenta()
        );
    }

    private BigDecimal obtenerPrecioProducto(
            OrdenProductoRequest request,
            Productos producto
    ) {

        if (
                request.getPrecioUnitario() != null
        ) {
            return normalizarImporte(
                    request.getPrecioUnitario()
            );
        }

        return normalizarImporte(
                producto.getPrecioVenta()
        );
    }

    private void calcularTotales(
            OrdenTaller orden
    ) {

        BigDecimal subtotalServicios =
                orden.getServicios()
                        .stream()
                        .map(
                                OrdenTallerServicio
                                        ::getSubtotal
                        )
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );

        BigDecimal subtotalProductos =
                orden.getProductos()
                        .stream()
                        .map(
                                OrdenTallerProducto
                                        ::getSubtotal
                        )
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );

        BigDecimal subtotal =
                subtotalServicios
                        .add(subtotalProductos);

        BigDecimal iva =
                calcularIva(
                        orden.getServicios(),
                        orden.getProductos()
                );

        /*
         * Se conserva la regla actual del proyecto:
         * precioVenta / total son importes finales.
         * IVA queda como desglose informativo.
         */
        BigDecimal total =
                subtotal.setScale(
                        2,
                        RoundingMode.HALF_UP
                );

        BigDecimal anticipo =
                normalizarImporte(
                        orden.getAnticipo()
                );

        BigDecimal saldo =
                total.subtract(anticipo);

        if (saldo.signum() < 0) {
            saldo = BigDecimal.ZERO;
        }

        orden.setSubtotalServicios(
                subtotalServicios.setScale(
                        2,
                        RoundingMode.HALF_UP
                )
        );

        orden.setSubtotalProductos(
                subtotalProductos.setScale(
                        2,
                        RoundingMode.HALF_UP
                )
        );

        orden.setSubtotal(
                subtotal.setScale(
                        2,
                        RoundingMode.HALF_UP
                )
        );

        orden.setIva(iva);
        orden.setTotal(total);
        orden.setAnticipo(anticipo);
        orden.setSaldoPendiente(
                saldo.setScale(
                        2,
                        RoundingMode.HALF_UP
                )
        );
    }

    private void recalcularDesdeBase(
            OrdenTaller orden
    ) {

        List<OrdenTallerServicio> servicios =
                ordenTallerServicioRepository
                        .findByOrdenTallerId(
                                orden.getId()
                        );

        List<OrdenTallerProducto> productos =
                ordenTallerProductoRepository
                        .findByOrdenTallerId(
                                orden.getId()
                        );

        BigDecimal subtotalServicios =
                servicios.stream()
                        .map(
                                OrdenTallerServicio
                                        ::getSubtotal
                        )
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );

        BigDecimal subtotalProductos =
                productos.stream()
                        .map(
                                OrdenTallerProducto
                                        ::getSubtotal
                        )
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );

        BigDecimal subtotal =
                subtotalServicios
                        .add(subtotalProductos);

        BigDecimal iva =
                calcularIva(
                        servicios,
                        productos
                );

        BigDecimal total =
                subtotal.setScale(
                        2,
                        RoundingMode.HALF_UP
                );

        BigDecimal saldo =
                total.subtract(
                        normalizarImporte(
                                orden.getAnticipo()
                        )
                );

        if (saldo.signum() < 0) {
            saldo = BigDecimal.ZERO;
        }

        orden.setSubtotalServicios(
                subtotalServicios.setScale(
                        2,
                        RoundingMode.HALF_UP
                )
        );

        orden.setSubtotalProductos(
                subtotalProductos.setScale(
                        2,
                        RoundingMode.HALF_UP
                )
        );

        orden.setSubtotal(
                subtotal.setScale(
                        2,
                        RoundingMode.HALF_UP
                )
        );

        orden.setIva(iva);
        orden.setTotal(total);
        orden.setSaldoPendiente(
                saldo.setScale(
                        2,
                        RoundingMode.HALF_UP
                )
        );

        orden.setFechaActualizacion(
                LocalDateTime.now()
        );

        ordenTallerRepository.save(orden);
    }

    private BigDecimal calcularIva(
            List<OrdenTallerServicio> servicios,
            List<OrdenTallerProducto> productos
    ) {

        BigDecimal baseIva =
                BigDecimal.ZERO;

        for (
                OrdenTallerServicio detalle
                : servicios
        ) {
            MotoServicios servicio =
                    detalle.getServicio();

            if (
                    servicio != null
                            && Boolean.TRUE.equals(
                            servicio.getAplicaIva()
                    )
            ) {
                baseIva =
                        baseIva.add(
                                detalle.getSubtotal()
                        );
            }
        }

        for (
                OrdenTallerProducto detalle
                : productos
        ) {
            baseIva =
                    baseIva.add(
                            detalle.getSubtotal()
                    );
        }

        return baseIva
                .multiply(PORCENTAJE_IVA)
                .setScale(
                        2,
                        RoundingMode.HALF_UP
                );
    }

    private void validarContenidoOrden(
            OrdenTallerRequest request
    ) {

        boolean tieneServicios =
                request.getServicios() != null
                        && !request
                        .getServicios()
                        .isEmpty();

        boolean tieneProductos =
                request.getProductos() != null
                        && !request
                        .getProductos()
                        .isEmpty();

        if (
                !tieneServicios
                        && !tieneProductos
        ) {
            throw new IllegalArgumentException(
                    "La orden debe contener al menos "
                            + "un servicio o una refacción"
            );
        }
    }

    private void validarPropietarioMoto(
            Clientes cliente,
            Motocicleta motocicleta
    ) {

        if (
                motocicleta.getCliente() == null
                        || !motocicleta
                        .getCliente()
                        .getId()
                        .equals(cliente.getId())
        ) {
            throw new IllegalArgumentException(
                    "La motocicleta seleccionada "
                            + "no pertenece al cliente"
            );
        }
    }

    private void validarStock(
            Productos producto,
            int cantidad
    ) {

        if (
                producto.getStockActual() != null
                        && producto.getStockActual()
                        < cantidad
        ) {
            throw new IllegalArgumentException(
                    "Stock insuficiente para "
                            + producto.getNombre()
                            + ". Disponible: "
                            + producto.getStockActual()
            );
        }
    }

    private void validarCantidad(
            int cantidad
    ) {

        if (cantidad <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad debe ser mayor a cero"
            );
        }
    }

    private void validarAnticipo(
            OrdenTaller orden
    ) {

        if (
                orden.getAnticipo()
                        .compareTo(
                                orden.getTotal()
                        ) > 0
        ) {
            throw new IllegalArgumentException(
                    "El anticipo no puede ser mayor "
                            + "al total de la orden"
            );
        }
    }

    private void validarOrdenEditable(
            OrdenTaller orden
    ) {

        if (
                orden.getEstado()
                        == EstadoOrdenTrabajo.ENTREGADA
                        || orden.getEstado()
                        == EstadoOrdenTrabajo.CANCELADA
                        || orden.getEstado()
                        == EstadoOrdenTrabajo.PAGADA
        ) {
            throw new IllegalArgumentException(
                    "La orden ya no permite modificaciones"
            );
        }
    }

    private void validarTransicion(
            OrdenTaller orden,
            EstadoOrdenTrabajo actual,
            EstadoOrdenTrabajo nuevo
    ) {

        Map<
                EstadoOrdenTrabajo,
                Set<EstadoOrdenTrabajo>
                > transiciones =
                new EnumMap<>(
                        EstadoOrdenTrabajo.class
                );

        transiciones.put(
                EstadoOrdenTrabajo.RECIBIDA,
                EnumSet.of(
                        EstadoOrdenTrabajo.EN_DIAGNOSTICO,
                        EstadoOrdenTrabajo.CANCELADA
                )
        );

        transiciones.put(
                EstadoOrdenTrabajo.EN_DIAGNOSTICO,
                EnumSet.of(
                        EstadoOrdenTrabajo
                                .ESPERANDO_AUTORIZACION,
                        EstadoOrdenTrabajo.AUTORIZADA,
                        EstadoOrdenTrabajo.CANCELADA
                )
        );

        transiciones.put(
                EstadoOrdenTrabajo
                        .ESPERANDO_AUTORIZACION,
                EnumSet.of(
                        EstadoOrdenTrabajo.AUTORIZADA,
                        EstadoOrdenTrabajo.RECHAZADA,
                        EstadoOrdenTrabajo.CANCELADA
                )
        );

        transiciones.put(
                EstadoOrdenTrabajo.RECHAZADA,
                EnumSet.of(
                        EstadoOrdenTrabajo
                                .ESPERANDO_AUTORIZACION,
                        EstadoOrdenTrabajo.CANCELADA
                )
        );

        transiciones.put(
                EstadoOrdenTrabajo.AUTORIZADA,
                EnumSet.of(
                        EstadoOrdenTrabajo.EN_REPARACION,
                        EstadoOrdenTrabajo.CANCELADA
                )
        );

        transiciones.put(
                EstadoOrdenTrabajo.EN_REPARACION,
                EnumSet.of(
                        EstadoOrdenTrabajo
                                .ESPERANDO_REFACCIONES,
                        EstadoOrdenTrabajo.EN_PRUEBAS,
                        EstadoOrdenTrabajo.TERMINADA
                )
        );

        transiciones.put(
                EstadoOrdenTrabajo
                        .ESPERANDO_REFACCIONES,
                EnumSet.of(
                        EstadoOrdenTrabajo.EN_REPARACION,
                        EstadoOrdenTrabajo.CANCELADA
                )
        );

        transiciones.put(
                EstadoOrdenTrabajo.EN_PRUEBAS,
                EnumSet.of(
                        EstadoOrdenTrabajo.EN_REPARACION,
                        EstadoOrdenTrabajo.TERMINADA
                )
        );

        transiciones.put(
                EstadoOrdenTrabajo.TERMINADA,
                EnumSet.of(
                        EstadoOrdenTrabajo
                                .LISTA_PARA_ENTREGA
                )
        );

        transiciones.put(
                EstadoOrdenTrabajo
                        .LISTA_PARA_ENTREGA,
                EnumSet.of(
                        EstadoOrdenTrabajo.PAGO_PARCIAL,
                        EstadoOrdenTrabajo.PAGADA
                )
        );

        transiciones.put(
                EstadoOrdenTrabajo.PAGO_PARCIAL,
                EnumSet.of(
                        EstadoOrdenTrabajo.PAGADA
                )
        );

        transiciones.put(
                EstadoOrdenTrabajo.PAGADA,
                EnumSet.of(
                        EstadoOrdenTrabajo.ENTREGADA
                )
        );

        if (
                !transiciones
                        .getOrDefault(
                                actual,
                                Set.of()
                        )
                        .contains(nuevo)
        ) {
            throw new IllegalArgumentException(
                    "No se permite cambiar la orden de "
                            + actual
                            + " a "
                            + nuevo
            );
        }

        if (
                nuevo
                        == EstadoOrdenTrabajo.PAGADA
                        && orden.getSaldoPendiente()
                        .compareTo(
                                BigDecimal.ZERO
                        ) > 0
        ) {
            throw new IllegalArgumentException(
                    "La orden todavía tiene saldo pendiente"
            );
        }

        if (
                nuevo
                        == EstadoOrdenTrabajo.ENTREGADA
                        && orden.getSaldoPendiente()
                        .compareTo(
                                BigDecimal.ZERO
                        ) > 0
        ) {
            throw new IllegalArgumentException(
                    "No se puede entregar una orden "
                            + "con saldo pendiente"
            );
        }
    }

    private void validarDetallePerteneceOrden(
            Long ordenId,
            Long ordenDetalleId
    ) {

        if (
                !Objects.equals(
                        ordenId,
                        ordenDetalleId
                )
        ) {
            throw new IllegalArgumentException(
                    "El detalle no pertenece a la orden"
            );
        }
    }

    private BigDecimal normalizarImporte(
            BigDecimal valor
    ) {

        return valor == null
                ? BigDecimal.ZERO
                : valor.setScale(
                2,
                RoundingMode.HALF_UP
        );
    }

    private String generarFolio() {

        String fecha =
                LocalDateTime.now()
                        .format(
                                DateTimeFormatter
                                        .ofPattern(
                                                "yyyyMMddHHmmssSSS"
                                        )
                        );

        return "OT-" + fecha;
    }

    private OrdenTaller buscarOrden(
            Long id
    ) {

        return ordenTallerRepository
                .findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "No existe la orden con id "
                                        + id
                        )
                );
    }

    private Usuario buscarUsuario(
            Long id
    ) {

        Usuario usuario =
                usuarioRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "No existe el usuario/técnico "
                                                + id
                                )
                        );

        if (
                !Boolean.TRUE.equals(
                        usuario.getActivo()
                )
        ) {
            throw new IllegalArgumentException(
                    "El usuario/técnico está inactivo"
            );
        }

        return usuario;
    }

    private Usuario obtenerUsuarioOpcional(
            Long id
    ) {

        if (id == null) {
            return null;
        }

        return usuarioRepository
                .findById(id)
                .orElse(null);
    }

    private void registrarHistorial(
            OrdenTaller orden,
            Usuario usuario,
            EstadoOrdenTrabajo anterior,
            EstadoOrdenTrabajo nuevo,
            String tipo,
            String descripcion
    ) {

        historialRepository.save(
                OrdenTallerHistorial.builder()
                        .ordenTaller(orden)
                        .usuario(usuario)
                        .estadoAnterior(anterior)
                        .estadoNuevo(nuevo)
                        .tipo(tipo)
                        .descripcion(descripcion)
                        .fecha(LocalDateTime.now())
                        .build()
        );
    }

    public OrdenTallerResponse
    convertirResponsePublico(
            OrdenTaller orden
    ) {
        return convertirResponse(orden);
    }

    private OrdenTallerResponse convertirResponse(
            OrdenTaller orden
    ) {

        List<
                OrdenTallerResponse.ServicioResponse
                > servicios =
                ordenTallerServicioRepository
                        .findByOrdenTallerId(
                                orden.getId()
                        )
                        .stream()
                        .map(detalle ->
                                OrdenTallerResponse
                                        .ServicioResponse
                                        .builder()
                                        .id(
                                                detalle.getId()
                                        )
                                        .servicioId(
                                                detalle
                                                        .getServicio()
                                                        .getId()
                                        )
                                        .codigo(
                                                detalle
                                                        .getServicio()
                                                        .getCodigo()
                                        )
                                        .servicioNombre(
                                                detalle
                                                        .getServicio()
                                                        .getNombre()
                                        )
                                        .descripcion(
                                                detalle
                                                        .getDescripcion()
                                        )
                                        .cantidad(
                                                detalle
                                                        .getCantidad()
                                        )
                                        .precioUnitario(
                                                detalle
                                                        .getPrecioUnitario()
                                        )
                                        .subtotal(
                                                detalle
                                                        .getSubtotal()
                                        )
                                        .aplicaIva(
                                                detalle
                                                        .getServicio()
                                                        .getAplicaIva()
                                        )
                                        .build()
                        )
                        .toList();

        List<
                OrdenTallerResponse.ProductoResponse
                > productos =
                ordenTallerProductoRepository
                        .findByOrdenTallerId(
                                orden.getId()
                        )
                        .stream()
                        .map(detalle ->
                                OrdenTallerResponse
                                        .ProductoResponse
                                        .builder()
                                        .id(
                                                detalle.getId()
                                        )
                                        .productoId(
                                                detalle
                                                        .getProducto()
                                                        .getId()
                                        )
                                        .codigo(
                                                detalle
                                                        .getProducto()
                                                        .getCodigo()
                                        )
                                        .codigoBarras(
                                                detalle
                                                        .getProducto()
                                                        .getCodigoBarras()
                                        )
                                        .productoNombre(
                                                detalle
                                                        .getProducto()
                                                        .getNombre()
                                        )
                                        .cantidad(
                                                detalle
                                                        .getCantidad()
                                        )
                                        .precioUnitario(
                                                detalle
                                                        .getPrecioUnitario()
                                        )
                                        .subtotal(
                                                detalle
                                                        .getSubtotal()
                                        )
                                        .stockActual(
                                                detalle
                                                        .getProducto()
                                                        .getStockActual()
                                        )
                                        .build()
                        )
                        .toList();

        List<HistorialOrdenResponse> historial =
                historialRepository
                        .findByOrdenTallerIdOrderByFechaDesc(
                                orden.getId()
                        )
                        .stream()
                        .map(h ->
                                HistorialOrdenResponse
                                        .builder()
                                        .id(h.getId())
                                        .usuarioId(
                                                h.getUsuario()
                                                        != null
                                                        ? h
                                                        .getUsuario()
                                                        .getId()
                                                        : null
                                        )
                                        .usuarioNombre(
                                                h.getUsuario()
                                                        != null
                                                        ? h
                                                        .getUsuario()
                                                        .getNombre()
                                                        : "Sistema"
                                        )
                                        .estadoAnterior(
                                                h
                                                        .getEstadoAnterior()
                                        )
                                        .estadoNuevo(
                                                h
                                                        .getEstadoNuevo()
                                        )
                                        .tipo(
                                                h.getTipo()
                                        )
                                        .descripcion(
                                                h.getDescripcion()
                                        )
                                        .fecha(
                                                h.getFecha()
                                        )
                                        .build()
                        )
                        .toList();

        Long ventaId = null;
        String estadoVenta = null;

        Optional<Ventas> venta =
                ventaRepository
                        .findByOrdenTallerId(
                                orden.getId()
                        );

        if (venta.isPresent()) {
            ventaId =
                    venta.get().getId();
            estadoVenta =
                    venta.get()
                            .getEstado()
                            .name();
        }

        return OrdenTallerResponse.builder()
                .id(orden.getId())
                .folio(orden.getFolio())
                .clienteId(
                        orden
                                .getCliente()
                                .getId()
                )
                .clienteNombre(
                        orden
                                .getCliente()
                                .getNombre()
                )
                .clienteTelefono(
                        orden
                                .getCliente()
                                .getTelefono()
                )
                .clienteEmail(
                        orden
                                .getCliente()
                                .getEmail()
                )
                .motoClienteId(
                        orden
                                .getMotocicleta()
                                .getId()
                )
                .motocicleta(
                        obtenerDescripcionMoto(
                                orden.getMotocicleta()
                        )
                )
                .placas(
                        orden
                                .getMotocicleta()
                                .getPlacas()
                )
                .color(
                        orden
                                .getMotocicleta()
                                .getColor()
                )
                .numeroSerie(
                        orden
                                .getMotocicleta()
                                .getNumeroSerie()
                )
                .tecnicoId(
                        orden.getTecnico()
                                != null
                                ? orden
                                .getTecnico()
                                .getId()
                                : null
                )
                .tecnicoNombre(
                        orden.getTecnico()
                                != null
                                ? orden
                                .getTecnico()
                                .getNombre()
                                : null
                )
                .fechaRecepcion(
                        orden.getFechaRecepcion()
                )
                .fechaEntregaEstimada(
                        orden
                                .getFechaEntregaEstimada()
                )
                .fechaEntregaReal(
                        orden.getFechaEntregaReal()
                )
                .kilometraje(
                        orden.getKilometraje()
                )
                .nivelCombustible(
                        orden.getNivelCombustible()
                )
                .prioridad(
                        orden.getPrioridad()
                )
                .estado(
                        orden.getEstado()
                )
                .fallaReportada(
                        orden.getFallaReportada()
                )
                .diagnosticoInicial(
                        orden
                                .getDiagnosticoInicial()
                )
                .observaciones(
                        orden.getObservaciones()
                )
                .subtotalServicios(
                        orden
                                .getSubtotalServicios()
                )
                .subtotalProductos(
                        orden
                                .getSubtotalProductos()
                )
                .subtotal(
                        orden.getSubtotal()
                )
                .iva(
                        orden.getIva()
                )
                .total(
                        orden.getTotal()
                )
                .anticipo(
                        orden.getAnticipo()
                )
                .saldoPendiente(
                        orden.getSaldoPendiente()
                )
                .fechaCreacion(
                        orden.getFechaCreacion()
                )
                .fechaActualizacion(
                        orden
                                .getFechaActualizacion()
                )
                .atrasada(
                        orden
                                .getFechaEntregaEstimada()
                                != null
                                && orden
                                .getFechaEntregaEstimada()
                                .isBefore(
                                        LocalDateTime.now()
                                )
                                && orden.getEstado()
                                != EstadoOrdenTrabajo
                                .ENTREGADA
                                && orden.getEstado()
                                != EstadoOrdenTrabajo
                                .CANCELADA
                )
                .ventaId(ventaId)
                .estadoVenta(estadoVenta)
                .servicios(servicios)
                .productos(productos)
                .historial(historial)
                .build();
    }

    private String obtenerDescripcionMoto(
            Motocicleta moto
    ) {

        if (
                moto == null
                        || moto.getMotoVersion()
                        == null
        ) {
            return "Motocicleta";
        }

        MotoVersiones version =
                moto.getMotoVersion();

        String marca = "";
        String modelo = "";

        if (
                version.getMotoModelo() != null
        ) {
            modelo =
                    version
                            .getMotoModelo()
                            .getNombre();

            if (
                    version
                            .getMotoModelo()
                            .getMarca()
                            != null
            ) {
                marca =
                        version
                                .getMotoModelo()
                                .getMarca()
                                .getNombre();
            }
        }

        String textoVersion =
                version.getVersion() == null
                        ? ""
                        : version.getVersion();

        String anio =
                version.getAnio() == null
                        ? ""
                        : String.valueOf(
                        version.getAnio()
                );

        return String.join(
                        " ",
                        marca,
                        modelo,
                        textoVersion,
                        anio
                )
                .replaceAll(
                        "\\s+",
                        " "
                )
                .trim();
    }
}

package com.posref.pos.service;

import com.posref.pos.dto.OrdenProductoRequest;
import com.posref.pos.dto.OrdenServicioRequest;
import com.posref.pos.dto.OrdenTallerRequest;
import com.posref.pos.dto.OrdenTallerResponse;
import com.posref.pos.model.Motocicleta;
import com.posref.pos.model.Clientes;
import com.posref.pos.model.EstadoOrdenTrabajo;
import com.posref.pos.model.MotoServicios;
import com.posref.pos.model.OrdenTaller;
import com.posref.pos.model.OrdenTallerProducto;
import com.posref.pos.model.OrdenTallerServicio;
import com.posref.pos.model.PrioridadOrdenTaller;
import com.posref.pos.model.Productos;
import com.posref.pos.repository.IClienteMotoRepository;
import com.posref.pos.repository.ClientesRepository;
import com.posref.pos.repository.IMotoServicioRepository;
import com.posref.pos.repository.IOrdenTallerRepository;
import com.posref.pos.repository.ProductosRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrdenTallerService
        implements IOrdenTallerService {

    private static final BigDecimal PORCENTAJE_IVA =
            new BigDecimal("0.16");

    private final IOrdenTallerRepository ordenTallerRepository;
    private final ClientesRepository clienteRepository;
    private final IClienteMotoRepository clienteMotoRepository;
    private final IMotoServicioRepository motoServicioRepository;
    private final ProductosRepository productoRepository;

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

        Motocicleta motoCliente = clienteMotoRepository
                .findById(request.getMotoClienteId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "No existe la motocicleta con id "
                                        + request.getMotoClienteId()
                        )
                );

        validarPropietarioMoto(cliente, motoCliente);

        OrdenTaller orden = OrdenTaller.builder()
                .folio(generarFolio())
                .cliente(cliente)
                .motocicleta(motoCliente)
                .fechaRecepcion(
                        request.getFechaRecepcion() != null
                                ? request.getFechaRecepcion()
                                : LocalDateTime.now()
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
                .estado(
                        request.getEstado() != null
                                ? request.getEstado()
                                : EstadoOrdenTrabajo.RECIBIDA
                )
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
                .fechaCreacion(LocalDateTime.now())
                .fechaActualizacion(LocalDateTime.now())
                .build();

        agregarServicios(orden, request.getServicios());
        agregarProductos(orden, request.getProductos());

        calcularTotales(orden);

        validarAnticipo(orden);

        OrdenTaller guardada =
                ordenTallerRepository.save(orden);

        return convertirResponse(guardada);
    }

    private void agregarServicios(
            OrdenTaller orden,
            List<OrdenServicioRequest> servicios
    ) {

        if (servicios == null) {
            return;
        }

        for (OrdenServicioRequest request : servicios) {

            MotoServicios servicio = motoServicioRepository
                    .findById(request.getServicioId())
                    .orElseThrow(() ->
                            new IllegalArgumentException(
                                    "No existe el servicio con id "
                                            + request.getServicioId()
                            )
                    );

            BigDecimal precio =
                    normalizarImporte(
                            request.getPrecioUnitario()
                    );

            Integer cantidad =
                    request.getCantidad() != null
                            ? request.getCantidad()
                            : 1;

            BigDecimal subtotal = precio.multiply(
                    BigDecimal.valueOf(cantidad)
            );

            OrdenTallerServicio detalle =
                    OrdenTallerServicio.builder()
                            .servicio(servicio)
                            .descripcion(
                                    request.getDescripcion()
                            )
                            .cantidad(cantidad)
                            .precioUnitario(precio)
                            .subtotal(
                                    subtotal.setScale(
                                            2,
                                            RoundingMode.HALF_UP
                                    )
                            )
                            .build();

            orden.agregarServicio(detalle);
        }
    }

    private void agregarProductos(
            OrdenTaller orden,
            List<OrdenProductoRequest> productos
    ) {

        if (productos == null) {
            return;
        }

        for (OrdenProductoRequest request : productos) {

            Productos producto = productoRepository
                    .findById(request.getProductoId())
                    .orElseThrow(() ->
                            new IllegalArgumentException(
                                    "No existe el producto con id "
                                            + request.getProductoId()
                            )
                    );

            Integer cantidad =
                    request.getCantidad() != null
                            ? request.getCantidad()
                            : 1;

            validarStock(producto, cantidad);

            BigDecimal precio =
                    normalizarImporte(
                            request.getPrecioUnitario()
                    );

            BigDecimal subtotal = precio.multiply(
                    BigDecimal.valueOf(cantidad)
            );

            OrdenTallerProducto detalle =
                    OrdenTallerProducto.builder()
                            .producto(producto)
                            .cantidad(cantidad)
                            .precioUnitario(precio)
                            .subtotal(
                                    subtotal.setScale(
                                            2,
                                            RoundingMode.HALF_UP
                                    )
                            )
                            .build();

            orden.agregarProducto(detalle);

            /*
             * Descomenta esta parte si deseas descontar
             * inventario al registrar la orden.
             *
             * Ajusta getStock() y setStock() al nombre real
             * de tu atributo.
             */


            producto.setStockActual(
                    producto.getStockActual() - cantidad
            );

            productoRepository.save(producto);

        }
    }

    private void calcularTotales(OrdenTaller orden) {

        BigDecimal subtotalServicios = orden.getServicios()
                .stream()
                .map(OrdenTallerServicio::getSubtotal)
                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add
                );

        BigDecimal subtotalProductos = orden.getProductos()
                .stream()
                .map(OrdenTallerProducto::getSubtotal)
                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add
                );

        BigDecimal subtotal = subtotalServicios
                .add(subtotalProductos);

        BigDecimal iva = subtotal
                .multiply(PORCENTAJE_IVA)
                .setScale(
                        2,
                        RoundingMode.HALF_UP
                );

        BigDecimal total = subtotal

                .setScale(
                        2,
                        RoundingMode.HALF_UP
                );

        BigDecimal anticipo =
                normalizarImporte(
                        orden.getAnticipo()
                );

        BigDecimal saldoPendiente =
                total.subtract(anticipo);

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
                saldoPendiente.setScale(
                        2,
                        RoundingMode.HALF_UP
                )
        );
    }

    private void validarContenidoOrden(
            OrdenTallerRequest request
    ) {

        boolean tieneServicios =
                request.getServicios() != null
                        && !request.getServicios().isEmpty();

        boolean tieneProductos =
                request.getProductos() != null
                        && !request.getProductos().isEmpty();

        if (!tieneServicios && !tieneProductos) {
            throw new IllegalArgumentException(
                    "La orden debe contener al menos un servicio "
                            + "o una refacción"
            );
        }
    }

    private void validarPropietarioMoto(
            Clientes cliente,
            Motocicleta motoCliente
    ) {

        /*
         * Ajusta getCliente() dependiendo de cómo esté
         * definida tu entidad ClienteMoto.
         */

        if (
                motoCliente.getCliente() == null
                        || !motoCliente
                        .getCliente()
                        .getId()
                        .equals(cliente.getId())
        ) {
            throw new IllegalArgumentException(
                    "La motocicleta seleccionada no pertenece "
                            + "al cliente"
            );
        }
    }

    private void validarStock(
            Productos producto,
            Integer cantidad
    ) {

        /*
         * Ajusta getStock() al nombre real de tu campo:
         *
         * producto.getStock()
         * producto.getStockActual()
         */

        if (
                producto.getStockActual() != null
                        && producto.getStockActual() < cantidad
        ) {
            throw new IllegalArgumentException(
                    "Stock insuficiente para el producto "
                            + producto.getNombre()
                            + ". Disponible: "
                            + producto.getStockActual()
            );
        }
    }

    private void validarAnticipo(OrdenTaller orden) {

        if (
                orden.getAnticipo()
                        .compareTo(orden.getTotal()) > 0
        ) {
            throw new IllegalArgumentException(
                    "El anticipo no puede ser mayor al total "
                            + "de la orden"
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

        String fecha = LocalDateTime.now().format(
                DateTimeFormatter.ofPattern(
                        "yyyyMMddHHmmssSSS"
                )
        );

        return "OT-" + fecha;
    }

    @Override
    @Transactional(readOnly = true)
    public OrdenTallerResponse obtenerPorId(Long id) {

        OrdenTaller orden = ordenTallerRepository
                .findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "No existe la orden con id " + id
                        )
                );

        return convertirResponse(orden);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdenTallerResponse> obtenerTodas() {

        return ordenTallerRepository
                .findAllByOrderByFechaRecepcionDesc()
                .stream()
                .map(this::convertirResponse)
                .collect(Collectors.toList());
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
                .collect(Collectors.toList());
    }

    private OrdenTallerResponse convertirResponse(
            OrdenTaller orden
    ) {

        List<OrdenTallerResponse.ServicioResponse>
                servicios = orden.getServicios()
                .stream()
                .map(detalle ->
                        OrdenTallerResponse
                                .ServicioResponse
                                .builder()
                                .id(detalle.getId())
                                .servicioId(
                                        detalle
                                                .getServicio()
                                                .getId()
                                )
                                .servicioNombre(
                                        detalle
                                                .getServicio()
                                                .getNombre()
                                )
                                .descripcion(
                                        detalle.getDescripcion()
                                )
                                .cantidad(
                                        detalle.getCantidad()
                                )
                                .precioUnitario(
                                        detalle.getPrecioUnitario()
                                )
                                .subtotal(
                                        detalle.getSubtotal()
                                )
                                .build()
                )
                .collect(Collectors.toList());

        List<OrdenTallerResponse.ProductoResponse>
                productos = orden.getProductos()
                .stream()
                .map(detalle ->
                        OrdenTallerResponse
                                .ProductoResponse
                                .builder()
                                .id(detalle.getId())
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
                                .productoNombre(
                                        detalle
                                                .getProducto()
                                                .getNombre()
                                )
                                .cantidad(
                                        detalle.getCantidad()
                                )
                                .precioUnitario(
                                        detalle.getPrecioUnitario()
                                )
                                .subtotal(
                                        detalle.getSubtotal()
                                )
                                .build()
                )
                .collect(Collectors.toList());

        return OrdenTallerResponse.builder()
                .id(orden.getId())
                .folio(orden.getFolio())
                .clienteId(
                        orden.getCliente().getId()
                )
                .clienteNombre(
                        obtenerNombreCliente(
                                orden.getCliente()
                        )
                )
                .motoClienteId(
                        orden.getMotocicleta().getId()
                )
                .motocicleta(
                        obtenerDescripcionMoto(
                                orden.getMotocicleta()
                        )
                )
                .fechaRecepcion(
                        orden.getFechaRecepcion()
                )
                .fechaEntregaEstimada(
                        orden.getFechaEntregaEstimada()
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
                        orden.getDiagnosticoInicial()
                )
                .observaciones(
                        orden.getObservaciones()
                )
                .subtotalServicios(
                        orden.getSubtotalServicios()
                )
                .subtotalProductos(
                        orden.getSubtotalProductos()
                )
                .subtotal(orden.getSubtotal())
                .iva(orden.getIva())
                .total(orden.getTotal())
                .anticipo(orden.getAnticipo())
                .saldoPendiente(
                        orden.getSaldoPendiente()
                )
                .servicios(servicios)
                .productos(productos)
                .build();
    }

    private String obtenerNombreCliente(
            Clientes cliente
    ) {

        /*
         * Ajusta este método a los atributos reales
         * de tu entidad Clientes.
         */

        if (cliente.getNombre() == null) {
            return "";
        }

        return cliente.getNombre();
    }

    private String obtenerDescripcionMoto(
            Motocicleta moto
    ) {

        /*
         * Ajusta este método según la estructura real
         * de ClienteMoto.
         */
        String modelo = "";
        if(moto.getMotoVersion() == null){
            return "Motocicleta";
        }
        modelo = String.valueOf(moto.getMotoVersion());

       /* if (moto.getMotoModelo() == null) {
            return "Motocicleta";
        }*/

        String marca = "";

        /*if (moto.getModelo().getMarca() != null) {
            marca = moto
                    .getModelo()
                    .getMarca()
                    .getNombre();
        }*/

        /*String modelo = moto
                .getModelo()
                .getNombre();*/

        /*String year = moto.getYear() != null
                ? String.valueOf(moto.getYear())
                : "";*/

        /*return String.join(
                " ",
                marca,
                modelo,
                year
        ).trim();*/
        return modelo;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdenTallerResponse> obtenerOrdenesPendientesDePago() {

        List<EstadoOrdenTrabajo> estados = Arrays.asList(
                EstadoOrdenTrabajo.TERMINADA,
                EstadoOrdenTrabajo.ENTREGADA,
                EstadoOrdenTrabajo.PAGO_PARCIAL,
                EstadoOrdenTrabajo.RECIBIDA
        );

        return ordenTallerRepository
                .findByEstadoIn(estados)
                .stream()
                .map(this::convertirResponse)
                .collect(Collectors.toList());
    }
}
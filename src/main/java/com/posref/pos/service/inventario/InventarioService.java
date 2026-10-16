package com.posref.pos.service.inventario;

import com.posref.pos.dto.inventario.InventarioMovimientoResponse;
import com.posref.pos.dto.inventario.InventarioProductoResponse;
import com.posref.pos.dto.inventario.InventarioResumenResponse;
import com.posref.pos.dto.inventario.MovimientoInventarioRequest;
import com.posref.pos.model.Productos;
import com.posref.pos.model.inventario.InventarioMovimiento;
import com.posref.pos.model.inventario.TipoMovimientoInventario;
import com.posref.pos.repository.inventario.InventarioMovimientoRepository;
import com.posref.pos.repository.inventario.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.posref.pos.model.inventario.TipoMovimientoInventario.ENTRADA;
import static com.posref.pos.model.inventario.TipoMovimientoInventario.SALIDA;


@Service
public class InventarioService implements IInventarioService{

    private final ProductoRepository productoRepository;
    private final InventarioMovimientoRepository movimientoRepository;

    public InventarioService(
            ProductoRepository productoRepository,
            InventarioMovimientoRepository movimientoRepository
    ) {
        this.productoRepository = productoRepository;
        this.movimientoRepository = movimientoRepository;
    }

    @Override
    public List<InventarioProductoResponse> obtenerInventario() {

        return productoRepository
                .findByActivoTrue()
                .stream()
                .map(this::convertirProducto)
                .collect(Collectors.toList());
    }

    @Override
    public InventarioProductoResponse obtenerProducto(Long productoId) {

        Productos producto = productoRepository
                .findById(productoId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Producto no encontrado: " + productoId
                        )
                );

        return convertirProducto(producto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventarioMovimientoResponse> obtenerMovimientos(
            Long productoId) {

        return movimientoRepository
                .findByProductoIdOrderByFechaDesc(productoId)
                .stream()
                .map(this::convertirMovimientoResponse)
                .collect(Collectors.toList());
    }


    @Override
    @Transactional
    public InventarioMovimientoResponse registrarMovimiento(
            MovimientoInventarioRequest request) {

        // 1. VALIDAR REQUEST
        if (request == null
                || request.getProductoId() == null
                || request.getTipo() == null
                || request.getCantidad() == null) {
            throw new IllegalArgumentException(
                    "Producto, tipo y cantidad son obligatorios"
            );
        }

        // 2. NO PERMITIR VENTAS MANUALES
        if (request.getTipo() == TipoMovimientoInventario.VENTA) {
            throw new IllegalArgumentException(
                    "Los movimientos de VENTA deben registrarse desde Punto de Venta"
            );
        }

        BigDecimal cantidad = request.getCantidad();

        if (cantidad.signum() < 0) {
            throw new IllegalArgumentException(
                    "La cantidad no puede ser negativa"
            );
        }

        if (cantidad.stripTrailingZeros().scale() > 0) {
            throw new IllegalArgumentException(
                    "La cantidad debe ser un número entero"
            );
        }

        // 3. OBTENER PRODUCTO
        Productos producto = productoRepository
                .findById(request.getProductoId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Producto no encontrado con id: "
                                        + request.getProductoId()
                        )
                );

        BigDecimal stockAnterior =
                producto.getStockActual() != null
                        ? BigDecimal.valueOf(producto.getStockActual())
                        : BigDecimal.ZERO;

        BigDecimal stockNuevo;

        // 4. CALCULAR MOVIMIENTO
        switch (request.getTipo()) {

            // INCREMENTAR STOCK
            case ENTRADA:
            case AJUSTE_ENTRADA:
            case COMPRA:
            case DEVOLUCION_CLIENTE:

                validarCantidadPositiva(cantidad);

                stockNuevo = stockAnterior.add(cantidad);
                break;

            // DISMINUIR STOCK
            case SALIDA:
            case AJUSTE_SALIDA:
            case TALLER:
            case DEVOLUCION_PROVEEDOR:

                validarCantidadPositiva(cantidad);

                if (stockAnterior.compareTo(cantidad) < 0) {
                    throw new IllegalArgumentException(
                            "Stock insuficiente para "
                                    + request.getTipo()
                                    + ". Disponible: " + stockAnterior
                                    + ", solicitado: " + cantidad
                    );
                }

                stockNuevo = stockAnterior.subtract(cantidad);
                break;

            // INVENTARIO INICIAL
            case INVENTARIO_INICIAL:

                if (producto.getStockActual() != null) {
                    throw new IllegalArgumentException(
                            "El producto ya tiene inventario inicial"
                    );
                }

                stockNuevo = cantidad;
                break;

            default:
                throw new IllegalArgumentException(
                        "Tipo de movimiento no permitido: "
                                + request.getTipo()
                );
        }

        // 5. ACTUALIZAR STOCK
        int stockFinal = stockNuevo.intValueExact();

        producto.setStockActual(stockFinal);
        productoRepository.save(producto);

        // 6. REGISTRAR HISTORIAL
        InventarioMovimiento movimiento =
                new InventarioMovimiento();

        movimiento.setProducto(producto);
        movimiento.setTipo(request.getTipo());
        movimiento.setCantidad(cantidad);
        movimiento.setStockAnterior(stockAnterior);
        movimiento.setStockNuevo(stockNuevo);
        movimiento.setMotivo(request.getMotivo());
        movimiento.setReferenciaTipo(request.getReferenciaTipo());
        movimiento.setFecha(LocalDateTime.now());

        InventarioMovimiento movimientoGuardado =
                movimientoRepository.save(movimiento);

        return convertirMovimientoResponse(movimientoGuardado);
    }

    private void validarCantidadPositiva(BigDecimal cantidad) {
        if (cantidad.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad debe ser mayor a cero"
            );
        }
    }


    @Override
    public InventarioResumenResponse obtenerResumen() {

        List<Productos> productos =
                productoRepository.findByActivoTrue();

        long total = productos.size();

        long bajos = productos
                .stream()
                .filter(p -> {

                    BigDecimal actual =
                            p.getStockActual() == null
                                    ? BigDecimal.ZERO
                                    : BigDecimal.valueOf(p.getStockActual());

                    BigDecimal minimo =
                            p.getStockMinimo() == null
                                    ? BigDecimal.ZERO
                                    : BigDecimal.valueOf(p.getStockMinimo());

                    return actual.compareTo(BigDecimal.ZERO) > 0
                            && actual.compareTo(minimo) <= 0;
                })
                .count();

        long agotados = productos
                .stream()
                .filter(p -> {

                    BigDecimal actual =
                            p.getStockActual() == null
                                    ? BigDecimal.ZERO
                                    : BigDecimal.valueOf(p.getStockActual());

                    return actual.compareTo(BigDecimal.ZERO) <= 0;
                })
                .count();

        BigDecimal valor = productos
                .stream()
                .map(p -> {

                    BigDecimal stock =
                            p.getStockActual() == null
                                    ? BigDecimal.ZERO
                                    : BigDecimal.valueOf(p.getStockActual());

                    BigDecimal costo =
                            p.getPrecioCompra() == null
                                    ? BigDecimal.ZERO
                                    : p.getPrecioCompra();

                    return stock.multiply(costo);
                })
                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add
                );

        InventarioResumenResponse response =
                new InventarioResumenResponse();

        response.setTotalProductos(total);
        response.setProductosStockBajo(bajos);
        response.setProductosAgotados(agotados);
        response.setValorInventario(valor);

        return response;
    }

    private InventarioProductoResponse convertirProducto(
            Productos producto
    ) {

        InventarioProductoResponse dto =
                new InventarioProductoResponse();

        dto.setProductoId(producto.getId());

        dto.setCodigo(producto.getCodigo());

        dto.setCodigoBarras(
                producto.getCodigoBarras()
        );

        dto.setNombre(producto.getNombre());

        dto.setDescripcion(producto.getDescripcion());

        dto.setPrecioCompra(
                producto.getPrecioCompra()
        );

        dto.setPrecioVenta(
                producto.getPrecioVenta()
        );

        dto.setStockActual(
                BigDecimal.valueOf(producto.getStockActual())
        );

        dto.setStockMinimo(
                BigDecimal.valueOf(producto.getStockMinimo())
        );

        dto.setUnidadMedida(
                producto.getUnidadMedida()
        );

        dto.setEstado(
                calcularEstado(producto)
        );

        return dto;
    }

    private String calcularEstado(Productos producto) {

        BigDecimal actual =
                producto.getStockActual() == null
                        ? BigDecimal.ZERO
                        : BigDecimal.valueOf(producto.getStockActual());

        BigDecimal minimo =
                producto.getStockMinimo() == null
                        ? BigDecimal.ZERO
                        : BigDecimal.valueOf(producto.getStockMinimo());

        if (actual.compareTo(BigDecimal.ZERO) <= 0) {
            return "AGOTADO";
        }

        if (actual.compareTo(minimo) <= 0) {
            return "BAJO";
        }

        return "NORMAL";
    }

    private boolean esEntrada(
            TipoMovimientoInventario tipo
    ) {

        return tipo == ENTRADA
                || tipo == TipoMovimientoInventario.COMPRA
                || tipo == TipoMovimientoInventario.AJUSTE_ENTRADA
                || tipo == TipoMovimientoInventario.DEVOLUCION_CLIENTE
                || tipo == TipoMovimientoInventario.INVENTARIO_INICIAL;
    }

    private void validarRequest(
            MovimientoInventarioRequest request
    ) {

        if (request.getProductoId() == null) {
            throw new IllegalArgumentException(
                    "El producto es obligatorio"
            );
        }

        if (request.getTipo() == null) {
            throw new IllegalArgumentException(
                    "El tipo de movimiento es obligatorio"
            );
        }

        if (request.getCantidad() == null
                || request.getCantidad()
                .compareTo(BigDecimal.ZERO) <= 0) {

            throw new IllegalArgumentException(
                    "La cantidad debe ser mayor a cero"
            );
        }
    }

    private InventarioMovimientoResponse convertirMovimientoResponse(
            InventarioMovimiento movimiento) {

        InventarioMovimientoResponse response =
                new InventarioMovimientoResponse();

        response.setId(movimiento.getId());

        if (movimiento.getProducto() != null) {

            response.setProductoId(
                    movimiento.getProducto().getId()
            );

            response.setProductoCodigo(
                    movimiento.getProducto().getCodigo()
            );

            response.setProductoNombre(
                    movimiento.getProducto().getNombre()
            );
        }

        if (movimiento.getTipo() != null) {
            response.setTipoMovimiento(
                    movimiento.getTipo().name()
            );
        }

        response.setCantidad(
                movimiento.getCantidad()
        );

        response.setStockAnterior(
                movimiento.getStockAnterior()
        );

        response.setStockNuevo(
                movimiento.getStockNuevo()
        );

        response.setMotivo(
                movimiento.getMotivo()
        );

        response.setReferencia(
                movimiento.getReferenciaTipo()
        );

        response.setFechaMovimiento(
                movimiento.getFecha()
        );

        return response;
    }
}

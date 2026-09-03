package com.posref.pos.mapper;

import com.posref.pos.dto.*;
import com.posref.pos.model.*;
import org.hibernate.mapping.List;

import java.util.stream.Collectors;

public class Mapper {

    public static CategoriasDTO toDTO(Categorias c){
        if (c == null) return null;

        return CategoriasDTO.builder()
                .id(c.getId())
                .nombre(c.getNombre())
                .descripcion(c.getDescripcion())
                .activo(c.isActivo())
                .build();
    }

    public static ProveedoresDTO toDTO(Proveedores p){
        if (p == null) return null;

        return ProveedoresDTO.builder()
                .id(p.getId())
                .nombre(p.getNombre())
                .telefono(p.getTelefono())
                .email(p.getEmail())
                .direccion(p.getDireccion())
                .contacto(p.getContacto())
                .activo(p.isActivo())
                .build();
    }

    public static MarcasDTO toDTO(Marcas m){
        if(m==null) return null;

        return MarcasDTO.builder()
                .id(m.getId())
                .nombre(m.getNombre())
                .activo(m.isActivo())
                .build();
    }

    public static MotoMarcasDTO toDTO(MotoMarcas m){
        if(m==null)return null;

        return MotoMarcasDTO.builder()
                .id(m.getId())
                .nombre(m.getNombre())
                .activo(m.getActivo())
                .build();
    }

    public static ProductosDTO toDTO(Productos p){
        if(p==null) return null;

        return ProductosDTO.builder()
                .id(p.getId())
                .codigo(p.getCodigo())
                .codigoBarras(p.getCodigoBarras())
                .nombre(p.getNombre())
                .descripcion(p.getDescripcion())
                .categoriaId(p.getCategoria().getId())
                .marcaId(p.getMarca().getId())
                .proveedorId(p.getProveedor().getId())
                .precioCompra(p.getPrecioCompra())
                .precioVenta(p.getPrecioVenta())
                .stockActual(p.getStockActual())
                .stockMinimo(p.getStockMinimo())
                .unidadMedida(p.getUnidadMedida())
                .activo(p.isActivo())
                .fechaCreacion(p.getFechaCreacion())
                .compatibilidadUniversal(p.getCompatibilidadUniversal())
                .build();

    }

    public static MotoModelosDTO toDTO(MotoModelos m){
        if(m==null) return null;

        return MotoModelosDTO.builder()
                .id(m.getId())
                .motoMarcaId(m.getMarca().getId())
                .nombre(m.getNombre())
                .activo(m.getActivo())
                .build();
    }

    public static MotoVersionesDTO toDTO(MotoVersiones m){
        if(m==null) return null;

        return MotoVersionesDTO.builder()
                .id(m.getId())
                .motoModeloId(m.getMotoModelo().getId())
                .anio(m.getAnio())
                .cilindraje(m.getCilindraje())
                .version(m.getVersion())
                .activo(m.getActivo())
                .build();
    }

    public static ProductoCompatibilidadMotoDTO toDTO(ProductoCompatibilidadMoto p){

        if(p==null)return null;

        return ProductoCompatibilidadMotoDTO.builder()
                .id(p.getId())
                .productoId(p.getProducto().getId())
                .motoVersionId(p.getProducto().getId())
                .observaciones(p.getObservaciones())
                .activo(p.getActivo())
                .build();

    }

    public static ClientesDTO toDTO(Clientes c){
        if(c==null) return null;

        return ClientesDTO.builder()
                .id(c.getId())
                .nombre(c.getNombre())
                .telefono(c.getTelefono())
                .email(c.getEmail())
                .direccion(c.getDireccion())
                .rfc(c.getRfc())
                .activo(c.isActivo())
                .fechaCreacion(c.getFechaCreacion())
                .build();
    }

    public static MotoServiciosDTO toDTO(MotoServicios s){
        if(s==null) return null;

        return MotoServiciosDTO.builder()
                .id(s.getId())
                .activo(s.getActivo())
                .codigo(s.getCodigo())
                .nombre(s.getNombre())
                .precioVenta(s.getPrecioVenta())
                .aplicaIva(s.getAplicaIva())
                .duracionEstimadaMinutos(s.getDuracionEstimadaMinutos())
                .descripcion(s.getDescripcion())
                .build();
    }

    public static VentaResponse convertirVentaResponse(Ventas venta) {

        java.util.List<DetalleResponse> items = venta.getDetalles()
                .stream()
                .map(detalle -> DetalleResponse.builder()
                        .id(detalle.getId())
                        .referenciaId(detalle.getReferenciaId())
                        .productoNombre(detalle.getDescripcion())
                        .tipo(String.valueOf(detalle.getTipo()))
                        .cantidad(detalle.getCantidad())
                        .precioUnitario(detalle.getPrecioUnitario())
                        .subtotal(detalle.getSubtotal())
                        .build())
                .collect(Collectors.toList());

        java.util.List<PagoResponse> pagos = venta.getPagos()
                .stream()
                .map(pago -> PagoResponse.builder()
                        .id(pago.getId())
                        .metodoPago(String.valueOf(pago.getMetodo()))
                        .monto(pago.getMonto())
                        .fecha(pago.getFecha())
                        .ventaId(pago.getVenta().getId())
                        .build())
                .collect(Collectors.toList());

        return VentaResponse.builder()
                .id(venta.getId())
                .fecha(venta.getFecha())
                .estado(String.valueOf(venta.getEstado()))
                .clienteId(
                        venta.getCliente() != null
                                ? venta.getCliente().getId()
                                : null
                )
                .clienteNombre(
                        venta.getCliente() != null
                                ? venta.getCliente().getNombre()
                                : null
                )
                .subtotal(venta.getSubtotal())
                .iva(venta.getIva())
                .total(venta.getTotal())
                .totalPagado(venta.getTotalPagado())
                .saldoPendiente(venta.getSaldoPendiente())
                .items(items)
                .pagos(pagos)
                .build();
    }

    private VentaResponse convertirAResponse(Ventas venta) {

        return VentaResponse.builder()
                .id(venta.getId())
                .fecha(venta.getFecha())

                .clienteId(
                        venta.getCliente() != null
                                ? venta.getCliente().getId()
                                : null
                )

                .clienteNombre(
                        venta.getCliente() != null
                                ? venta.getCliente().getNombre()
                                : null
                )

                .subtotal(venta.getSubtotal())
                .iva(venta.getIva())
                .total(venta.getTotal())
                .totalPagado(venta.getTotalPagado())
                .saldoPendiente(venta.getSaldoPendiente())

                .estado(
                        venta.getEstado() != null
                                ? venta.getEstado().name()
                                : null
                )
                .build();
    }


}

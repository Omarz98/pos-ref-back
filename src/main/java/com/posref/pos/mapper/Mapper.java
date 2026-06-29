package com.posref.pos.mapper;

import com.posref.pos.dto.*;
import com.posref.pos.model.*;

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
                .activo(m.isActivo())
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
                .stock(p.getStock())
                .stockMinimo(p.getStockMinimo())
                .unidadMedida(p.getUnidadMedida())
                .activo(p.isActivo())
                .fechaCreacion(p.getFechaCreacion())
                .build();

    }

    public static MotoModelosDTO toDTO(MotoModelos m){
        if(m==null) return null;

        return MotoModelosDTO.builder()
                .id(m.getId())
                .motoMarcaId(m.getMotoMarca().getId())
                .nombre(m.getNombre())
                .activo(m.isActivo())
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


}

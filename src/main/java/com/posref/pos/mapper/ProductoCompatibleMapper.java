package com.posref.pos.mapper;

import com.posref.pos.dto.ProductoCompatibleResponse;
import com.posref.pos.model.Productos;
import org.springframework.stereotype.Component;

@Component
public class ProductoCompatibleMapper {

    public ProductoCompatibleResponse toResponse(Productos producto) {

        return ProductoCompatibleResponse.builder()
                .id(producto.getId())
                .codigo(producto.getCodigo())
                .codigoBarras(producto.getCodigoBarras())
                .nombre(producto.getNombre())
                .descripcion(producto.getDescripcion())
                .precioCompra(producto.getPrecioCompra())
                .precioVenta(producto.getPrecioVenta())
                .stockActual(producto.getStockActual())
                .stockMinimo(producto.getStockMinimo())
                .unidadMedida(producto.getUnidadMedida())
                .compatibilidadUniversal(
                        producto.getCompatibilidadUniversal()
                )
                .build();
    }

}

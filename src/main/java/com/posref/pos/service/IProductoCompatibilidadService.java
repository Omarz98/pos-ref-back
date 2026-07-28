package com.posref.pos.service;

import com.posref.pos.dto.ProductoCompatibleResponse;

import java.util.List;

public interface IProductoCompatibilidadService {

    List<ProductoCompatibleResponse>
    obtenerProductosCompatibles(Long motoVersionId);

    boolean esCompatible(
            Long productoId,
            Long motoVersionId
    );

}

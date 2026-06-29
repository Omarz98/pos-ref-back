package com.posref.pos.service;

import com.posref.pos.dto.MarcasDTO;
import com.posref.pos.dto.ProductoCompatibilidadMotoDTO;

import java.util.List;

public interface IProductoCompatibilidadMotoService {
    List<ProductoCompatibilidadMotoDTO> traerCompatibilidades();
    ProductoCompatibilidadMotoDTO crearCompatibilidad(ProductoCompatibilidadMotoDTO compatibilidadDto);
    ProductoCompatibilidadMotoDTO actualizarConpatibilidad(Long id, ProductoCompatibilidadMotoDTO compatibilidadDto);
    void eliminarCompatibilidad(Long id);
}

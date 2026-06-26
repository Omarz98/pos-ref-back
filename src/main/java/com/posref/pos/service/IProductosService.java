package com.posref.pos.service;

import com.posref.pos.dto.ProductosDTO;
import com.posref.pos.model.Productos;

import java.util.List;

public interface IProductosService {

    List<ProductosDTO> traerProductos();
    ProductosDTO crearProducto(ProductosDTO productoDto);
    ProductosDTO actualizarProducto(ProductosDTO productoDto);
    void eliminarProducto(Long id);

}

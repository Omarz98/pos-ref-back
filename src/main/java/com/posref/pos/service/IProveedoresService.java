package com.posref.pos.service;

import com.posref.pos.dto.ProductosDTO;
import com.posref.pos.dto.ProveedoresDTO;

import java.util.List;

public interface IProveedoresService {
    List<ProveedoresDTO> traerProveedores();
    ProveedoresDTO crearProveedor(ProveedoresDTO proveedorDto);
    ProveedoresDTO actualizarProveedor(Long id, ProveedoresDTO proveedorDto);
    void eliminarProveedor(Long id);
}

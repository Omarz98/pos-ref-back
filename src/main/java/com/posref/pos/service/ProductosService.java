package com.posref.pos.service;

import com.posref.pos.dto.ProductosDTO;
import com.posref.pos.exception.NotFoundException;
import com.posref.pos.mapper.Mapper;
import com.posref.pos.model.*;
import com.posref.pos.repository.CategoriasRepository;
import com.posref.pos.repository.MarcasRepository;
import com.posref.pos.repository.ProductosRepository;
import com.posref.pos.repository.ProveedoresRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ProductosService implements IProductosService{

    @Autowired
    private ProductosRepository productoRepo;
    @Autowired
    private MarcasRepository marcaRepo;
    @Autowired
    private CategoriasRepository categoriatoRepo;
    @Autowired
    private ProveedoresRepository proveedorRepo;

    @Override
    public List<ProductosDTO> traerProductos() {
        return productoRepo.findAll().stream().map(Mapper::toDTO).toList();
    }

    @Override
    public ProductosDTO crearProducto(ProductosDTO productoDto) {

        if (productoDto.getCategoriaId() == null) throw new RuntimeException("Debe indicar la categoria");
        if (productoDto.getMarcaId() == null) throw new RuntimeException("Debe indicar la marca");
        if (productoDto.getProveedorId()== null) throw new RuntimeException("Debe indicar el proveedor");

        Categorias categoria = categoriatoRepo.findById(productoDto.getCategoriaId()).orElseThrow(() -> new NotFoundException("Categoria no encontrada"));
        if (categoria == null) {
            throw new NotFoundException("Categoria no encontrada");
        }
        Marcas marca = marcaRepo.findById(productoDto.getMarcaId()).orElseThrow(() -> new NotFoundException("marca no encontrada"));

        if (marca == null) {
            throw new NotFoundException("Marca no encontrada");
        }
        Proveedores proveedor = proveedorRepo.findById(productoDto.getProveedorId()).orElseThrow(() -> new NotFoundException("proveedor no encontrado"));

        if (proveedor == null) {
            throw new NotFoundException("Proveedor no encontrado");
        }

        Productos producto = Productos.builder()
                .id(productoDto.getId())
                .codigo(productoDto.getCodigo())
                .codigoBarras(productoDto.getCodigoBarras())
                .nombre(productoDto.getNombre())
                .descripcion(productoDto.getDescripcion())
                .categoria(categoria)
                .marca(marca)
                .proveedor(proveedor)
                .precioCompra(productoDto.getPrecioCompra())
                .precioVenta(productoDto.getPrecioVenta())
                .stock(productoDto.getStock())
                .stockMinimo(productoDto.getStockMinimo())
                .unidadMedida(productoDto.getUnidadMedida())
                .activo(true)
                .fechaCreacion(productoDto.getFechaCreacion())
                .build();

        return Mapper.toDTO(productoRepo.save(producto));
    }

    @Override
    public ProductosDTO actualizarProducto(Long id, ProductosDTO productoDto) {
        Productos p = productoRepo.findById(id).orElseThrow(() -> new NotFoundException("Producto no encontrado"));
        if (p == null) throw new RuntimeException("Producto no encontrada");

        p.setId(productoDto.getId());
        p.setCodigo(productoDto.getCodigo());
        p.setCodigoBarras(productoDto.getCodigoBarras());
        p.setNombre(productoDto.getNombre());
        p.setDescripcion(productoDto.getDescripcion());

        if(productoDto.getCategoriaId() != null){
            Categorias categoria = categoriatoRepo.findById(productoDto.getCategoriaId()).orElseThrow(() -> new NotFoundException("Categoria no encontrada"));
            if (categoria == null) throw new NotFoundException("Categoria  no encontrada");
            p.setCategoria(categoria);
        }

        if(productoDto.getMarcaId() != null){
            Marcas marca = marcaRepo.findById(productoDto.getMarcaId()).orElseThrow(() -> new NotFoundException("Marca no encontrada"));
            if (marca == null) throw new NotFoundException("marca  no encontrada");
            p.setMarca(marca);
        }

        if(productoDto.getProveedorId() != null){
            Proveedores proveedor = proveedorRepo.findById(productoDto.getProveedorId()).orElseThrow(() -> new NotFoundException("Proveedor no encontrado"));
            if (proveedor == null) throw new NotFoundException("Proveedor  no encontrada");
            p.setProveedor(proveedor);
        }

        p.setPrecioCompra(productoDto.getPrecioCompra());
        p.setPrecioVenta(productoDto.getPrecioCompra());
        p.setStock(productoDto.getStock());
        p.setStockMinimo(productoDto.getStockMinimo());
        p.setUnidadMedida(productoDto.getUnidadMedida());
        p.setActivo(true);
        p.setFechaCreacion(productoDto.getFechaCreacion());

        productoRepo.save(p);

        ProductosDTO producto = Mapper.toDTO(p);

        return producto;

    }

    @Override
    public void eliminarProducto(Long id) {
        Productos p = productoRepo.findById(id).orElseThrow(() -> new NotFoundException("Producto no encontrado"));
        if (p == null) throw new RuntimeException("Producto no encontrada");
        productoRepo.delete(p);
    }
}

package com.posref.pos.controller;

import com.posref.pos.dto.ProductosDTO;
import com.posref.pos.service.IProductosService;
import com.posref.pos.service.ProductosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "http://localhost:5173") // Para conectar con React
public class ProductosController {

    @Autowired
    private IProductosService productosService;

    @PreAuthorize("hasAuthority('PRODUCTO_VER')")
    @GetMapping
    public ResponseEntity<List<ProductosDTO>> traerMotoModelos(){
        return ResponseEntity.ok(productosService.traerProductos());
    }

    @PreAuthorize("hasAuthority('PRODUCTO_CREAR')")
    @PostMapping
    public ResponseEntity<ProductosDTO> crearProducto(@RequestBody ProductosDTO dto){
        ProductosDTO creado = productosService.crearProducto(dto);

        return ResponseEntity.created(URI.create("/api/productos"+creado.getId())).body(creado);
    }

    @PreAuthorize("hasAuthority('PRODUCTO_EDITAR')")
    @PutMapping("/{id}")
    public ResponseEntity<ProductosDTO> actualizarMotoModelo(@PathVariable Long id, @RequestBody ProductosDTO dto){
        return ResponseEntity.ok(productosService.actualizarProducto(id,dto));
    }

    @PreAuthorize("hasAuthority('PRODUCTO_ELIMINAR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrarMotoModelo(@PathVariable Long id){
        productosService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }

}

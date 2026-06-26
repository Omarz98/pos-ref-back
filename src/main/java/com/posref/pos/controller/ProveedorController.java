package com.posref.pos.controller;

import com.posref.pos.dto.CategoriasDTO;
import com.posref.pos.dto.ProveedoresDTO;
import com.posref.pos.service.IProveedoresService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/proveedores")
@CrossOrigin(origins = "http://localhost:5173") // Para conectar con React
public class ProveedorController {
    @Autowired
    private IProveedoresService proveedorService;

    @GetMapping
    public ResponseEntity<List<ProveedoresDTO>> traerProveedores(){
        return ResponseEntity.ok(proveedorService.traerProveedores());
    }

    @PostMapping
    public ResponseEntity<ProveedoresDTO> crearProveedor(@RequestBody ProveedoresDTO dto){
        ProveedoresDTO creado = proveedorService.crearProveedor(dto);

        return ResponseEntity.created(URI.create("/api/proveedores"+creado.getId())).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProveedoresDTO> actualizarCategoria(@PathVariable Long id, @RequestBody ProveedoresDTO dto){
        return ResponseEntity.ok(proveedorService.actualizarProveedor(id,dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrarCategoria(@PathVariable Long id){
        proveedorService.eliminarProveedor(id);
        return ResponseEntity.noContent().build();
    }

}

package com.posref.pos.controller;

import com.posref.pos.dto.CategoriasDTO;
import com.posref.pos.service.CategoriasService;
import com.posref.pos.service.ICategoriasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@CrossOrigin(origins = "http://localhost:5173") // Para conectar con React
public class CategoriaController {

    @Autowired
    private ICategoriasService categoriasService;

    @GetMapping
    public ResponseEntity<List<CategoriasDTO>> traerCategorias(){
        return ResponseEntity.ok(categoriasService.traerCategorias());
    }

    @PostMapping
    public ResponseEntity<CategoriasDTO> crearCategoria(@RequestBody CategoriasDTO dto){
        CategoriasDTO creado = categoriasService.crearCategoria(dto);

        return ResponseEntity.created(URI.create("/api/categorias"+creado.getId())).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriasDTO> actualizarCategoria(@PathVariable Long id, @RequestBody CategoriasDTO dto){
        return ResponseEntity.ok(categoriasService.actualizarCategoria(id,dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrarCategoria(@PathVariable Long id){
        categoriasService.eliminarCategoria(id);
        return ResponseEntity.noContent().build();
    }

}

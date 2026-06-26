package com.posref.pos.controller;

import com.posref.pos.dto.CategoriasDTO;
import com.posref.pos.dto.MarcasDTO;
import com.posref.pos.service.ICategoriasService;
import com.posref.pos.service.IMarcasService;
import com.posref.pos.service.MarcasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/marcas")
@CrossOrigin(origins = "http://localhost:5173") // Para conectar con React
public class MarcaController {
    @Autowired
    private MarcasService marcasService;

    @GetMapping
    public ResponseEntity<List<MarcasDTO>> traerCategorias(){
        return ResponseEntity.ok(marcasService.traerMarcas());
    }

    @PostMapping
    public ResponseEntity<MarcasDTO> crearCategoria(@RequestBody MarcasDTO dto){
        MarcasDTO creado = marcasService.crearMarca(dto);

        return ResponseEntity.created(URI.create("/api/marcas"+creado.getId())).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MarcasDTO> actualizarCategoria(@PathVariable Long id, @RequestBody MarcasDTO dto){
        return ResponseEntity.ok(marcasService.actualizarMarca(id,dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrarCategoria(@PathVariable Long id){
        marcasService.eliminarMarca(id);
        return ResponseEntity.noContent().build();
    }

}

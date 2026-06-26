package com.posref.pos.controller;

import com.posref.pos.dto.MotoMarcasDTO;
import com.posref.pos.service.MotoMarcasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/motomarcas")
@CrossOrigin(origins = "http://localhost:5173") // Para conectar con React
public class MotoMarcaController {
    @Autowired
    private MotoMarcasService marcasService;

    @GetMapping
    public ResponseEntity<List<MotoMarcasDTO>> traerCategorias(){
        return ResponseEntity.ok(marcasService.traerMotoMarcas());
    }

    @PostMapping
    public ResponseEntity<MotoMarcasDTO> crearCategoria(@RequestBody MotoMarcasDTO dto){
        MotoMarcasDTO creado = marcasService.crearMotoMarca(dto);

        return ResponseEntity.created(URI.create("/api/motomarcas"+creado.getId())).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MotoMarcasDTO> actualizarCategoria(@PathVariable Long id, @RequestBody MotoMarcasDTO dto){
        return ResponseEntity.ok(marcasService.actualizarMotoMarca(id,dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrarCategoria(@PathVariable Long id){
        marcasService.eliminarMotoMarca(id);
        return ResponseEntity.noContent().build();
    }

}

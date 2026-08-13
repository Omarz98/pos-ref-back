package com.posref.pos.controller;

import com.posref.pos.dto.CategoriasDTO;
import com.posref.pos.dto.MarcasDTO;
import com.posref.pos.service.ICategoriasService;
import com.posref.pos.service.IMarcasService;
import com.posref.pos.service.MarcasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/marcas")
@CrossOrigin(origins = "http://localhost:5173") // Para conectar con React
public class MarcaController {
    @Autowired
    private IMarcasService marcasService;

    @PreAuthorize("hasAuthority('MARCA_VER')")
    @GetMapping
    public ResponseEntity<List<MarcasDTO>> traerCategorias(){
        return ResponseEntity.ok(marcasService.traerMarcas());
    }

    @PreAuthorize("hasAuthority('MARCA_CREAR')")
    @PostMapping
    public ResponseEntity<MarcasDTO> crearCategoria(@RequestBody MarcasDTO dto){
        MarcasDTO creado = marcasService.crearMarca(dto);

        return ResponseEntity.created(URI.create("/api/marcas"+creado.getId())).body(creado);
    }

    @PreAuthorize("hasAuthority('MARCA_EDITAR')")
    @PutMapping("/{id}")
    public ResponseEntity<MarcasDTO> actualizarCategoria(@PathVariable Long id, @RequestBody MarcasDTO dto){
        return ResponseEntity.ok(marcasService.actualizarMarca(id,dto));
    }

    @PreAuthorize("hasAuthority('MARCA_ELIMINAR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrarCategoria(@PathVariable Long id){
        marcasService.eliminarMarca(id);
        return ResponseEntity.noContent().build();
    }

}

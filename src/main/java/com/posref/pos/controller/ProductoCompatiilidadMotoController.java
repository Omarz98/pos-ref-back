package com.posref.pos.controller;

import com.posref.pos.dto.MotoVersionesDTO;
import com.posref.pos.dto.ProductoCompatibilidadMotoDTO;
import com.posref.pos.service.ProductoCompatiilidadMotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/compatibilidadproducto")
@CrossOrigin(origins = "http://localhost:5173") // Para conectar con React
public class ProductoCompatiilidadMotoController {

    @Autowired
    private ProductoCompatiilidadMotoService compatibilidadService;

    @GetMapping
    public ResponseEntity<List<ProductoCompatibilidadMotoDTO>> traerCompatibilidades(){
        return ResponseEntity.ok(compatibilidadService.traerCompatibilidades());
    }

    @PostMapping
    public ResponseEntity<ProductoCompatibilidadMotoDTO> crearCompatibilidad(@RequestBody ProductoCompatibilidadMotoDTO dto){
        ProductoCompatibilidadMotoDTO creado = compatibilidadService.crearCompatibilidad(dto);

        return ResponseEntity.created(URI.create("/api/compatibilidadproducto"+creado.getId())).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoCompatibilidadMotoDTO> actualizarConpatibilidad(@PathVariable Long id, @RequestBody ProductoCompatibilidadMotoDTO dto){
        return ResponseEntity.ok(compatibilidadService.actualizarConpatibilidad(id,dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCompatibilidad(@PathVariable Long id){
        compatibilidadService.eliminarCompatibilidad(id);
        return ResponseEntity.noContent().build();
    }

}

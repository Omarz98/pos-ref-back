package com.posref.pos.controller;

import com.posref.pos.dto.MotoServiciosDTO;
import com.posref.pos.service.IMotoServiciosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/servicios")
@CrossOrigin(origins = "http://localhost:5173") // Para conectar con React
public class MotoServiciosController {
    @Autowired
    private IMotoServiciosService motoservicioService;

    @GetMapping
    public ResponseEntity<List<MotoServiciosDTO>> traerServicios() {
        return ResponseEntity.ok(motoservicioService.traerServicios());
    }

    @PostMapping
    public ResponseEntity<MotoServiciosDTO> crearServicio(@RequestBody MotoServiciosDTO dto){
        MotoServiciosDTO creado = motoservicioService.crearServicio(dto);

        return ResponseEntity.created(URI.create("/api/servicios"+creado.getId())).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MotoServiciosDTO> actualizarServicio(@PathVariable Long id, @RequestBody MotoServiciosDTO dto){
        return ResponseEntity.ok(motoservicioService.actualizarServicio(id,dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarServicio(@PathVariable Long id){
        motoservicioService.eliminarServicio(id);
        return ResponseEntity.noContent().build();
    }
}

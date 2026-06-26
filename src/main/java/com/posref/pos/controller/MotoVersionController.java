package com.posref.pos.controller;

import com.posref.pos.dto.MotoModelosDTO;
import com.posref.pos.dto.MotoVersionesDTO;
import com.posref.pos.service.MotoModelosService;
import com.posref.pos.service.MotoVersionesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/motoversiones")
@CrossOrigin(origins = "http://localhost:5173") // Para conectar con React
public class MotoVersionController {
    @Autowired
    private MotoVersionesService motoVersionService;

    @GetMapping
    public ResponseEntity<List<MotoVersionesDTO>> traerMotoModelos(){
        return ResponseEntity.ok(motoVersionService.traerMotoVersiones());
    }

    @PostMapping
    public ResponseEntity<MotoVersionesDTO> crearMotoModelo(@RequestBody MotoVersionesDTO dto){
        MotoVersionesDTO creado = motoVersionService.crearMotoVersion(dto);

        return ResponseEntity.created(URI.create("/api/motoversiones"+creado.getId())).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MotoVersionesDTO> actualizarMotoModelo(@PathVariable Long id, @RequestBody MotoVersionesDTO dto){
        return ResponseEntity.ok(motoVersionService.actualizarMotoVersion(id,dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrarMotoModelo(@PathVariable Long id){
        motoVersionService.eliminarMotoVersion(id);
        return ResponseEntity.noContent().build();
    }
}

package com.posref.pos.controller;

import com.posref.pos.dto.MotoModelosDTO;
import com.posref.pos.service.MotoModelosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/motomodelos")
@CrossOrigin(origins = "http://localhost:5173") // Para conectar con React
public class MotoModeloController {

    @Autowired
    private MotoModelosService motoModeloService;

    @GetMapping
    public ResponseEntity<List<MotoModelosDTO>> traerMotoModelos(){
        return ResponseEntity.ok(motoModeloService.traerMotoModelos());
    }

    @PostMapping
    public ResponseEntity<MotoModelosDTO> crearMotoModelo(@RequestBody MotoModelosDTO dto){
        MotoModelosDTO creado = motoModeloService.crearMotoModelo(dto);

        return ResponseEntity.created(URI.create("/api/motomodelos"+creado.getId())).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MotoModelosDTO> actualizarMotoModelo(@PathVariable Long id, @RequestBody MotoModelosDTO dto){
        return ResponseEntity.ok(motoModeloService.actualizarMotoModelo(id,dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrarMotoModelo(@PathVariable Long id){
        motoModeloService.eliminarMotoModelo(id);
        return ResponseEntity.noContent().build();
    }


}

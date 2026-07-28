package com.posref.pos.controller;

import com.posref.pos.model.MotoMarcas;
import com.posref.pos.model.MotoModelos;
import com.posref.pos.model.MotoVersiones;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.posref.pos.repository.MotoMarcasRepository;
import com.posref.pos.repository.MotoModelosRepository;
import com.posref.pos.repository.MotoVersionesRepository;

import java.util.List;

@RestController
@RequestMapping("/api/catalogos/motos")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class MotoCatalogoController {

    private final MotoMarcasRepository motoMarcasRepository;
    private final MotoModelosRepository motoModelosRepository;
    private final MotoVersionesRepository motoVersionesRepository;

    @GetMapping("/marcas")
    public List<MotoMarcas> obtenerMarcas() {

        return motoMarcasRepository
                .findByActivoTrueOrderByNombreAsc();
    }

    @GetMapping("/marcas/{marcaId}/modelos")
    public List<MotoModelos> obtenerModelos(
            @PathVariable Long marcaId
    ) {

        return motoModelosRepository
                .findByMarcaIdAndActivoTrueOrderByNombreAsc(
                        marcaId
                );
    }

    @GetMapping("/modelos/{modeloId}/versiones")
    public List<MotoVersiones> obtenerVersiones(
            @PathVariable Long modeloId
    ) {

        return motoVersionesRepository
                .findByMotoModeloIdAndActivoTrueOrderByAnioDesc(
                        modeloId
                );
    }

}

package com.posref.pos.controller.taller;

import com.posref.pos.dto.taller.TecnicoTallerResponse;
import com.posref.pos.repository.seguridad.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/taller/tecnicos")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class TecnicoTallerController {

    private final UsuarioRepository usuarioRepository;

    @GetMapping
    public ResponseEntity<List<TecnicoTallerResponse>>
    listar() {

        List<TecnicoTallerResponse> tecnicos =
                usuarioRepository
                        .findByActivoTrueOrderByNombreAsc()
                        .stream()
                        .map(usuario ->
                                TecnicoTallerResponse
                                        .builder()
                                        .id(
                                                usuario.getId()
                                        )
                                        .nombre(
                                                usuario
                                                        .getNombre()
                                        )
                                        .username(
                                                usuario
                                                        .getUsername()
                                        )
                                        .email(
                                                usuario
                                                        .getEmail()
                                        )
                                        .build()
                        )
                        .toList();

        return ResponseEntity.ok(tecnicos);
    }
}

package com.posref.pos.controller;

import com.posref.pos.dto.CategoriasDTO;
import com.posref.pos.dto.ClienteMotoRequest;
import com.posref.pos.dto.ClienteMotoResponse;
import com.posref.pos.dto.ClientesDTO;
import com.posref.pos.model.Motocicleta;
import com.posref.pos.service.ICategoriasService;
import com.posref.pos.service.IClientesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "http://localhost:5173")
public class ClienteController {
    @Autowired
    private IClientesService clientesService;

    @PreAuthorize("hasAuthority('CLIENTE_VER')")
    @GetMapping
    public ResponseEntity<List<ClientesDTO>> traerClientes(){
        return ResponseEntity.ok(clientesService.traerClientes());
    }

    @PostMapping
    public ResponseEntity<ClientesDTO> crearCliente(@RequestBody ClientesDTO dto){
        ClientesDTO creado = clientesService.crearCliente(dto);

        return ResponseEntity.created(URI.create("/api/clientes"+creado.getId())).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientesDTO> actualizarCliente(@PathVariable Long id, @RequestBody ClientesDTO dto){
        return ResponseEntity.ok(clientesService.actualizarCliente(id,dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrarCliente(@PathVariable Long id){
        clientesService.eliminarCliente(id);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/{clienteId}/motos")
    public ResponseEntity<ClienteMotoResponse> agregarMoto(
            @PathVariable Long clienteId,
            @RequestBody ClienteMotoRequest request
    ) {

        ClienteMotoResponse moto =
                clientesService.agregarMoto(
                        clienteId,
                        request
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(moto);
    }

    @GetMapping("/{clienteId}/motos")
    public ResponseEntity<List<ClienteMotoResponse>> obtenerMotos(
            @PathVariable Long clienteId
    ) {

        return ResponseEntity.ok(
                clientesService.obtenerMotos(clienteId)
        );
    }

}

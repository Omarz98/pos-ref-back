package com.posref.pos.controller;

import com.posref.pos.dto.CategoriasDTO;
import com.posref.pos.dto.ClientesDTO;
import com.posref.pos.service.ICategoriasService;
import com.posref.pos.service.IClientesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "http://localhost:5173")
public class ClienteController {
    @Autowired
    private IClientesService clientesService;

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

}

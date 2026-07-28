package com.posref.pos.repository;

import com.posref.pos.model.Clientes;
import com.posref.pos.model.Motocicleta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientesRepository extends JpaRepository<Clientes,Long> {

}

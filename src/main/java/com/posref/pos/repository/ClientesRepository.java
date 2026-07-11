package com.posref.pos.repository;

import com.posref.pos.model.Clientes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientesRepository extends JpaRepository<Clientes,Long> {
}

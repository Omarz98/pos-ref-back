package com.posref.pos.repository;

import com.posref.pos.model.Categorias;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface  CategoriasRepository  extends JpaRepository<Categorias,Long> {
}

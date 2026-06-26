package com.posref.pos.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.util.List;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class MotoMarcas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String nombre;
    private boolean activo;

    //@OneToMany(mappedBy = "marca", cascade = CascadeType.ALL)
    //private List<MotoModelos> modelos;

}

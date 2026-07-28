package com.posref.pos.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.util.List;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(
        name = "moto_marcas",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_moto_marca_nombre",
                        columnNames = "nombre"
                )
        }
)
@Entity
public class MotoMarcas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    @Builder.Default
    @Column(nullable = false)
    private Boolean activo = true;


}

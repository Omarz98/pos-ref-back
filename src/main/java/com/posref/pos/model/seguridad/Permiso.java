package com.posref.pos.model.seguridad;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "permisos")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Permiso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String nombre;

    private String descripcion;

    @Builder.Default
    private Boolean activo = true;

}

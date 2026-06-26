package com.posref.pos.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class MotoVersiones {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private MotoModelos motoModelo;

    private Integer anio;

    private String cilindraje;

    private String version;

    private Boolean activo;

}

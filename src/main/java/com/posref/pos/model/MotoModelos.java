package com.posref.pos.model;

import jakarta.persistence.*;
import lombok.*;

@Table(
        name = "moto_modelos",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_moto_modelo",
                        columnNames = {"marca_id", "nombre"}
                )
        }
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class MotoModelos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "marca_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_moto_modelo_marca")
    )
    private MotoMarcas marca;

    @Column(nullable = false)
    private String nombre;

    @Builder.Default
    @Column(nullable = false)
    private Boolean activo = true;

}

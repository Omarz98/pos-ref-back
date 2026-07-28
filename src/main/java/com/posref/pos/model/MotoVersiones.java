package com.posref.pos.model;

import jakarta.persistence.*;
import lombok.*;

@Table(
        name = "moto_versiones",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_moto_version",
                        columnNames = {
                                "moto_modelo_id",
                                "anio",
                                "cilindraje",
                                "version"
                        }
                )
        }
)
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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "moto_modelo_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_moto_version_modelo")
    )
    private MotoModelos motoModelo;

    @Column(nullable = false)
    private Integer anio;

    private String cilindraje;

    private String version;

    @Builder.Default
    @Column(nullable = false)
    private Boolean activo = true;

}

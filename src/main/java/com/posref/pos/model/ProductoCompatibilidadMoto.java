package com.posref.pos.model;

import jakarta.persistence.*;
import lombok.*;

@Table(
        name = "producto_compatibilidad_moto",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_producto_moto_version",
                        columnNames = {
                                "producto_id",
                                "moto_version_id"
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
public class ProductoCompatibilidadMoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "producto_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_compatibilidad_producto"
            )
    )
    private Productos producto;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "moto_version_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_compatibilidad_moto_version"
            )
    )
    private MotoVersiones motoVersion;

    @Column(length = 255)
    private String observaciones;

    @Builder.Default
    @Column(nullable = false)
    private Boolean activo = true;

}

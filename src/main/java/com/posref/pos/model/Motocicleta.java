package com.posref.pos.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "motocicletas",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_motocicleta_numero_serie",
                        columnNames = "numero_serie"
                ),
                @UniqueConstraint(
                        name = "uk_motocicleta_placas",
                        columnNames = "placas"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Motocicleta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "cliente_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_motocicleta_cliente")
    )
    private Clientes cliente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "moto_version_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_motocicleta_version")
    )
    private MotoVersiones motoVersion;

    @Column(name = "numero_serie", length = 100)
    private String numeroSerie;

    @Column(name = "numero_motor", length = 100)
    private String numeroMotor;

    @Column(length = 20)
    private String placas;

    @Column(length = 50)
    private String color;

    @Builder.Default
    @Column(name = "kilometraje_actual", nullable = false)
    private Integer kilometrajeActual = 0;

    @Column(length = 500)
    private String observaciones;

    @Builder.Default
    @Column(nullable = false)
    private Boolean activo = true;

    @Builder.Default
    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @Builder.Default
    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion = LocalDateTime.now();

    @PrePersist
    public void prePersist() {
        LocalDateTime ahora = LocalDateTime.now();

        if (fechaCreacion == null) {
            fechaCreacion = ahora;
        }

        fechaActualizacion = ahora;

        if (activo == null) {
            activo = true;
        }

        if (kilometrajeActual == null) {
            kilometrajeActual = 0;
        }
    }

    @PreUpdate
    public void preUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }
}

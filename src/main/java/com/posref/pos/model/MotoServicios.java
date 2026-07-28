package com.posref.pos.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@   Table(
        name = "moto_servicios",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_moto_servicios_codigo",
                        columnNames = "codigo"
                )
        }
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class MotoServicios {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String codigo;

    @Column(nullable = false)
    private String nombre;

    @Column(length = 500)
    private String descripcion;

    @Builder.Default
    @Column(
            name = "precio_venta",
            nullable = false,
            precision = 12,
            scale = 2
    )
    private BigDecimal precioVenta = BigDecimal.ZERO;

    @Column(name = "duracion_estimada_minutos")
    private Integer duracionEstimadaMinutos;

    @Builder.Default
    @Column(name = "aplica_iva", nullable = false)
    private Boolean aplicaIva = true;

    @Builder.Default
    @Column(nullable = false)
    private Boolean activo = true;

}

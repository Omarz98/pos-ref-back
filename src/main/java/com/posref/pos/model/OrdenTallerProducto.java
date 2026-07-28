package com.posref.pos.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orden_taller_productos")
public class OrdenTallerProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "orden_taller_id",
            nullable = false
    )
    private OrdenTaller ordenTaller;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "producto_id",
            nullable = false
    )
    private Productos producto;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(
            name = "precio_unitario",
            nullable = false,
            precision = 12,
            scale = 2
    )
    private BigDecimal precioUnitario;

    @Column(
            nullable = false,
            precision = 12,
            scale = 2
    )
    private BigDecimal subtotal;
}

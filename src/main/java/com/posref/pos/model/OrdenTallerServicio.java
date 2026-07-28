package com.posref.pos.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orden_taller_servicios")
public class OrdenTallerServicio {

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
            name = "servicio_id",
            nullable = false
    )
    private MotoServicios servicio;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

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

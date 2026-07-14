package com.posref.pos.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class VentaPago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private MetodoPago metodo;

    private BigDecimal monto;

    @ManyToOne
    @JoinColumn(name = "venta_id")
    private Ventas venta;

}

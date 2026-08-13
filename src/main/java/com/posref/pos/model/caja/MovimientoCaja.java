package com.posref.pos.model.caja;

import com.posref.pos.model.Ventas;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "caja_movimientos")
public class MovimientoCaja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "caja_sesion_id", nullable = false)
    private CajaSesion cajaSesion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venta_id")
    private Ventas venta;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TipoMovimientoCaja tipo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private MetodoMovimientoCaja metodo;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal monto;

    @Column(nullable = false, length = 250)
    private String concepto;

    @Column(nullable = false)
    private LocalDateTime fecha;

}

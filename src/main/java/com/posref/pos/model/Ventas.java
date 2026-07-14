package com.posref.pos.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Ventas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime fecha;

    private BigDecimal subtotal;

    private BigDecimal iva;

    private BigDecimal total;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Clientes cliente;

    private BigDecimal totalPagado;

    private BigDecimal saldoPendiente;

    @Enumerated(EnumType.STRING)
    private EstadoVenta estado;

    @OneToMany(
            mappedBy = "venta",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<VentaDetalle> detalles;

    @OneToMany(
            mappedBy = "venta",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<VentaPago> pagos;


}

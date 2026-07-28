package com.posref.pos.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
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

    @Builder.Default
    @OneToMany(
            mappedBy = "venta",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<VentaDetalle> detalles = new ArrayList<>();

    @Builder.Default
    @OneToMany(
            mappedBy = "venta",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<VentaPago> pagos = new ArrayList<>();

    public void agregarPago(VentaPago pago) {
        pagos.add(pago);
        pago.setVenta(this);
    }

    public void eliminarPago(VentaPago pago) {
        pagos.remove(pago);
        pago.setVenta(null);
    }

    public void agregarDetalle(VentaDetalle detalle) {
        if (detalles == null) {
            detalles = new ArrayList<>();
        }

        detalles.add(detalle);
        detalle.setVenta(this);
    }

    public void eliminarDetalle(VentaDetalle detalle) {
        if (detalles != null) {
            detalles.remove(detalle);
        }

        detalle.setVenta(null);
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orden_taller_id", unique = true)
    private OrdenTaller ordenTaller;

}

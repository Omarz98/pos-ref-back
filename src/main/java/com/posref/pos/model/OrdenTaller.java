package com.posref.pos.model;

import com.posref.pos.model.seguridad.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ordenes_taller")
public class OrdenTaller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "folio",
            nullable = false,
            unique = true,
            length = 30
    )
    private String folio;

    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    @JoinColumn(
            name = "cliente_id",
            nullable = false
    )
    private Clientes cliente;

    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    @JoinColumn(
            name = "moto_cliente_id",
            nullable = false
    )
    private Motocicleta motocicleta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tecnico_id")
    private Usuario tecnico;

    @Column(
            name = "fecha_recepcion",
            nullable = false
    )
    private LocalDateTime fechaRecepcion;

    @Column(name = "fecha_entrega_estimada")
    private LocalDateTime fechaEntregaEstimada;

    @Column(name = "fecha_entrega_real")
    private LocalDateTime fechaEntregaReal;

    private Integer kilometraje;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "nivel_combustible",
            length = 30
    )
    private NivelCombustible nivelCombustible;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "prioridad",
            nullable = false,
            length = 30
    )
    private PrioridadOrdenTaller prioridad;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "estado",
            nullable = false,
            length = 40
    )
    private EstadoOrdenTrabajo estado;

    @Column(
            name = "falla_reportada",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String fallaReportada;

    @Column(
            name = "diagnostico_inicial",
            columnDefinition = "TEXT"
    )
    private String diagnosticoInicial;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    @Column(
            name = "subtotal_servicios",
            nullable = false,
            precision = 12,
            scale = 2
    )
    private BigDecimal subtotalServicios;

    @Column(
            name = "subtotal_productos",
            nullable = false,
            precision = 12,
            scale = 2
    )
    private BigDecimal subtotalProductos;

    @Column(
            nullable = false,
            precision = 12,
            scale = 2
    )
    private BigDecimal subtotal;

    @Column(
            nullable = false,
            precision = 12,
            scale = 2
    )
    private BigDecimal iva;

    @Column(
            nullable = false,
            precision = 12,
            scale = 2
    )
    private BigDecimal total;

    @Column(
            nullable = false,
            precision = 12,
            scale = 2
    )
    private BigDecimal anticipo;

    @Column(
            name = "saldo_pendiente",
            nullable = false,
            precision = 12,
            scale = 2
    )
    private BigDecimal saldoPendiente;

    @Column(
            name = "fecha_creacion",
            nullable = false
    )
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @Builder.Default
    @OneToMany(
            mappedBy = "ordenTaller",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<OrdenTallerServicio> servicios =
            new ArrayList<>();

    @Builder.Default
    @OneToMany(
            mappedBy = "ordenTaller",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<OrdenTallerProducto> productos =
            new ArrayList<>();

    public void agregarServicio(
            OrdenTallerServicio detalle
    ) {
        servicios.add(detalle);
        detalle.setOrdenTaller(this);
    }

    public void agregarProducto(
            OrdenTallerProducto detalle
    ) {
        productos.add(detalle);
        detalle.setOrdenTaller(this);
    }
}

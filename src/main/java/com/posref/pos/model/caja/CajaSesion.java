package com.posref.pos.model.caja;

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
@Table(name = "caja_sesiones")
public class CajaSesion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "nombre_caja", nullable = false, length = 100)
    private String nombreCaja;

    @Column(name = "fecha_apertura", nullable = false)
    private LocalDateTime fechaApertura;

    @Column(name = "fecha_cierre")
    private LocalDateTime fechaCierre;

    @Column(name = "monto_inicial", nullable = false, precision = 12, scale = 2)
    private BigDecimal montoInicial;

    @Column(name = "efectivo_esperado", precision = 12, scale = 2)
    private BigDecimal efectivoEsperado;

    @Column(name = "efectivo_contado", precision = 12, scale = 2)
    private BigDecimal efectivoContado;

    @Column(precision = 12, scale = 2)
    private BigDecimal diferencia;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoCaja estado;

    @Column(name = "observaciones_apertura", length = 500)
    private String observacionesApertura;

    @Column(name = "observaciones_cierre", length = 500)
    private String observacionesCierre;

    @Builder.Default
    @OneToMany(
            mappedBy = "cajaSesion",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<MovimientoCaja> movimientos = new ArrayList<>();

}

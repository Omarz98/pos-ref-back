package com.posref.pos.repository.caja;

import com.posref.pos.model.caja.MetodoMovimientoCaja;
import com.posref.pos.model.caja.MovimientoCaja;
import com.posref.pos.model.caja.TipoMovimientoCaja;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface MovimientoCajaRepository
        extends JpaRepository<MovimientoCaja, Long> {

    List<MovimientoCaja> findByCajaSesionIdOrderByFechaAsc(
            Long cajaSesionId
    );

    @Query("""
        select coalesce(sum(m.monto), 0)
        from MovimientoCaja m
        where m.cajaSesion.id = :sesionId
          and m.tipo = :tipo
          and m.metodo = :metodo
    """)
    BigDecimal sumarPorTipoYMetodo(
            @Param("sesionId") Long sesionId,
            @Param("tipo") TipoMovimientoCaja tipo,
            @Param("metodo") MetodoMovimientoCaja metodo
    );
}

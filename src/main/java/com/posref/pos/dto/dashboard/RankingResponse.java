package com.posref.pos.dto.dashboard;

import java.math.BigDecimal;

public record RankingResponse(
        String id,

        String nombre,

        Long cantidad,

        BigDecimal total
) {
}

package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PlanAhorroRequest(
        Long idUsuario,
        String nombre,
        BigDecimal montoPeriodico,
        String frecuencia,
        Integer diaEjecucion,
        LocalDate fechaInicio,
        LocalDate fechaFin
) {
}

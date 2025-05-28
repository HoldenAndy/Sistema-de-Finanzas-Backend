package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AhorroFijoResponse(
        Long idAhorroFijo,
        String nombre,
        BigDecimal montoObjetivo,
        BigDecimal montoActual,
        BigDecimal progreso,
        LocalDate fechaInicio,
        LocalDate fechaObjetivo,
        String estado,
        boolean completado
) {
}

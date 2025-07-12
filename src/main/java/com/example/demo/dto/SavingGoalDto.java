package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SavingGoalDto(
        String nombre,
        BigDecimal montoObjetivo,
        LocalDate fechaObjetivo,
        String descripcion,
        String categoria
) {
}

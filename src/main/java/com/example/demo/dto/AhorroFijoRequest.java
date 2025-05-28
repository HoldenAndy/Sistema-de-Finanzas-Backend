package com.example.demo.dto;

import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

public record AhorroFijoRequest(
        Long idUsuario,
        String nombre,
        BigDecimal montoObjetivo,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate fechaObjetivo
) {
}

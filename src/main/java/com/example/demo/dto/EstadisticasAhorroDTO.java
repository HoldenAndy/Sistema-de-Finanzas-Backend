package com.example.demo.dto;

import java.math.BigDecimal;

public record EstadisticasAhorroDTO(
        BigDecimal totalAhorrado,
        Integer cantidadPlanes,
        BigDecimal promedioMensual,
        BigDecimal proyeccionAnual
) {
}

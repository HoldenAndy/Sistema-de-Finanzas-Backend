package com.example.demo.dto;

import lombok.Data; // Para generar getters y setters automáticamente

import java.math.BigDecimal;

@Data
public class IngresoDto {
    private BigDecimal monto;
    private String descripcion;
    private String tipo;

}
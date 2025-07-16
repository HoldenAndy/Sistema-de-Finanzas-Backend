package com.example.demo.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class IngresoDto {
    private BigDecimal monto;
    private String descripcion;
    private String tipo;

}
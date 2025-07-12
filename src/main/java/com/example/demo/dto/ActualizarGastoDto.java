// src/main/java/com/example/demo/dto/ActualizarGastoDto.java
package com.example.demo.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public class ActualizarGastoDto {

    // Los campos son opcionales para la actualización si no se usan @NotNull,
    // pero si se envían, deben cumplir las validaciones
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor que cero")
    private BigDecimal monto;

    private String descripcion;

    private LocalDate fechaGasto;

    private String categoria;

    // Getters y Setters
    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFechaGasto() {
        return fechaGasto;
    }

    public void setFechaGasto(LocalDate fechaGasto) {
        this.fechaGasto = fechaGasto;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
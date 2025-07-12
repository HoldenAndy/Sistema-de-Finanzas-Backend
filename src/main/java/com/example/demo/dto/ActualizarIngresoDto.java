package com.example.demo.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public class ActualizarIngresoDto {

    @NotNull(message = "El monto no puede ser nulo")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor que cero")
    private BigDecimal monto;

    @NotBlank(message = "La descripción no puede estar vacía")
    private String descripcion;

    @NotNull(message = "La fecha de ingreso no puede ser nula")
    private LocalDate fechaIngreso;

    @NotBlank(message = "La categoría no puede estar vacía")
    private String categoria;

    @NotBlank(message = "El tipo de ingreso no puede estar vacío") // <-- ¡Añadido!
    private String tipo; // <-- ¡Añadido!

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

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getTipo() { // <-- ¡Nuevo Getter!
        return tipo;
    }

    public void setTipo(String tipo) { // <-- ¡Nuevo Setter!
        this.tipo = tipo;
    }
}
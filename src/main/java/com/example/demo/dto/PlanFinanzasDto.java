package com.example.demo.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

// No necesitas @Data aquí a menos que quieras Lombok para los DTOs,
// pero por claridad y para evitar dependencias implícitas en el DTO, los Getters/Setters se escriben.
public class PlanFinanzasDto {

    @NotNull(message = "El sueldo base no puede ser nulo")
    @DecimalMin(value = "0.00", inclusive = true, message = "El sueldo base no puede ser negativo")
    private BigDecimal sueldoBase;

    @NotNull(message = "La fecha de inicio no puede ser nula")
    private LocalDate fechaInicio;

    private LocalDate fechaFin; // Puede ser nula, por eso no @NotNull

    @NotBlank(message = "El estado no puede estar vacío")
    private String estado; // Cambiado de 'activo' a 'estado'

    // Getters y Setters (los escribo para asegurar compatibilidad si no usas Lombok en DTOs)

    public BigDecimal getSueldoBase() {
        return sueldoBase;
    }

    public void setSueldoBase(BigDecimal sueldoBase) {
        this.sueldoBase = sueldoBase;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
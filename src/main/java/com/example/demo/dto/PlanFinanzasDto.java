package com.example.demo.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public class PlanFinanzasDto {

    @NotNull(message = "El sueldo base no puede ser nulo")
    @DecimalMin(value = "0.00", inclusive = true, message = "El sueldo base no puede ser negativo")
    private BigDecimal sueldoBase;

    @NotNull(message = "La fecha de inicio no puede ser nula")
    private LocalDate fechaInicio;

    private LocalDate fechaFin;

    @NotBlank(message = "El estado no puede estar vacío")
    private String estado;

    // Campos adicionales del wizard avanzado
    private String nombre;
    private BigDecimal otrosIngresos;
    private String metaPrincipal;
    private BigDecimal montoObjetivo;
    private BigDecimal distribucionNecesidades;
    private BigDecimal distribucionDeseos;
    private BigDecimal distribucionAhorros;
    private String tipoDistribucion;
    private String prioridad;

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

    // Getters y setters para campos del wizard
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getOtrosIngresos() {
        return otrosIngresos;
    }

    public void setOtrosIngresos(BigDecimal otrosIngresos) {
        this.otrosIngresos = otrosIngresos;
    }

    public String getMetaPrincipal() {
        return metaPrincipal;
    }

    public void setMetaPrincipal(String metaPrincipal) {
        this.metaPrincipal = metaPrincipal;
    }

    public BigDecimal getMontoObjetivo() {
        return montoObjetivo;
    }

    public void setMontoObjetivo(BigDecimal montoObjetivo) {
        this.montoObjetivo = montoObjetivo;
    }

    public BigDecimal getDistribucionNecesidades() {
        return distribucionNecesidades;
    }

    public void setDistribucionNecesidades(BigDecimal distribucionNecesidades) {
        this.distribucionNecesidades = distribucionNecesidades;
    }

    public BigDecimal getDistribucionDeseos() {
        return distribucionDeseos;
    }

    public void setDistribucionDeseos(BigDecimal distribucionDeseos) {
        this.distribucionDeseos = distribucionDeseos;
    }

    public BigDecimal getDistribucionAhorros() {
        return distribucionAhorros;
    }

    public void setDistribucionAhorros(BigDecimal distribucionAhorros) {
        this.distribucionAhorros = distribucionAhorros;
    }

    public String getTipoDistribucion() {
        return tipoDistribucion;
    }

    public void setTipoDistribucion(String tipoDistribucion) {
        this.tipoDistribucion = tipoDistribucion;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }
}
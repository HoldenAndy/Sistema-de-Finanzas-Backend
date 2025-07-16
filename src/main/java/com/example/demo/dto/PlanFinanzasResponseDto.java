package com.example.demo.dto;

import com.example.demo.entity.PlanFinanzas;
import java.math.BigDecimal;
import java.time.LocalDate;

public class PlanFinanzasResponseDto {

    private Integer id;
    private BigDecimal sueldoBase;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String estado;
    private Integer usuarioId;
    private BigDecimal totalIngresos;
    private BigDecimal totalGastos;
    private BigDecimal saldoActual;

    public PlanFinanzasResponseDto() {
    }

    public PlanFinanzasResponseDto(PlanFinanzas planFinanzas, BigDecimal totalIngresos, BigDecimal totalGastos, BigDecimal saldoActual) {
        this.id = planFinanzas.getId();
        this.sueldoBase = planFinanzas.getSueldoBase();
        this.fechaInicio = planFinanzas.getFechaInicio();
        this.fechaFin = planFinanzas.getFechaFin();
        this.estado = planFinanzas.getEstado();
        this.usuarioId = planFinanzas.getUsuario() != null ? planFinanzas.getUsuario().getId() : null;
        this.totalIngresos = totalIngresos;
        this.totalGastos = totalGastos;
        this.saldoActual = saldoActual;
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }

    public BigDecimal getTotalIngresos() {
        return totalIngresos;
    }

    public void setTotalIngresos(BigDecimal totalIngresos) {
        this.totalIngresos = totalIngresos;
    }

    public BigDecimal getTotalGastos() {
        return totalGastos;
    }

    public void setTotalGastos(BigDecimal totalGastos) {
        this.totalGastos = totalGastos;
    }

    public BigDecimal getSaldoActual() {
        return saldoActual;
    }

    public void setSaldoActual(BigDecimal saldoActual) {
        this.saldoActual = saldoActual;
    }
}
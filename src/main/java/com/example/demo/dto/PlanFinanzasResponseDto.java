package com.example.demo.dto;

import com.example.demo.entity.PlanFinanzas;
import java.math.BigDecimal;
import java.time.LocalDate;

public class PlanFinanzasResponseDto {

    private Integer id;
    private String nombre;
    private String metaPrincipal;
    private String prioridad;
    private BigDecimal sueldoBase;
    private BigDecimal otrosIngresos;
    private BigDecimal montoObjetivo;
    private String tipoDistribucion;
    private BigDecimal distribucionNecesidades;
    private BigDecimal distribucionDeseos;
    private BigDecimal distribucionAhorros;
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
        this.nombre = planFinanzas.getNombre();
        this.metaPrincipal = planFinanzas.getMetaPrincipal();
        this.prioridad = planFinanzas.getPrioridad();
        this.sueldoBase = planFinanzas.getSueldoBase();
        this.otrosIngresos = planFinanzas.getOtrosIngresos();
        this.montoObjetivo = planFinanzas.getMontoObjetivo();
        this.tipoDistribucion = planFinanzas.getTipoDistribucion();
        this.distribucionNecesidades = planFinanzas.getDistribucionNecesidades();
        this.distribucionDeseos = planFinanzas.getDistribucionDeseos();
        this.distribucionAhorros = planFinanzas.getDistribucionAhorros();
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMetaPrincipal() {
        return metaPrincipal;
    }

    public void setMetaPrincipal(String metaPrincipal) {
        this.metaPrincipal = metaPrincipal;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    public BigDecimal getOtrosIngresos() {
        return otrosIngresos;
    }

    public void setOtrosIngresos(BigDecimal otrosIngresos) {
        this.otrosIngresos = otrosIngresos;
    }

    public BigDecimal getMontoObjetivo() {
        return montoObjetivo;
    }

    public void setMontoObjetivo(BigDecimal montoObjetivo) {
        this.montoObjetivo = montoObjetivo;
    }

    public String getTipoDistribucion() {
        return tipoDistribucion;
    }

    public void setTipoDistribucion(String tipoDistribucion) {
        this.tipoDistribucion = tipoDistribucion;
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
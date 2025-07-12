package com.example.demo.entity;


import jakarta.persistence.*;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "planes_ahorro")
@Setter
public class PlanAhorro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idPlanAhorro;

    @Column(name = "id_usuario", nullable = false)
    private long idUsuario;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "monto_periodico", nullable = false, precision = 12, scale = 2)
    private BigDecimal montoPeriodico;

    @Enumerated(EnumType.STRING)
    @Column(name = "frecuencia", nullable = false)
    private FrecuenciaAhorro frecuencia;

    @Column(name = "dia_ejecucion", nullable = false)
    private Integer diaEjecucion;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", length = 20)
    private EstadoAhorro estado = EstadoAhorro.ACTIVO;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public PlanAhorro(Long idUsuario, String nombre, BigDecimal montoPeriodico,
                      FrecuenciaAhorro frecuencia, LocalDate fechaInicio) {
        this.idUsuario = Objects.requireNonNull(idUsuario);
        this.nombre = Objects.requireNonNull(nombre);
        this.montoPeriodico = Objects.requireNonNull(montoPeriodico);
        this.frecuencia = Objects.requireNonNull(frecuencia);
        this.fechaInicio = Objects.requireNonNull(fechaInicio);
        this.estado = EstadoAhorro.ACTIVO;
    }

    public PlanAhorro() {
    }

    // Setters manuales para asegurar compatibilidad
    public void setIdUsuario(long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setMontoPeriodico(BigDecimal montoPeriodico) {
        this.montoPeriodico = montoPeriodico;
    }

    public void setFrecuencia(FrecuenciaAhorro frecuencia) {
        this.frecuencia = frecuencia;
    }

    public void setDiaEjecucion(Integer diaEjecucion) {
        this.diaEjecucion = diaEjecucion;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public void setEstado(EstadoAhorro estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "PlanAhorro{" +
                "idPlanAhorro=" + idPlanAhorro +
                ", idUsuario=" + idUsuario +
                ", nombre='" + nombre + '\'' +
                ", montoPeriodico=" + montoPeriodico +
                ", frecuencia=" + frecuencia +
                ", diaEjecucion=" + diaEjecucion +
                ", fechaInicio=" + fechaInicio +
                ", fechaFin=" + fechaFin +
                ", estado=" + estado +
                '}';
    }

    public LocalDate calcularProximaEjecucion() {
        return switch (frecuencia) {
            case DIARIO -> fechaInicio.plusDays(1);
            case SEMANAL -> fechaInicio.plusWeeks(1);
            case MENSUAL -> fechaInicio.plusMonths(1);
            case ANUAL -> fechaInicio.plusYears(1);
        };
    }


    public boolean debeEjecutarseHoy() {
        LocalDate hoy = LocalDate.now();
        return switch (frecuencia) {
            case DIARIO -> true;
            case SEMANAL -> hoy.getDayOfWeek().getValue() == diaEjecucion;
            case MENSUAL -> hoy.getDayOfMonth() == diaEjecucion;
            case ANUAL -> hoy.getDayOfYear() == fechaInicio.getDayOfYear();
        };
    }

    public long getIdPlanAhorro() {
        return idPlanAhorro;
    }

    public long getIdUsuario() {
        return idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public BigDecimal getMontoPeriodico() {
        return montoPeriodico;
    }

    public FrecuenciaAhorro getFrecuencia() {
        return frecuencia;
    }

    public Integer getDiaEjecucion() {
        return diaEjecucion;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public EstadoAhorro getEstado() {
        return estado;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public enum EstadoAhorro {
        ACTIVO, COMPLETADO, PAUSADO, CANCELADO
    }

    public enum FrecuenciaAhorro {
        DIARIO, SEMANAL, MENSUAL, ANUAL
    }

}

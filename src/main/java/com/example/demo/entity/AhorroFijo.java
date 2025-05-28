package com.example.demo.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "ahorro_fijo")
public class AhorroFijo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAhorroFijo;

    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "monto_objetivo", nullable = false, precision = 12, scale = 2)
    private BigDecimal montoObjetivo;

    @Column(name = "monto_actual", nullable = false, precision = 12, scale = 2)
    private BigDecimal montoActual = BigDecimal.ZERO;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_objetivo", nullable = false)
    private LocalDate fechaObjetivo;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", length = 20)
    private EstadoAhorro estado = EstadoAhorro.ACTIVO;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public AhorroFijo(Long idUsuario, String nombre, BigDecimal montoObjetivo, LocalDate fechaObjetivo) {
        this.idUsuario = Objects.requireNonNull(idUsuario, "ID usuario no puede ser null");
        this.nombre = Objects.requireNonNull(nombre, "Nombre no puede ser null");
        this.montoObjetivo = Objects.requireNonNull(montoObjetivo, "Monto objetivo no puede ser null");
        this.fechaInicio = LocalDate.now();
        this.fechaObjetivo = fechaObjetivo;
        this.montoActual = BigDecimal.ZERO;
        this.estado = EstadoAhorro.ACTIVO;
    }

    public AhorroFijo() {
    }

    public AhorroFijo agregarMonto(BigDecimal monto) {
        return new AhorroFijo.Builder(this).conMontoActual(this.montoActual.add(monto)).build();
    }

    public AhorroFijo cambiarEstado(EstadoAhorro nuevoEstado) {
        return new AhorroFijo.Builder(this).conEstado(nuevoEstado).build();
    }

    public BigDecimal calcularProgreso() {
        if (montoObjetivo.compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO;
        return montoActual.divide(montoObjetivo, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
    }

    public boolean estaCompleto() {
        return montoObjetivo.compareTo(montoObjetivo) >= 0;
    }

    public static class Builder {
        private final AhorroFijo original;
        private Long idUsuario;
        private String nombre;
        private BigDecimal montoObjetivo;
        private BigDecimal montoActual;
        private LocalDate fechaInicio;
        private LocalDate fechaObjetivo;
        private EstadoAhorro estado;

        public Builder(AhorroFijo original) {
            this.original = original;
            this.idUsuario = original.idUsuario;
            this.nombre = original.nombre;
            this.montoObjetivo = original.montoObjetivo;
            this.montoActual = original.montoActual;
            this.fechaInicio = original.fechaInicio;
            this.fechaObjetivo = original.fechaObjetivo;
            this.estado = original.estado;
        }

        public Builder conMontoActual(BigDecimal montoActual) {
            this.montoActual = montoActual;
            return this;
        }

        public Builder conEstado(EstadoAhorro estado) {
            this.estado = estado;
            return this;
        }

        public AhorroFijo build() {
            AhorroFijo nuevo = new AhorroFijo();
            nuevo.idAhorroFijo = original.idAhorroFijo;
            nuevo.idUsuario = this.idUsuario;
            nuevo.nombre = this.nombre;
            nuevo.montoObjetivo = this.montoObjetivo;
            nuevo.montoActual = this.montoActual;
            nuevo.fechaInicio = this.fechaInicio;
            nuevo.fechaObjetivo = this.fechaObjetivo;
            nuevo.estado = this.estado;
            nuevo.createdAt = original.createdAt;
            return nuevo;
        }
    }

    public enum EstadoAhorro {
        ACTIVO, COMPLETADO, PAUSADO, CANCELADO
    }

    public Long getIdAhorroFijo() {
        return idAhorroFijo;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public BigDecimal getMontoObjetivo() {
        return montoObjetivo;
    }

    public BigDecimal getMontoActual() {
        return montoActual;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaObjetivo() {
        return fechaObjetivo;
    }

    public EstadoAhorro getEstado() {
        return estado;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}

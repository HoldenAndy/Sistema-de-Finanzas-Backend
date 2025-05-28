package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Table(name = "planesfinanzas")
public class PlanFinanzas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_plan;

    @ManyToOne(fetch = FetchType.LAZY) // Es importante que sea LAZY
    @JoinColumn(name = "id_usuario")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "planesFinanzas"}) // "planesFinanzas" si existe en Usuario
    private Usuario usuario;

    @Column(name = "sueldo_base", precision = 10, scale = 2)
    private BigDecimal sueldoBase;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    @Column(name = "estado", length = 50, nullable = false)
    private String estado;

    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;

    @OneToMany(mappedBy = "planFinanzas", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "planFinanzas"})
    private List<Ingreso> ingresos;

    public PlanFinanzas() {
    }

    public PlanFinanzas(Usuario usuario, BigDecimal sueldoBase, LocalDate fechaInicio, LocalDate fechaFin, String estado) {
        this.usuario = usuario;
        this.sueldoBase = sueldoBase;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = estado;
    }

    public Integer getId_plan() { return id_plan; }
    public void setId_plan(Integer id_plan) { this.id_plan = id_plan; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public BigDecimal getSueldoBase() { return sueldoBase; }
    public void setSueldoBase(BigDecimal sueldoBase) { this.sueldoBase = sueldoBase; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }
    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = OffsetDateTime.now();
        }
    }
}
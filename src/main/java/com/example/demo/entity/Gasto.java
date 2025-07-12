package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "gastos")
public class Gasto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private BigDecimal monto;

    @Column(nullable = false)
    private String descripcion;

    @Column(name = "fecha_gasto", nullable = false)
    private LocalDate fechaGasto;

    @Column(nullable = false)
    private String categoria;

    // Relación Many-to-One con PlanFinanzas
    // Un gasto pertenece a un único PlanFinanzas
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false) // Asegúrate que 'plan_id' sea el nombre de la columna FK en tu tabla 'gastos'
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "gastos"}) // Evita bucles infinitos en JSON y carga perezosa
    private PlanFinanzas planFinanzas;

    // Constructores
    public Gasto() {
    }

    public Gasto(BigDecimal monto, String descripcion, LocalDate fechaGasto, String categoria, PlanFinanzas planFinanzas) {
        this.monto = monto;
        this.descripcion = descripcion;
        this.fechaGasto = fechaGasto;
        this.categoria = categoria;
        this.planFinanzas = planFinanzas;
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public PlanFinanzas getPlanFinanzas() {
        return planFinanzas;
    }

    public void setPlanFinanzas(PlanFinanzas planFinanzas) {
        this.planFinanzas = planFinanzas;
    }

    // Opcional: toString(), equals(), hashCode() si los necesitas para depuración o colecciones
}
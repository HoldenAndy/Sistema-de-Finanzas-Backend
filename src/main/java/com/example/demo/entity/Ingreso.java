package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "plan_ingresos")
public class Ingreso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_ingreso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_plan", referencedColumnName = "id_plan", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "ingresos", "gastos"}) // "ingresos" y "gastos" si existen en PlanFinanzas para evitar bucles
    private PlanFinanzas planFinanzas;

    @Column(name = "monto", precision = 10, scale = 2, nullable = false)
    private BigDecimal monto;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "descripcion", length = 100)
    private String descripcion;

    @Column(name = "tipo", length = 50, nullable = false)
    private String tipo;

    public Ingreso() {
    }

    public Ingreso(PlanFinanzas planFinanzas, BigDecimal monto, LocalDate fecha, String descripcion, String tipo) {
        this.planFinanzas = planFinanzas;
        this.monto = monto;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.tipo = tipo;
    }

    public Integer getId_ingreso() { return id_ingreso; }
    public void setId_ingreso(Integer id_ingreso) { this.id_ingreso = id_ingreso; }
    public PlanFinanzas getPlanFinanzas() { return planFinanzas; }
    public void setPlanFinanzas(PlanFinanzas planFinanzas) { this.planFinanzas = planFinanzas; }
    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
}
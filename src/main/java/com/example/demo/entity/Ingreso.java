package com.example.demo.entity;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;

import java.math.BigDecimal;

import java.time.LocalDate; // <-- Importación necesaria



@Entity

@Table(name = "ingresos")

public class Ingreso {



    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer id;



    private BigDecimal monto;



    private String descripcion;



    @Column(name = "fecha_ingreso")

    private LocalDate fechaIngreso; // <-- Campo añadido en una corrección anterior



    private String categoria; // <-- Campo añadido en una corrección anterior



    private String tipo; // <-- ¡Nuevo campo añadido!



    @ManyToOne(fetch = FetchType.LAZY)

    @JoinColumn(name = "plan_id")

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "ingresos"})

    private PlanFinanzas planFinanzas;



    // Constructores (puedes añadir uno por defecto si lo necesitas)

    public Ingreso() {

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



    public String getTipo() {

        return tipo;

    }



    public void setTipo(String tipo) {

        this.tipo = tipo;

    }



    public PlanFinanzas getPlanFinanzas() {

        return planFinanzas;

    }



    public void setPlanFinanzas(PlanFinanzas planFinanzas) {

        this.planFinanzas = planFinanzas;

    }

}
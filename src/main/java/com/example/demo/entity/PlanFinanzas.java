package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Table(name = "planesfinanzas")
public class PlanFinanzas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "planesFinanzas"})
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


    @Column(name = "nombre", length = 100)
    private String nombre;

    @Column(name = "otros_ingresos", precision = 10, scale = 2)
    private BigDecimal otrosIngresos;

    @Column(name = "meta_principal", length = 255)
    private String metaPrincipal;

    @Column(name = "monto_objetivo", precision = 12, scale = 2)
    private BigDecimal montoObjetivo;

    @Column(name = "distribucion_necesidades", precision = 5, scale = 2)
    private BigDecimal distribucionNecesidades;

    @Column(name = "distribucion_deseos", precision = 5, scale = 2)
    private BigDecimal distribucionDeseos;

    @Column(name = "distribucion_ahorros", precision = 5, scale = 2)
    private BigDecimal distribucionAhorros;

    @Column(name = "tipo_distribucion", length = 50)
    private String tipoDistribucion;

    @Column(name = "prioridad", length = 50)
    private String prioridad;

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

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

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

    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public BigDecimal getOtrosIngresos() { return otrosIngresos; }
    public void setOtrosIngresos(BigDecimal otrosIngresos) { this.otrosIngresos = otrosIngresos; }

    public String getMetaPrincipal() { return metaPrincipal; }
    public void setMetaPrincipal(String metaPrincipal) { this.metaPrincipal = metaPrincipal; }

    public BigDecimal getMontoObjetivo() { return montoObjetivo; }
    public void setMontoObjetivo(BigDecimal montoObjetivo) { this.montoObjetivo = montoObjetivo; }

    public BigDecimal getDistribucionNecesidades() { return distribucionNecesidades; }
    public void setDistribucionNecesidades(BigDecimal distribucionNecesidades) { this.distribucionNecesidades = distribucionNecesidades; }

    public BigDecimal getDistribucionDeseos() { return distribucionDeseos; }
    public void setDistribucionDeseos(BigDecimal distribucionDeseos) { this.distribucionDeseos = distribucionDeseos; }

    public BigDecimal getDistribucionAhorros() { return distribucionAhorros; }
    public void setDistribucionAhorros(BigDecimal distribucionAhorros) { this.distribucionAhorros = distribucionAhorros; }

    public String getTipoDistribucion() { return tipoDistribucion; }
    public void setTipoDistribucion(String tipoDistribucion) { this.tipoDistribucion = tipoDistribucion; }

    public String getPrioridad() { return prioridad; }
    public void setPrioridad(String prioridad) { this.prioridad = prioridad; }

    public List<Ingreso> getIngresos() { return ingresos; }
    public void setIngresos(List<Ingreso> ingresos) { this.ingresos = ingresos; }

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = OffsetDateTime.now();
        }
    }
}
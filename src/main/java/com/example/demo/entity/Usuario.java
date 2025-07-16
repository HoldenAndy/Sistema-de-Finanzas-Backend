package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Usuario{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private int edad;

    @Column(nullable = false)
    private LocalDate fechaRegistro;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rol_id", nullable = false)
    private Rol rol;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "usuario"}) // Para evitar bucles con PlanFinanzas
    private List<PlanFinanzas> planesFinanzas;

    public Usuario(int id, String nombre, String email, String password, int edad, LocalDate fechaRegistro, Rol rol) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.edad = edad;
        this.fechaRegistro = fechaRegistro;
        this.rol = rol;
    }

    public Usuario(String nombre, String email, String password, int edad, LocalDate fechaRegistro, Rol rol) {
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.edad = edad;
        this.fechaRegistro = fechaRegistro;
        this.rol = rol;
    }

    public Usuario(String nombre, String email, String password, int edad) {
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.edad = edad;
    }
    
    
    public int getId() {
        return this.id;
    }
    
    public String getNombre() {
        return this.nombre;
    }
    
    public String getEmail() {
        return this.email;
    }
    
    public String getPassword() {
        return this.password;
    }
    
    public int getEdad() {
        return this.edad;
    }
    
    public LocalDate getFechaRegistro() {
        return this.fechaRegistro;
    }
    
    public Rol getRol() {
        return this.rol;
    }
}

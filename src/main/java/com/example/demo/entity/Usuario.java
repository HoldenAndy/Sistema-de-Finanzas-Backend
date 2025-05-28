package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
    private int id;

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
}

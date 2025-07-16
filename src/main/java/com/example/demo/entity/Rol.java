package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Rol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column (unique = true, nullable = false)
    private NombreRol nombre;

    public enum NombreRol {
        ADMIN,
        USER
    }
    

    public int getId() {
        return this.id;
    }
    
    public NombreRol getNombre() {
        return this.nombre;
    }
    
    
    public void setId(int id) {
        this.id = id;
    }
    
    public void setNombre(NombreRol nombre) {
        this.nombre = nombre;
    }
}

package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    private String nombre;
    private String email;
    private String password;
    private Integer edad;
    
    // Constructores
    public RegisterRequest() {}
    
    public RegisterRequest(String nombre, String email, String password, Integer edad) {
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.edad = edad;
    }
    
    // Getters manuales para evitar problemas de Lombok
    public String getNombre() {
        return this.nombre;
    }
    
    public String getEmail() {
        return this.email;
    }
    
    public String getPassword() {
        return this.password;
    }
    
    public Integer getEdad() {
        return this.edad;
    }
}

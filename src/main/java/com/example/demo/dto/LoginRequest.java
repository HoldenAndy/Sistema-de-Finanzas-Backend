package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    private String email;
    private String password;
    
    // Getters manuales para evitar problemas de Lombok
    public String getEmail() {
        return this.email;
    }
    
    public String getPassword() {
        return this.password;
    }
}

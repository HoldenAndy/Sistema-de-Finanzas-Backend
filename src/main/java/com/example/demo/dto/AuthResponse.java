package com.example.demo.dto;

public class AuthResponse {
    private String token;
    
    // Constructor sin parámetros
    public AuthResponse() {}
    
    // Constructor con parámetro
    public AuthResponse(String token) {
        this.token = token;
    }
    
    // Getter
    public String getToken() {
        return this.token;
    }
    
    // Setter
    public void setToken(String token) {
        this.token = token;
    }
}

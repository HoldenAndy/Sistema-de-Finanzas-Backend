package com.example.demo.controller;

import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.entity.Usuario;
import com.example.demo.service.AuthService;
import com.example.demo.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*") // Añadir soporte adicional de CORS a nivel de controlador
public class AuthController {
    private final AuthService authService;
    private final UsuarioService usuarioService;

    public AuthController(AuthService authService, UsuarioService usuarioService){
        this.authService = authService;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> listarTodosUsuarios(){
        List<Usuario> usuarios = usuarioService.listarTodosUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/test")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("Backend is working!");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        try {
            AuthResponse response = authService.login(loginRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        try {
            // Log para debugging
            System.out.println("Datos recibidos para registro:");
            System.out.println("Nombre: " + registerRequest.getNombre());
            System.out.println("Email: " + registerRequest.getEmail());
            System.out.println("Password: " + (registerRequest.getPassword() != null ? "***" : "null"));
            System.out.println("Edad: " + registerRequest.getEdad());
            
            // Crear entidad Usuario usando constructor
            Usuario usuario = new Usuario(
                registerRequest.getNombre(),
                registerRequest.getEmail(),
                registerRequest.getPassword(),
                registerRequest.getEdad() != null ? registerRequest.getEdad() : 18
            );
            
            Usuario newUser = authService.registrarUsuario(usuario);
            return new ResponseEntity<>(newUser, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            System.err.println("Error en registro: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }
    
    @GetMapping("/me")
    public ResponseEntity<Usuario> getCurrentUser() {
        try {
            System.out.println("DEBUG - /me endpoint called");
            
            // Obtener el usuario autenticado desde el contexto de seguridad
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            System.out.println("DEBUG - Authentication: " + authentication);
            
            if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                System.out.println("DEBUG - UserDetails username: " + userDetails.getUsername());
                
                Usuario usuario = usuarioService.buscarPorEmail(userDetails.getUsername());
                if (usuario != null) {
                    System.out.println("DEBUG - Usuario found: " + usuario.getEmail());
                    return ResponseEntity.ok(usuario);
                } else {
                    System.out.println("DEBUG - Usuario not found for email: " + userDetails.getUsername());
                }
            } else {
                System.out.println("DEBUG - Authentication is null or principal is not UserDetails");
                if (authentication != null) {
                    System.out.println("DEBUG - Principal type: " + authentication.getPrincipal().getClass());
                }
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            System.err.println("DEBUG - Exception in /me endpoint: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
    
    // Clase interna para respuestas de error
    public static class ErrorResponse {
        private String message;
        
        public ErrorResponse(String message) {
            this.message = message;
        }
        
        public String getMessage() {
            return message;
        }
        
        public void setMessage(String message) {
            this.message = message;
        }
    }
}

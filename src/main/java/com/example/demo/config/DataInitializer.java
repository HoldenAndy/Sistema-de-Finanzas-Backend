package com.example.demo.config;

import com.example.demo.entity.Rol;
import com.example.demo.entity.Usuario;
import com.example.demo.repository.RolRepository;
import com.example.demo.repository.UsuarioRepository;

import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Component
public class DataInitializer {
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UsuarioRepository usuarioRepository, RolRepository rolRepository, PasswordEncoder passwordEncoder) {

        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        Rol rolAdmin = rolRepository.findByNombre(Rol.NombreRol.ADMIN).orElse(null);
        if (rolAdmin == null) {
            rolAdmin = new Rol();
            rolAdmin.setNombre(Rol.NombreRol.ADMIN);
            rolAdmin = rolRepository.save(rolAdmin);
        }

        
        Rol rolUser = rolRepository.findByNombre(Rol.NombreRol.USER).orElse(null);
        if (rolUser == null) {
            rolUser = new Rol();
            rolUser.setNombre(Rol.NombreRol.USER);
            rolRepository.save(rolUser);
        }

        String emailAdmin = "adminpapu";

        Optional<Usuario> adminExistente = usuarioRepository.findByEmail(emailAdmin);
        if (adminExistente.isEmpty()) {
            Usuario admin = new Usuario(
                "Administrador",
                emailAdmin,
                passwordEncoder.encode("papu123"),
                18,
                LocalDate.now(),
                rolAdmin
            );

            usuarioRepository.save(admin);
            System.out.println("Usuario administrador creado: " + emailAdmin);
        } else {
            System.out.println("Usuario administrador ya existe.");
        }
    }
}


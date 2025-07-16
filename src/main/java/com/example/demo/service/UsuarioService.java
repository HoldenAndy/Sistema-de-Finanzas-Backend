package com.example.demo.service;

import com.example.demo.entity.Usuario;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository){
        this.usuarioRepository = usuarioRepository;
    }

    public Optional<Usuario> findByNombre(String nombreUsuario) {
        return usuarioRepository.findByNombre(nombreUsuario);
    }

    public List<Usuario> listarTodosUsuarios(){
        List<Usuario> usuarios = usuarioRepository.findAll();
        return usuarios;
    }
    
    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email).orElse(null);
    }

    public Optional<Usuario> findUserById(Integer id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> findUserByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public Usuario saveUser(Usuario user) {
        return usuarioRepository.save(user);
    }
}

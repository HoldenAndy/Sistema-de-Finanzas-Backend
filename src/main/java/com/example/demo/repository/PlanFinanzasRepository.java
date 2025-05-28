package com.example.demo.repository;

import com.example.demo.entity.PlanFinanzas;
import com.example.demo.entity.Usuario; // Si necesitas buscar por User
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlanFinanzasRepository extends JpaRepository<PlanFinanzas, Integer> {

    List<PlanFinanzas> findByUsuario(Usuario usuario);

    Optional<PlanFinanzas> findByUsuarioAndEstado(Usuario usuario, String estado);

    Optional<PlanFinanzas> findByUsuario_IdAndEstado(Integer usuarioId, String estado);

    List<PlanFinanzas> findByEstado(String estado);

    List<PlanFinanzas> findByUsuario_IdOrderByCreatedAtDesc(Integer usuarioId);

}
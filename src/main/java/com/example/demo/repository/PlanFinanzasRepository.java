package com.example.demo.repository;

import com.example.demo.entity.PlanFinanzas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlanFinanzasRepository extends JpaRepository<PlanFinanzas, Integer> { // El segundo tipo es el tipo del ID

    // Para listar todos los planes de un usuario por el ID del usuario
    List<PlanFinanzas> findByUsuarioId(Integer userId);

    // Para encontrar un plan por el ID del usuario Y su id_plan (el ID del plan de finanzas)
    // El nombre del método debe coincidir con la estructura de la entidad:
    // usuario.id (el ID del usuario) y id_plan (el ID del plan)
    Optional<PlanFinanzas> findByIdAndUsuario_Id(Integer planId, Integer userId);
    // Para encontrar planes por ID de usuario y por su 'estado' (String)
    List<PlanFinanzas> findByUsuarioIdAndEstado(Integer userId, String estado);

    // Si tenías otros métodos relacionados con fechas o sueldos, aquí es donde irían.
    // Ejemplos de métodos que tenías en tu TXT original que pueden no ser necesarios si los datos no los pasas
    // en la API:
    // Optional<PlanFinanzas> findByUsuario_IdAndEstado(Integer userId, String estado); // Usar findByUsuarioIdAndEstado directamente
    // List<PlanFinanzas> findByUsuario_IdOrderByCreatedAtDesc(Integer userId); // Puedes crear este si lo necesitas
}
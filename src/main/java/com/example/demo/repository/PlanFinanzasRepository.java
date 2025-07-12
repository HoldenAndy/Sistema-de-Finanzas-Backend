package com.example.demo.repository;

import com.example.demo.entity.PlanFinanzas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlanFinanzasRepository extends JpaRepository<PlanFinanzas, Integer> { // El segundo tipo es el tipo del ID

    List<PlanFinanzas> findByUsuarioId(Integer userId);

    Optional<PlanFinanzas> findByIdAndUsuario_Id(Integer planId, Integer userId);

    List<PlanFinanzas> findByUsuarioIdAndEstado(Integer userId, String estado);

}
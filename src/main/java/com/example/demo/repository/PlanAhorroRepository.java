package com.example.demo.repository;

import com.example.demo.entity.PlanAhorro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface PlanAhorroRepository extends JpaRepository<PlanAhorro, Long> {
    List<PlanAhorro> findByIdUsuario(Long idUsuario);

    List<PlanAhorro> findByIdUsuarioAndEstado(Long idUsuario, PlanAhorro.EstadoAhorro estado);

    @Query("SELECT pa FROM PlanAhorro pa WHERE pa.estado = 'ACTIVO' AND pa.fechaInicio <= ?1")
    List<PlanAhorro> findPlanesParaEjecucion(LocalDate fecha);
}

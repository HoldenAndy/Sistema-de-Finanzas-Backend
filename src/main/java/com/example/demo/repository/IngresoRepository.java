package com.example.demo.repository;

import com.example.demo.entity.Ingreso;
import com.example.demo.entity.PlanFinanzas; // Si necesitas buscar por PlanFinanzas
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IngresoRepository extends JpaRepository<Ingreso, Integer> {

    List<Ingreso> findByPlanFinanzas(PlanFinanzas planFinanzas);

    List<Ingreso> findByPlanFinanzasOrderByFechaDesc(PlanFinanzas planFinanzas);

    List<Ingreso> findByTipoAndPlanFinanzas(String tipo, PlanFinanzas planFinanzas);

    @Query("SELECT i FROM Ingreso i WHERE i.planFinanzas.id_plan = :idPlan")
    List<Ingreso> findByPlanFinanzasIdPlan(@Param("idPlan") Integer idPlan);

}
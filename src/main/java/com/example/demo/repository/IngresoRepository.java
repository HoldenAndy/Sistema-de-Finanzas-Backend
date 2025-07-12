package com.example.demo.repository;

import com.example.demo.entity.Ingreso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Repository
public interface IngresoRepository extends JpaRepository<Ingreso, Integer> {

    @Query("SELECT i FROM Ingreso i WHERE i.planFinanzas.usuario.id = :userId AND i.planFinanzas.id = :planId")
    List<Ingreso> findByPlanFinanzas_Usuario_IdAndPlanFinanzas_Id(Integer userId, Integer planId);

    @Query("SELECT new map(FUNCTION('DATE_FORMAT', i.fechaIngreso, '%Y-%m') as mes, SUM(i.monto) as total) " +
            "FROM Ingreso i " +
            "WHERE i.planFinanzas.usuario.id = :userId " +
            "GROUP BY mes " +
            "ORDER BY mes")
    List<Map<String, Object>> findIngresoSummaryByMonthForUser(Integer userId);

    @Query("SELECT new map(i.categoria as categoria, SUM(i.monto) as total) " +
            "FROM Ingreso i " +
            "WHERE i.planFinanzas.usuario.id = :userId " +
            "GROUP BY i.categoria " +
            "ORDER BY total DESC")
    List<Map<String, Object>> findIngresoSummaryByTypeForUser(Integer userId);

    @Query("SELECT SUM(i.monto) FROM Ingreso i WHERE i.planFinanzas.id = :planId")
    BigDecimal sumMontoByPlanFinanzasId(Integer planId);
}
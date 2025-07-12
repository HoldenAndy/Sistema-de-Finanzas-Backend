package com.example.demo.repository;

import com.example.demo.entity.Gasto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface GastoRepository extends JpaRepository<Gasto, Integer> {

    Optional<Gasto> findByIdAndPlanFinanzas_Usuario_Id(Integer gastoId, Integer userId);

    List<Gasto> findByPlanFinanzas_Usuario_IdAndPlanFinanzas_Id(Integer userId, Integer planId);

    List<Gasto> findByPlanFinanzas_Usuario_Id(Integer userId);

    @Query("SELECT new map(FUNCTION('DATE_FORMAT', g.fechaGasto, '%Y-%m') as mes, SUM(g.monto) as total) " +
            "FROM Gasto g " +
            "WHERE g.planFinanzas.usuario.id = :userId " +
            "GROUP BY mes " +
            "ORDER BY mes")
    List<Map<String, Object>> findGastoSummaryByMonthForUser(Integer userId);

    @Query("SELECT new map(g.categoria as categoria, SUM(g.monto) as total) " +
            "FROM Gasto g " +
            "WHERE g.planFinanzas.usuario.id = :userId " +
            "GROUP BY g.categoria " +
            "ORDER BY total DESC")
    List<Map<String, Object>> findGastoSummaryByCategoryForUser(Integer userId);

    List<Gasto> findByPlanFinanzas_Usuario_IdAndFechaGastoBetween(Integer userId, LocalDate startDate, LocalDate endDate);

    @Query("SELECT DISTINCT g.categoria FROM Gasto g WHERE g.planFinanzas.usuario.id = :userId ORDER BY g.categoria")
    List<String> findDistinctCategoriaByPlanFinanzas_Usuario_Id(Integer userId);

    @Query("SELECT SUM(g.monto) FROM Gasto g WHERE g.planFinanzas.id = :planId")
    BigDecimal sumMontoByPlanFinanzasId(Integer planId);

}
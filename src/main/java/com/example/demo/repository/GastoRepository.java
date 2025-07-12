// src/main/java/com/example/demo/repository/GastoRepository.java
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

    // Método para encontrar un Gasto por su ID y el ID del usuario propietario del plan de finanzas
    // Esto asegura que solo el propietario del gasto pueda acceder a él
    Optional<Gasto> findByIdAndPlanFinanzas_Usuario_Id(Integer gastoId, Integer userId);

    // Método para listar todos los gastos de un usuario para un plan de finanzas específico
    List<Gasto> findByPlanFinanzas_Usuario_IdAndPlanFinanzas_Id(Integer userId, Integer planId);

    // Método para listar todos los gastos de un usuario (sin importar el plan)
    // Esto sería útil para el endpoint GET /api/expenses sin planId
    List<Gasto> findByPlanFinanzas_Usuario_Id(Integer userId);

    // Endpoint: GET /api/expenses/summary
    // Resumen de gastos por mes para un usuario específico
    @Query("SELECT new map(FUNCTION('DATE_FORMAT', g.fechaGasto, '%Y-%m') as mes, SUM(g.monto) as total) " +
            "FROM Gasto g " +
            "WHERE g.planFinanzas.usuario.id = :userId " +
            "GROUP BY mes " +
            "ORDER BY mes")
    List<Map<String, Object>> findGastoSummaryByMonthForUser(Integer userId);

    // Endpoint: GET /api/expenses/by-category
    // Resumen de gastos por categoría para un usuario específico
    @Query("SELECT new map(g.categoria as categoria, SUM(g.monto) as total) " +
            "FROM Gasto g " +
            "WHERE g.planFinanzas.usuario.id = :userId " +
            "GROUP BY g.categoria " +
            "ORDER BY total DESC")
    List<Map<String, Object>> findGastoSummaryByCategoryForUser(Integer userId);

    // Endpoint: GET /api/expenses/by-month (similar a summary, pero podría ser más detallado o con filtros)
    // Para simplificar, si el anterior ya da por mes, este podría dar una lista de gastos detallados por mes
    // O si es solo un resumen, el `findGastoSummaryByMonthForUser` ya lo cubre.
    // Si necesitas un listado de objetos Gasto filtrados por mes, sería algo como:
    List<Gasto> findByPlanFinanzas_Usuario_IdAndFechaGastoBetween(Integer userId, LocalDate startDate, LocalDate endDate);

    @Query("SELECT DISTINCT g.categoria FROM Gasto g WHERE g.planFinanzas.usuario.id = :userId ORDER BY g.categoria")
    List<String> findDistinctCategoriaByPlanFinanzas_Usuario_Id(Integer userId);

    @Query("SELECT SUM(g.monto) FROM Gasto g WHERE g.planFinanzas.id = :planId")
    BigDecimal sumMontoByPlanFinanzasId(Integer planId);

}
package com.example.demo.repository;

import com.example.demo.entity.Ingreso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface IngresoRepository extends JpaRepository<Ingreso, Integer> {

    // Método corregido: Ahora se usa 'id' en lugar de 'id_plan'
    @Query("SELECT i FROM Ingreso i WHERE i.planFinanzas.usuario.id = :userId AND i.planFinanzas.id = :planId")
    List<Ingreso> findByPlanFinanzas_Usuario_IdAndPlanFinanzas_Id(Integer userId, Integer planId);

    // Método para obtener el resumen de ingresos por mes (año-mes) para un usuario específico
    // Por ejemplo: [{"mes": "2023-01", "total": 1500.00}, {"mes": "2023-02", "total": 2000.50}]
    @Query("SELECT new map(FUNCTION('DATE_FORMAT', i.fechaIngreso, '%Y-%m') as mes, SUM(i.monto) as total) " +
            "FROM Ingreso i " +
            "WHERE i.planFinanzas.usuario.id = :userId " +
            "GROUP BY mes " +
            "ORDER BY mes")
    List<Map<String, Object>> findIngresoSummaryByMonthForUser(Integer userId);

    // Método para obtener el resumen de ingresos por categoría para un usuario específico
    // Por ejemplo: [{"categoria": "Salario", "total": 3000.00}, {"categoria": "Ventas", "total": 500.00}]
    @Query("SELECT new map(i.categoria as categoria, SUM(i.monto) as total) " +
            "FROM Ingreso i " +
            "WHERE i.planFinanzas.usuario.id = :userId " +
            "GROUP BY i.categoria " +
            "ORDER BY total DESC")
    List<Map<String, Object>> findIngresoSummaryByTypeForUser(Integer userId);
}
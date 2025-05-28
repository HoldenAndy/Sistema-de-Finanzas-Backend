package com.example.demo.service;

import com.example.demo.dto.PlanAhorroResponse;
import com.example.demo.entity.PlanAhorro;
import com.example.demo.predicates.AhorroPredicates;
import com.example.demo.repository.PlanAhorroRepository;
import com.example.demo.transformers.AhorroTransformers;
import com.example.demo.utils.FunctionalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;

@Service
public class PlanAhorroService {

    @Autowired
    private PlanAhorroRepository repository;

    // Crear nuevo plan de ahorro
    public PlanAhorro crearPlan(PlanAhorro plan) {
        return repository.save(plan);
    }

    // Obtener planes por usuario
    public List<PlanAhorroResponse> obtenerPlanesPorUsuario(Long idUsuario) {
        List<PlanAhorro> planes = repository.findByIdUsuario(idUsuario);
        return FunctionalUtils.mapear(planes, AhorroTransformers.TO_PLAN_RESPONSE);
    }

    // Filtrar planes usando predicados
    public List<PlanAhorroResponse> filtrarPlanes(Long idUsuario, Predicate<PlanAhorro> filtro) {
        List<PlanAhorro> planes = repository.findByIdUsuario(idUsuario);
        List<PlanAhorro> planesFiltrados = FunctionalUtils.filtrar(planes, filtro);
        return FunctionalUtils.mapear(planesFiltrados, AhorroTransformers.TO_PLAN_RESPONSE);
    }

    // Obtener planes activos
    public List<PlanAhorroResponse> obtenerPlanesActivos(Long idUsuario) {
        return filtrarPlanes(idUsuario, AhorroPredicates.PLAN_ACTIVO);
    }

    // Obtener planes que deben ejecutarse hoy
    public List<PlanAhorroResponse> obtenerPlanesParaHoy(Long idUsuario) {
        return filtrarPlanes(idUsuario, AhorroPredicates.PLAN_DEBE_EJECUTARSE_HOY);
    }

    // Obtener planes por frecuencia
    public List<PlanAhorroResponse> obtenerPlanesPorFrecuencia(Long idUsuario, PlanAhorro.FrecuenciaAhorro frecuencia) {
        return filtrarPlanes(idUsuario, AhorroPredicates.frecuenciaEs(frecuencia));
    }

    // Procesar ejecución de planes - Programación funcional
    public List<PlanAhorro> procesarEjecucionPlanes() {
        List<PlanAhorro> planesParaEjecutar = repository.findPlanesParaEjecucion(LocalDate.now());
        List<PlanAhorro> planesAEjecutar = FunctionalUtils.filtrar(planesParaEjecutar,
                AhorroPredicates.PLAN_DEBE_EJECUTARSE_HOY);

        // Ejecutar lógica de negocio para cada plan
        FunctionalUtils.forEach(planesAEjecutar, this::ejecutarPlan);

        return planesAEjecutar;
    }

    // Ejecutar plan individual
    private void ejecutarPlan(PlanAhorro plan) {
        // Lógica para ejecutar el plan de ahorro
        // Podría incluir transferencia de fondos, notificaciones, etc.
        System.out.println("Ejecutando plan: " + plan.getNombre() + " - Monto: " + plan.getMontoPeriodico());
    }
}

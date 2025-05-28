package com.example.demo.service;

import com.example.demo.entity.Ingreso;
import com.example.demo.entity.PlanFinanzas;
import com.example.demo.repository.IngresoRepository;
import com.example.demo.repository.PlanFinanzasRepository; // Necesitamos el repo de planes para vincular
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Comparator;
import java.util.stream.Collectors;

@Service
public class IngresoService {

    private final IngresoRepository ingresoRepository;
    private final PlanFinanzasRepository planFinanzasRepository;

    @Autowired
    public IngresoService(IngresoRepository ingresoRepository, PlanFinanzasRepository planFinanzasRepository) {
        this.ingresoRepository = ingresoRepository;
        this.planFinanzasRepository = planFinanzasRepository;
    }

    @Transactional
    public Optional<Ingreso> findById(Integer idIngreso) {
        return ingresoRepository.findById(idIngreso);
    }

    @Transactional
    public Ingreso registrarNuevoIngreso(Integer idPlan, BigDecimal monto, String descripcion, String tipo) {
        Optional<PlanFinanzas> planOptional = planFinanzasRepository.findById(idPlan);
        if (planOptional.isEmpty()) {
            throw new IllegalArgumentException("Plan financiero no encontrado con ID: " + idPlan);
        }

        Ingreso nuevoIngreso = new Ingreso();
        nuevoIngreso.setPlanFinanzas(planOptional.get());
        nuevoIngreso.setMonto(monto);
        nuevoIngreso.setDescripcion(descripcion);
        nuevoIngreso.setTipo(tipo);
        nuevoIngreso.setFecha(LocalDate.now());
        return ingresoRepository.save(nuevoIngreso);
    }

    @Transactional
    public Ingreso updateIngreso(Integer idIngreso, BigDecimal monto, String descripcion) {
        Optional<Ingreso> ingresoOptional = ingresoRepository.findById(idIngreso);
        if (ingresoOptional.isEmpty()) {
            throw new IllegalArgumentException("Ingreso no encontrado con ID: " + idIngreso);
        }

        Ingreso ingresoAActualizar = ingresoOptional.get();
        ingresoAActualizar.setMonto(monto);
        ingresoAActualizar.setDescripcion(descripcion);

        return ingresoRepository.save(ingresoAActualizar);
    }

    @Transactional
    public void deleteIngreso(Integer idIngreso) {

        if (!ingresoRepository.existsById(idIngreso)) {
            throw new IllegalArgumentException("Ingreso no encontrado con ID: " + idIngreso);
        }
        ingresoRepository.deleteById(idIngreso);
    }

    @Transactional(readOnly = true)
    public List<Ingreso> getIngresosByPlanId(Integer idPlan) {
        Optional<PlanFinanzas> planOptional = planFinanzasRepository.findById(idPlan);
        if (planOptional.isEmpty()) {
            return List.of();
        }
        return ingresoRepository.findByPlanFinanzasOrderByFechaDesc(planOptional.get());
    }

    @Transactional(readOnly = true)
    public List<BigDecimal> getUltimosIngresosParaGrafico(Integer userId, int limit) {

        Optional<PlanFinanzas> planActivoOptional = planFinanzasRepository.findByUsuario_IdAndEstado(userId, "activo");

        if (planActivoOptional.isEmpty()) {
            return List.of();
        }

        List<Ingreso> ingresos = ingresoRepository.findByPlanFinanzasOrderByFechaDesc(planActivoOptional.get());

        return ingresos.stream()
                .limit(limit)
                .map(Ingreso::getMonto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BigDecimal getPromedioIngresos(Integer userId) {

        List<PlanFinanzas> ultimosPlanes = planFinanzasRepository.findByUsuario_IdOrderByCreatedAtDesc(userId)
                .stream()
                .limit(6)
                .collect(Collectors.toList());

        BigDecimal totalIngresos = BigDecimal.ZERO;
        int countIngresos = 0;

        for (PlanFinanzas plan : ultimosPlanes) {
            List<Ingreso> ingresosDelPlan = ingresoRepository.findByPlanFinanzas(plan);
            for (Ingreso ingreso : ingresosDelPlan) {
                totalIngresos = totalIngresos.add(ingreso.getMonto());
                countIngresos++;
            }
        }

        if (countIngresos == 0) {
            return BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP);
        }

        return totalIngresos.divide(new BigDecimal(countIngresos), 2, BigDecimal.ROUND_HALF_UP);
    }
}
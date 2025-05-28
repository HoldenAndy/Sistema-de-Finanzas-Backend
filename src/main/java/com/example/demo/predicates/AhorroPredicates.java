package com.example.demo.predicates;

import com.example.demo.entity.AhorroFijo;
import com.example.demo.entity.PlanAhorro;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.function.Predicate;

public class AhorroPredicates {

    // Predicados para AhorroFijo
    public static final Predicate<AhorroFijo> AHORRO_ACTIVO =
            ahorro -> ahorro.getEstado() == AhorroFijo.EstadoAhorro.ACTIVO;

    public static final Predicate<AhorroFijo> AHORRO_COMPLETADO =
            ahorro -> ahorro.estaCompleto();

    public static final Predicate<AhorroFijo> AHORRO_VENCIDO =
            ahorro -> ahorro.getFechaObjetivo().isBefore(LocalDate.now()) && !ahorro.estaCompleto();

    public static Predicate<AhorroFijo> montoMayorA(BigDecimal monto) {
        return ahorro -> ahorro.getMontoActual().compareTo(monto) > 0;
    }

    public static Predicate<AhorroFijo> progresoMayorA(BigDecimal porcentaje) {
        return ahorro -> ahorro.calcularProgreso().compareTo(porcentaje) > 0;
    }

    // Predicados para PlanAhorro
    public static final Predicate<PlanAhorro> PLAN_ACTIVO =
            plan -> plan.getEstado() == PlanAhorro.EstadoAhorro.ACTIVO;

    public static final Predicate<PlanAhorro> PLAN_DEBE_EJECUTARSE_HOY =
            plan -> plan.debeEjecutarseHoy();

    public static Predicate<PlanAhorro> frecuenciaEs(PlanAhorro.FrecuenciaAhorro frecuencia) {
        return plan -> plan.getFrecuencia() == frecuencia;
    }
}

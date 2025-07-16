package com.example.demo.transformers;

import com.example.demo.dto.AhorroFijoResponse;
import com.example.demo.dto.PlanAhorroResponse;
import com.example.demo.entity.AhorroFijo;
import com.example.demo.entity.PlanAhorro;

import java.util.function.Function;

public class AhorroTransformers {

    public static final Function<AhorroFijo, AhorroFijoResponse> TO_RESPONSE =
            ahorro -> new AhorroFijoResponse(
                    ahorro.getIdAhorroFijo(),
                    ahorro.getNombre(),
                    ahorro.getMontoObjetivo(),
                    ahorro.getMontoActual(),
                    ahorro.calcularProgreso(),
                    ahorro.getFechaInicio(),
                    ahorro.getFechaObjetivo(),
                    ahorro.getEstado().toString(),
                    ahorro.estaCompleto()
            );

    public static final Function<AhorroFijo, String> TO_SUMMARY =
            ahorro -> String.format("%s: %s/%s (%.2f%%)",
                    ahorro.getNombre(),
                    ahorro.getMontoActual(),
                    ahorro.getMontoObjetivo(),
                    ahorro.calcularProgreso());

    public static final Function<PlanAhorro, PlanAhorroResponse> TO_PLAN_RESPONSE =
            plan -> new PlanAhorroResponse(
                    plan.getIdPlanAhorro(),
                    plan.getNombre(),
                    plan.getMontoPeriodico(),
                    plan.getFrecuencia().toString(),
                    plan.getDiaEjecucion(),
                    plan.getFechaInicio(),
                    plan.getFechaFin(),
                    plan.getEstado().toString(),
                    plan.calcularProximaEjecucion(),
                    plan.getSueldoBase(),
                    plan.getOtrosIngresos(),
                    plan.getMetaPrincipal(),
                    plan.getMontoObjetivo(),
                    plan.getDistribucionNecesidades(),
                    plan.getDistribucionDeseos(),
                    plan.getDistribucionAhorros(),
                    plan.getTipoDistribucion(),
                    plan.getPrioridad()
            );

}

package com.example.demo.controller;

import com.example.demo.dto.PlanFinanzasDto;
import com.example.demo.dto.PlanFinanzasResponseDto;
import com.example.demo.entity.PlanFinanzas;
import com.example.demo.service.PlanFinanzasService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/plans")
public class PlanFinanzasController {

    private final PlanFinanzasService planFinanzasService;

    public PlanFinanzasController(PlanFinanzasService planFinanzasService) {
        this.planFinanzasService = planFinanzasService;
    }

    @PostMapping
    public ResponseEntity<PlanFinanzas> crearPlanFinanzas(@Valid @RequestBody PlanFinanzasDto planFinanzasDto) {
        PlanFinanzas nuevoPlan = planFinanzasService.crearPlanFinanzas(planFinanzasDto);
        return new ResponseEntity<>(nuevoPlan, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlanFinanzas> actualizarPlanFinanzas(@PathVariable Integer id, @Valid @RequestBody PlanFinanzasDto planFinanzasDto) {
        PlanFinanzas planActualizado = planFinanzasService.actualizarPlanFinanzas(id, planFinanzasDto);
        return new ResponseEntity<>(planActualizado, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPlanFinanzas(@PathVariable Integer id) {
        planFinanzasService.eliminarPlanFinanzas(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlanFinanzasResponseDto> obtenerPlanFinanzasPorId(@PathVariable Integer id) {
        PlanFinanzas plan = planFinanzasService.obtenerPlanFinanzasPorId(id);
        BigDecimal totalIngresos = planFinanzasService.calcularTotalIngresosPorPlan(id);
        BigDecimal totalGastos = planFinanzasService.calcularTotalGastosPorPlan(id);
        BigDecimal saldoActual = planFinanzasService.calcularSaldoTotalPlan(id);

        PlanFinanzasResponseDto responseDto = new PlanFinanzasResponseDto(plan, totalIngresos, totalGastos, saldoActual);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // Modificar este endpoint para devolver una lista de PlanFinanzasResponseDto
    @GetMapping
    public ResponseEntity<List<PlanFinanzasResponseDto>> listarPlanesFinanzasPorUsuario() {
        List<PlanFinanzas> planes = planFinanzasService.listarPlanesFinanzasPorUsuario();

        // Mapear cada PlanFinanzas a un PlanFinanzasResponseDto y calcular sus saldos
        List<PlanFinanzasResponseDto> responseDtos = planes.stream()
                .map(plan -> {
                    BigDecimal totalIngresos = planFinanzasService.calcularTotalIngresosPorPlan(plan.getId());
                    BigDecimal totalGastos = planFinanzasService.calcularTotalGastosPorPlan(plan.getId());
                    BigDecimal saldoActual = planFinanzasService.calcularSaldoTotalPlan(plan.getId());
                    return new PlanFinanzasResponseDto(plan, totalIngresos, totalGastos, saldoActual);
                })
                .collect(Collectors.toList());

        return new ResponseEntity<>(responseDtos, HttpStatus.OK);
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<PlanFinanzas> activarPlanFinanzas(@PathVariable Integer id) {
        PlanFinanzas planActivado = planFinanzasService.activarPlanFinanzas(id);
        return new ResponseEntity<>(planActivado, HttpStatus.OK);
    }

    @GetMapping("/active")
    public ResponseEntity<PlanFinanzas> obtenerPlanFinanzasActivo() {
        PlanFinanzas planActivo = planFinanzasService.obtenerPlanFinanzasActivoPorUsuario();
        return new ResponseEntity<>(planActivo, HttpStatus.OK);
    }

    @GetMapping("/active/stats")
    public ResponseEntity<PlanFinanzasResponseDto> obtenerEstadisticasPlanActivo() {
        PlanFinanzas planActivo = planFinanzasService.obtenerPlanFinanzasActivoPorUsuario();
        BigDecimal totalIngresos = planFinanzasService.calcularTotalIngresosPorPlan(planActivo.getId());
        BigDecimal totalGastos = planFinanzasService.calcularTotalGastosPorPlan(planActivo.getId());
        BigDecimal saldoActual = planFinanzasService.calcularSaldoTotalPlan(planActivo.getId());

        PlanFinanzasResponseDto responseDto = new PlanFinanzasResponseDto(planActivo, totalIngresos, totalGastos, saldoActual);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> contarPlanesPorUsuario() {
        List<PlanFinanzas> planes = planFinanzasService.listarPlanesFinanzasPorUsuario();
        return new ResponseEntity<>(planes.size(), HttpStatus.OK);
    }

}
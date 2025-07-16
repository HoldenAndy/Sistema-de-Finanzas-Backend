package com.example.demo.controller;

import com.example.demo.dto.PlanAhorroRequest;
import com.example.demo.dto.PlanAhorroResponse;
import com.example.demo.entity.PlanAhorro;
import com.example.demo.service.PlanAhorroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/planes-ahorro")
@CrossOrigin(origins = "*")
public class PlanAhorroController {
    @Autowired
    private PlanAhorroService service;

    @GetMapping("/test-auth")
    public ResponseEntity<String> testAuth() {
        return ResponseEntity.ok("Authentication successful! User: " +
            SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @PostMapping
    public ResponseEntity<PlanAhorro> crearPlan(@RequestBody PlanAhorroRequest request) {
        System.out.println("DEBUG - Creating plan for user: " + request.idUsuario());
        System.out.println("DEBUG - Plan data: " + request);
        System.out.println("DEBUG - Authentication: " + SecurityContextHolder.getContext().getAuthentication());

        // Crear plan usando constructor vacío y setters para incluir todos los campos
        PlanAhorro plan = new PlanAhorro();
        plan.setIdUsuario(request.idUsuario());
        plan.setNombre(request.nombre());
        plan.setMontoPeriodico(request.montoPeriodico());
        plan.setFrecuencia(PlanAhorro.FrecuenciaAhorro.valueOf(request.frecuencia().toUpperCase()));
        plan.setDiaEjecucion(request.diaEjecucion() != null ? request.diaEjecucion() : 1); // Valor por defecto si es null
        plan.setFechaInicio(request.fechaInicio());
        plan.setFechaFin(request.fechaFin());
        plan.setEstado(PlanAhorro.EstadoAhorro.ACTIVO); // Establecer estado por defecto
        
        // Agregar los nuevos campos del wizard avanzado
        plan.setSueldoBase(request.sueldoBase());
        plan.setOtrosIngresos(request.otrosIngresos() != null ? request.otrosIngresos() : BigDecimal.ZERO);
        plan.setMetaPrincipal(request.metaPrincipal());
        plan.setMontoObjetivo(request.montoObjetivo());
        plan.setDistribucionNecesidades(request.distribucionNecesidades() != null ? request.distribucionNecesidades() : new BigDecimal("50.00"));
        plan.setDistribucionDeseos(request.distribucionDeseos() != null ? request.distribucionDeseos() : new BigDecimal("30.00"));
        plan.setDistribucionAhorros(request.distribucionAhorros() != null ? request.distribucionAhorros() : new BigDecimal("20.00"));
        plan.setTipoDistribucion(request.tipoDistribucion() != null ? request.tipoDistribucion() : "BALANCED");
        plan.setPrioridad(request.prioridad() != null ? request.prioridad() : "MEDIA");
        
        System.out.println("DEBUG - Plan object before save: " + plan);
        
        PlanAhorro planCreado = service.crearPlan(plan);
        return ResponseEntity.ok(planCreado);
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<PlanAhorroResponse>> obtenerPlanes(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(service.obtenerPlanesPorUsuario(idUsuario));
    }

    @GetMapping("/usuario/{idUsuario}/activos")
    public ResponseEntity<List<PlanAhorroResponse>> obtenerPlanesActivos(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(service.obtenerPlanesActivos(idUsuario));
    }

    @GetMapping("/usuario/{idUsuario}/para-hoy")
    public ResponseEntity<List<PlanAhorroResponse>> obtenerPlanesParaHoy(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(service.obtenerPlanesParaHoy(idUsuario));
    }

    @GetMapping("/usuario/{idUsuario}/frecuencia/{frecuencia}")
    public ResponseEntity<List<PlanAhorroResponse>> obtenerPlanesPorFrecuencia(
            @PathVariable Long idUsuario,
            @PathVariable String frecuencia) {
        PlanAhorro.FrecuenciaAhorro freq = PlanAhorro.FrecuenciaAhorro.valueOf(frecuencia.toUpperCase());
        return ResponseEntity.ok(service.obtenerPlanesPorFrecuencia(idUsuario, freq));
    }

    @PostMapping("/ejecutar-planes")
    public ResponseEntity<List<PlanAhorro>> ejecutarPlanes() {
        List<PlanAhorro> planesEjecutados = service.procesarEjecucionPlanes();
        return ResponseEntity.ok(planesEjecutados);
    }
}

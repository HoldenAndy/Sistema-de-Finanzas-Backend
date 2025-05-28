package com.example.demo.controller;

import com.example.demo.dto.PlanAhorroRequest;
import com.example.demo.dto.PlanAhorroResponse;
import com.example.demo.entity.PlanAhorro;
import com.example.demo.service.PlanAhorroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/planes-ahorro")
@CrossOrigin(origins = "*")
public class PlanAhorroController {
    @Autowired
    private PlanAhorroService service;

    @PostMapping
    public ResponseEntity<PlanAhorro> crearPlan(@RequestBody PlanAhorroRequest request) {
        PlanAhorro plan = new PlanAhorro(
                request.idUsuario(),
                request.nombre(),
                request.montoPeriodico(),
                PlanAhorro.FrecuenciaAhorro.valueOf(request.frecuencia().toUpperCase()),
                request.fechaInicio()
        );
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

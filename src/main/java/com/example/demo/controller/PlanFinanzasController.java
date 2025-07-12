package com.example.demo.controller;

import com.example.demo.dto.PlanFinanzasDto;
import com.example.demo.entity.PlanFinanzas;
import com.example.demo.service.PlanFinanzasService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plans") // Mapeo base para todos los endpoints de planes
public class PlanFinanzasController {

    private final PlanFinanzasService planFinanzasService;
    // private final IngresoService ingresoService; // Elimina esta línea si estaba

    public PlanFinanzasController(PlanFinanzasService planFinanzasService) {
        this.planFinanzasService = planFinanzasService;
        // this.ingresoService = ingresoService; // Elimina esta línea si estaba en el constructor
    }

    // Métodos de PlanFinanzas

    // POST /api/plans
    @PostMapping
    public ResponseEntity<PlanFinanzas> crearPlanFinanzas(@Valid @RequestBody PlanFinanzasDto planFinanzasDto) {
        PlanFinanzas nuevoPlan = planFinanzasService.crearPlanFinanzas(planFinanzasDto);
        return new ResponseEntity<>(nuevoPlan, HttpStatus.CREATED);
    }

    // GET /api/plans
    @GetMapping
    public ResponseEntity<List<PlanFinanzas>> listarPlanesFinanzas() {
        List<PlanFinanzas> planes = planFinanzasService.listarPlanesFinanzasPorUsuario();
        return new ResponseEntity<>(planes, HttpStatus.OK);
    }

    // GET /api/plans/{id}
    @GetMapping("/{id}")
    public ResponseEntity<PlanFinanzas> obtenerPlanFinanzasPorId(@PathVariable Integer id) {
        PlanFinanzas plan = planFinanzasService.obtenerPlanFinanzasPorId(id);
        return new ResponseEntity<>(plan, HttpStatus.OK);
    }

    // PUT /api/plans/{id}
    @PutMapping("/{id}")
    public ResponseEntity<PlanFinanzas> actualizarPlanFinanzas(@PathVariable Integer id, @Valid @RequestBody PlanFinanzasDto planFinanzasDto) {
        PlanFinanzas planActualizado = planFinanzasService.actualizarPlanFinanzas(id, planFinanzasDto);
        return new ResponseEntity<>(planActualizado, HttpStatus.OK);
    }

    // DELETE /api/plans/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPlanFinanzas(@PathVariable Integer id) {
        planFinanzasService.eliminarPlanFinanzas(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // PUT /api/plans/{id}/activate
    @PutMapping("/{id}/activate")
    public ResponseEntity<PlanFinanzas> activarPlanFinanzas(@PathVariable Integer id) {
        PlanFinanzas planActivado = planFinanzasService.activarPlanFinanzas(id);
        return new ResponseEntity<>(planActivado, HttpStatus.OK);
    }

    // GET /api/plans/active
    @GetMapping("/active")
    public ResponseEntity<PlanFinanzas> obtenerPlanFinanzasActivo() {
        PlanFinanzas planActivo = planFinanzasService.obtenerPlanFinanzasActivoPorUsuario();
        return new ResponseEntity<>(planActivo, HttpStatus.OK);
    }

}
package com.example.demo.controller;

import com.example.demo.dto.IngresoDto;
import com.example.demo.entity.Ingreso;
import com.example.demo.entity.PlanFinanzas;
import com.example.demo.entity.Usuario; // Asegúrate de que esta importación esté
import com.example.demo.service.IngresoService;
import com.example.demo.service.PlanFinanzasService;
import com.example.demo.service.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController

@RequestMapping("/api/planes")
public class PlanFinanzasController {

    private final PlanFinanzasService planFinanzasService;
    private final IngresoService ingresoService;
    private final UsuarioService usuarioService;

    @Autowired
    public PlanFinanzasController(PlanFinanzasService planFinanzasService, IngresoService ingresoService, UsuarioService usuarioService) {
        this.planFinanzasService = planFinanzasService;
        this.ingresoService = ingresoService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/usuario/{idUsuario}/activo")
    public ResponseEntity<?> getPlanActivoDelUsuario(@PathVariable Integer idUsuario) {
        try {
            Optional<PlanFinanzas> planActivo = planFinanzasService.getPlanActivoByUserId(idUsuario);

            return planActivo.<ResponseEntity<?>>map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró un plan activo para el usuario."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener el plan activo: " + e.getMessage());
        }
    }

    @GetMapping("/usuario/{idUsuario}/plan/{idPlan}/ingresos")
    public ResponseEntity<?> getIngresosByPlan(@PathVariable Integer idUsuario, @PathVariable Integer idPlan) {
        try {
            Optional<PlanFinanzas> plan = planFinanzasService.getPlanById(idPlan);
            if (plan.isEmpty() || !plan.get().getUsuario().getId().equals(idUsuario)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso denegado. El plan no existe o no pertenece al usuario proporcionado.");
            }

            List<Ingreso> ingresos = ingresoService.getIngresosByPlanId(idPlan);
            if (ingresos.isEmpty()) {
                return ResponseEntity.ok(List.of());
            }
            return ResponseEntity.ok(ingresos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener los ingresos: " + e.getMessage());
        }
    }

    @PostMapping("/usuario/{idUsuario}/plan/{idPlan}/ingresos")
    public ResponseEntity<?> registrarIngreso(@PathVariable Integer idUsuario, @PathVariable Integer idPlan, @RequestBody IngresoDto ingresoDto) {
        try {
            Optional<PlanFinanzas> plan = planFinanzasService.getPlanById(idPlan);
            if (plan.isEmpty() || !plan.get().getUsuario().getId().equals(idUsuario)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso denegado. El plan no existe o no pertenece al usuario proporcionado.");
            }

            if (ingresoDto.getMonto() == null || ingresoDto.getTipo() == null || ingresoDto.getDescripcion() == null) {
                return ResponseEntity.badRequest().body("Monto, tipo y descripción son obligatorios.");
            }

            Ingreso nuevoIngreso = ingresoService.registrarNuevoIngreso(
                    idPlan,
                    ingresoDto.getMonto(),
                    ingresoDto.getDescripcion(),
                    ingresoDto.getTipo()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoIngreso);
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al registrar el ingreso: " + e.getMessage());
        }
    }

    @PutMapping("/usuario/{idUsuario}/ingresos/{idIngreso}")
    public ResponseEntity<?> updateIngreso(@PathVariable Integer idUsuario, @PathVariable Integer idIngreso, @RequestBody IngresoDto ingresoDto) {
        try {
            Optional<Ingreso> ingresoExistente = ingresoService.findById(idIngreso);

            if (ingresoExistente.isEmpty() || !ingresoExistente.get().getPlanFinanzas().getUsuario().getId().equals(idUsuario)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso denegado. El ingreso no existe o no pertenece al usuario proporcionado.");
            }

            if (ingresoDto.getMonto() == null || ingresoDto.getDescripcion() == null) {
                return ResponseEntity.badRequest().body("Monto y descripción son obligatorios para actualizar.");
            }
            Ingreso ingresoActualizado = ingresoService.updateIngreso(
                    idIngreso,
                    ingresoDto.getMonto(),
                    ingresoDto.getDescripcion()
            );
            return ResponseEntity.ok(ingresoActualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar el ingreso: " + e.getMessage());
        }
    }

    @DeleteMapping("/usuario/{idUsuario}/ingresos/{idIngreso}")
    public ResponseEntity<?> deleteIngreso(@PathVariable Integer idUsuario, @PathVariable Integer idIngreso) {
        try {
            Optional<Ingreso> ingresoExistente = ingresoService.findById(idIngreso);
            if (ingresoExistente.isEmpty() || !ingresoExistente.get().getPlanFinanzas().getUsuario().getId().equals(idUsuario)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso denegado. El ingreso no existe o no pertenece al usuario proporcionado.");
            }

            ingresoService.deleteIngreso(idIngreso);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar el ingreso: " + e.getMessage());
        }
    }

    @GetMapping("/usuario/{idUsuario}/ingresos/ultimos/{limit}")
    public ResponseEntity<?> getUltimosIngresosParaGrafico(@PathVariable Integer idUsuario, @PathVariable int limit) {
        try {
            List<BigDecimal> ingresosParaGrafico = ingresoService.getUltimosIngresosParaGrafico(idUsuario, limit);
            return ResponseEntity.ok(ingresosParaGrafico);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener datos para el gráfico: " + e.getMessage());
        }
    }

    @GetMapping("/usuario/{idUsuario}/ingresos/promedio")
    public ResponseEntity<?> getPromedioIngresos(@PathVariable Integer idUsuario) {
        try {
            BigDecimal promedio = ingresoService.getPromedioIngresos(idUsuario);
            return ResponseEntity.ok(Map.of("promedio", promedio));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al calcular el promedio: " + e.getMessage());
        }
    }
}
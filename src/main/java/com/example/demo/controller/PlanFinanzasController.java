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

// *************************************************************************
// ** IMPORTANTE: Elimina estas importaciones de seguridad **
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.security.core.userdetails.UserDetails;
// *************************************************************************

@RestController
// Cambiamos la ruta base para incluir el ID de usuario si es para todos los endpoints
// O puedes añadirlo a cada endpoint individualmente si no todos dependen del ID de usuario en la URL base.
// Para este ejemplo, lo pondremos en los métodos, como hace tu amigo.
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

    // *************************************************************************
    // ** ELIMINAMOS getAuthenticatedUserId() COMPLETAMENTE **
    // *************************************************************************

    // Ejemplo de endpoint sin idUsuario en la URL base
    // Si tu lógica para PlanFinanzasController realmente no necesita un ID de usuario en la URL para crear
    // o para otras operaciones, puedes dejarlo así.
    // Sin embargo, si quieres que la creación de un plan financiero esté asociada a un usuario
    // como en el controlador de tu amigo, deberías pasar el idUsuario en el body o en la URL.
    // Por simplicidad, asumiré que los GETs/POSTs que tenías con un plan específico no necesitan el idUsuario
    // en la URL BASE si el plan ya pertenece a un usuario.
    // Si la creacion de un PlanFinanzas también requiere el idUsuario, lo manejamos en el body
    // o en una ruta como: @PostMapping("/usuario/{idUsuario}")

    @GetMapping("/usuario/{idUsuario}/activo") // Nuevo endpoint: incluye el idUsuario en la URL
    public ResponseEntity<?> getPlanActivoDelUsuario(@PathVariable Integer idUsuario) {
        // El idUsuario ya viene directamente del PathVariable
        try {
            Optional<PlanFinanzas> planActivo = planFinanzasService.getPlanActivoByUserId(idUsuario);

            return planActivo.<ResponseEntity<?>>map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró un plan activo para el usuario."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener el plan activo: " + e.getMessage());
        }
    }

    // Nota: Para los siguientes endpoints, que ya incluyen un {idPlan}, la verificación de propiedad
    // del plan al usuario (plan.get().getUsuario().getId().equals(userId)) es crucial.
    // Aquí el 'userId' viene de la URL.
    @GetMapping("/usuario/{idUsuario}/plan/{idPlan}/ingresos") // Nuevo endpoint: incluye idUsuario
    public ResponseEntity<?> getIngresosByPlan(@PathVariable Integer idUsuario, @PathVariable Integer idPlan) {
        try {
            Optional<PlanFinanzas> plan = planFinanzasService.getPlanById(idPlan);
            // Verifica que el plan exista y que pertenezca al usuario proporcionado en la URL
            if (plan.isEmpty() || !plan.get().getUsuario().getId().equals(idUsuario)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso denegado. El plan no existe o no pertenece al usuario proporcionado.");
            }

            List<Ingreso> ingresos = ingresoService.getIngresosByPlanId(idPlan);
            if (ingresos.isEmpty()) {
                return ResponseEntity.ok(List.of()); // Devuelve una lista vacía si no hay ingresos
            }
            return ResponseEntity.ok(ingresos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener los ingresos: " + e.getMessage());
        }
    }

    @PostMapping("/usuario/{idUsuario}/plan/{idPlan}/ingresos") // Nuevo endpoint: incluye idUsuario
    public ResponseEntity<?> registrarIngreso(@PathVariable Integer idUsuario, @PathVariable Integer idPlan, @RequestBody IngresoDto ingresoDto) {
        try {
            Optional<PlanFinanzas> plan = planFinanzasService.getPlanById(idPlan);
            // Verifica que el plan exista y que pertenezca al usuario proporcionado en la URL
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

    @PutMapping("/usuario/{idUsuario}/ingresos/{idIngreso}") // Nuevo endpoint: incluye idUsuario
    public ResponseEntity<?> updateIngreso(@PathVariable Integer idUsuario, @PathVariable Integer idIngreso, @RequestBody IngresoDto ingresoDto) {
        try {
            Optional<Ingreso> ingresoExistente = ingresoService.findById(idIngreso);

            // Verifica que el ingreso exista y que pertenezca al usuario proporcionado en la URL
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

    @DeleteMapping("/usuario/{idUsuario}/ingresos/{idIngreso}") // Nuevo endpoint: incluye idUsuario
    public ResponseEntity<?> deleteIngreso(@PathVariable Integer idUsuario, @PathVariable Integer idIngreso) {
        try {
            Optional<Ingreso> ingresoExistente = ingresoService.findById(idIngreso);
            // Verifica que el ingreso exista y que pertenezca al usuario proporcionado en la URL
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

    @GetMapping("/usuario/{idUsuario}/ingresos/ultimos/{limit}") // Nuevo endpoint: incluye idUsuario
    public ResponseEntity<?> getUltimosIngresosParaGrafico(@PathVariable Integer idUsuario, @PathVariable int limit) {
        try {
            List<BigDecimal> ingresosParaGrafico = ingresoService.getUltimosIngresosParaGrafico(idUsuario, limit);
            return ResponseEntity.ok(ingresosParaGrafico);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener datos para el gráfico: " + e.getMessage());
        }
    }

    @GetMapping("/usuario/{idUsuario}/ingresos/promedio") // Nuevo endpoint: incluye idUsuario
    public ResponseEntity<?> getPromedioIngresos(@PathVariable Integer idUsuario) {
        try {
            BigDecimal promedio = ingresoService.getPromedioIngresos(idUsuario);
            return ResponseEntity.ok(Map.of("promedio", promedio));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al calcular el promedio: " + e.getMessage());
        }
    }
}
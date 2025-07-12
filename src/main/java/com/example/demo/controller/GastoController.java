// src/main/java/com/example/demo/controller/GastoController.java
package com.example.demo.controller;

import com.example.demo.dto.ActualizarGastoDto;
import com.example.demo.dto.CrearGastoDto;
import com.example.demo.entity.Gasto;
import com.example.demo.service.GastoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/expenses") // Mapeo base para todos los endpoints de gastos
public class GastoController {

    private final GastoService gastoService;

    public GastoController(GastoService gastoService) {
        this.gastoService = gastoService;
    }

    // Endpoint para crear un nuevo gasto
    // POST /api/expenses
    @PostMapping
    public ResponseEntity<Gasto> crearGasto(@Valid @RequestBody CrearGastoDto crearGastoDto) {
        Gasto nuevoGasto = gastoService.crearGasto(crearGastoDto);
        return new ResponseEntity<>(nuevoGasto, HttpStatus.CREATED);
    }

    // Endpoint para listar gastos
    // GET /api/expenses
    // Permite filtrar por planId (opcional)
    @GetMapping
    public ResponseEntity<List<Gasto>> listarGastos(@RequestParam(required = false) Integer planId) {
        List<Gasto> gastos;
        if (planId != null) {
            gastos = gastoService.listarGastosPorUsuarioYPlan(planId);
        } else {
            // Si no se proporciona planId, listar todos los gastos del usuario autenticado
            gastos = gastoService.listarTodosLosGastosDelUsuario();
        }
        return new ResponseEntity<>(gastos, HttpStatus.OK);
    }

    // Endpoint para obtener un gasto específico por ID
    // GET /api/expenses/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Gasto> obtenerGastoPorId(@PathVariable Integer id) {
        Gasto gasto = gastoService.obtenerGastoPorId(id);
        return new ResponseEntity<>(gasto, HttpStatus.OK);
    }

    // Endpoint para actualizar un gasto
    // PUT /api/expenses/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Gasto> actualizarGasto(@PathVariable Integer id, @Valid @RequestBody ActualizarGastoDto actualizarGastoDto) {
        Gasto gastoActualizado = gastoService.actualizarGasto(id, actualizarGastoDto);
        return new ResponseEntity<>(gastoActualizado, HttpStatus.OK);
    }

    // Endpoint para eliminar un gasto
    // DELETE /api/expenses/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarGasto(@PathVariable Integer id) {
        gastoService.eliminarGasto(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content para eliminación exitosa
    }

    // Endpoint para obtener categorías (puedes tener un listado fijo o desde BD)
    // GET /api/expenses/categories
    @GetMapping("/categories")
    public ResponseEntity<List<String>> obtenerCategorias() {
        // ESTA ES LA LÍNEA CLAVE: Llama al método del servicio que obtiene categorías únicas
        List<String> categorias = gastoService.obtenerCategoriasUnicasGastosPorUsuario();
        return new ResponseEntity<>(categorias, HttpStatus.OK);
    }

    // Endpoint para resumen de gastos por mes
    // GET /api/expenses/summary
    @GetMapping("/summary")
    public ResponseEntity<List<Map<String, Object>>> obtenerResumenGastosMensual() {
        List<Map<String, Object>> resumen = gastoService.obtenerResumenGastosMensualPorUsuario();
        return new ResponseEntity<>(resumen, HttpStatus.OK);
    }

    // Endpoint para gastos por categoría
    // GET /api/expenses/by-category
    @GetMapping("/by-category")
    public ResponseEntity<List<Map<String, Object>>> obtenerGastosPorCategoria() {
        List<Map<String, Object>> gastosPorCategoria = gastoService.obtenerGastosPorCategoriaPorUsuario();
        return new ResponseEntity<>(gastosPorCategoria, HttpStatus.OK);
    }

    // Endpoint para gastos por mes (puede ser un listado detallado o un resumen diferente al /summary)
    // GET /api/expenses/by-month?year=2025&month=7
    @GetMapping("/by-month")
    public ResponseEntity<List<Gasto>> obtenerGastosPorMes(
            @RequestParam @Valid @NotNull Integer year,
            @RequestParam @Valid @NotNull Integer month) {
        List<Gasto> gastosPorMes = gastoService.obtenerGastosPorMesParaUsuario(year, month);
        return new ResponseEntity<>(gastosPorMes, HttpStatus.OK);
    }
}
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
@RequestMapping("/api/expenses")
public class GastoController {

    private final GastoService gastoService;

    public GastoController(GastoService gastoService) {
        this.gastoService = gastoService;
    }

    @PostMapping
    public ResponseEntity<Gasto> crearGasto(@Valid @RequestBody CrearGastoDto crearGastoDto) {
        Gasto nuevoGasto = gastoService.crearGasto(crearGastoDto);
        return new ResponseEntity<>(nuevoGasto, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Gasto>> listarGastos(@RequestParam(required = false) Integer planId) {
        List<Gasto> gastos;
        if (planId != null) {
            gastos = gastoService.listarGastosPorUsuarioYPlan(planId);
        } else {
            gastos = gastoService.listarTodosLosGastosDelUsuario();
        }
        return new ResponseEntity<>(gastos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Gasto> obtenerGastoPorId(@PathVariable Integer id) {
        Gasto gasto = gastoService.obtenerGastoPorId(id);
        return new ResponseEntity<>(gasto, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Gasto> actualizarGasto(@PathVariable Integer id, @Valid @RequestBody ActualizarGastoDto actualizarGastoDto) {
        Gasto gastoActualizado = gastoService.actualizarGasto(id, actualizarGastoDto);
        return new ResponseEntity<>(gastoActualizado, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarGasto(@PathVariable Integer id) {
        gastoService.eliminarGasto(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/categories")
    public ResponseEntity<List<String>> obtenerCategorias() {
    
        List<String> categorias = gastoService.obtenerCategoriasUnicasGastosPorUsuario();
        return new ResponseEntity<>(categorias, HttpStatus.OK);
    }

    @GetMapping("/summary")
    public ResponseEntity<List<Map<String, Object>>> obtenerResumenGastosMensual() {
        List<Map<String, Object>> resumen = gastoService.obtenerResumenGastosMensualPorUsuario();
        return new ResponseEntity<>(resumen, HttpStatus.OK);
    }

    @GetMapping("/by-category")
    public ResponseEntity<List<Map<String, Object>>> obtenerGastosPorCategoria() {
        List<Map<String, Object>> gastosPorCategoria = gastoService.obtenerGastosPorCategoriaPorUsuario();
        return new ResponseEntity<>(gastosPorCategoria, HttpStatus.OK);
    }

    @GetMapping("/by-month")
    public ResponseEntity<List<Gasto>> obtenerGastosPorMes(
            @RequestParam @Valid @NotNull Integer year,
            @RequestParam @Valid @NotNull Integer month) {
        List<Gasto> gastosPorMes = gastoService.obtenerGastosPorMesParaUsuario(year, month);
        return new ResponseEntity<>(gastosPorMes, HttpStatus.OK);
    }
}
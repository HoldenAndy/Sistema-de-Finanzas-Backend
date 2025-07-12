package com.example.demo.controller;

import com.example.demo.dto.ActualizarIngresoDto;
import com.example.demo.dto.CrearIngresoDto;
import com.example.demo.entity.Ingreso;
import com.example.demo.service.IngresoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/incomes")
public class IngresoController {

    private final IngresoService ingresoService;

    public IngresoController(IngresoService ingresoService) {
        this.ingresoService = ingresoService;
    }

    @PostMapping
    public ResponseEntity<Ingreso> crearIngreso(@Valid @RequestBody CrearIngresoDto crearIngresoDto) {
        Ingreso nuevoIngreso = ingresoService.crearIngreso(crearIngresoDto);
        return new ResponseEntity<>(nuevoIngreso, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Ingreso>> listarIngresos(@RequestParam(required = false) Integer planId) {
        List<Ingreso> ingresos;
        if (planId != null) {
            ingresos = ingresoService.listarIngresosPorUsuarioYPlan(planId);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(ingresos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ingreso> obtenerIngresoPorId(@PathVariable Integer id) {
        Ingreso ingreso = ingresoService.obtenerIngresoPorId(id);
        return new ResponseEntity<>(ingreso, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ingreso> actualizarIngreso(@PathVariable Integer id, @Valid @RequestBody ActualizarIngresoDto actualizarIngresoDto) {
        Ingreso ingresoActualizado = ingresoService.actualizarIngreso(id, actualizarIngresoDto);
        return new ResponseEntity<>(ingresoActualizado, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarIngreso(@PathVariable Integer id) {
        ingresoService.eliminarIngreso(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
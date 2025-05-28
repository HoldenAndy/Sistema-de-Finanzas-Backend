package com.example.demo.controller;

import com.example.demo.dto.AhorroFijoRequest;
import com.example.demo.dto.AhorroFijoResponse;
import com.example.demo.dto.AporteMonto;
import com.example.demo.entity.AhorroFijo;
import com.example.demo.predicates.AhorroPredicates;
import com.example.demo.service.AhorroFijoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/ahorros-fijos")
@CrossOrigin(origins = "*")
public class AhorroFijoController {

    @Autowired
    private AhorroFijoService service;

    @PostMapping
    public ResponseEntity<AhorroFijo> crearAhorro(@RequestBody AhorroFijoRequest request) {
        AhorroFijo ahorro = new AhorroFijo(
                request.idUsuario(),
                request.nombre(),
                request.montoObjetivo(),
                request.fechaObjetivo()
        );
        AhorroFijo ahorroCreado = service.crearAhorro(ahorro);
        return ResponseEntity.ok(ahorroCreado);
    }
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<AhorroFijoResponse>> obtenerAhorros(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(service.obtenerAhorrosPorUsuario(idUsuario));
    }

    @GetMapping("/usuario/{idUsuario}/activos")
    public ResponseEntity<List<AhorroFijoResponse>> obtenerAhorrosActivos(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(service.obtenerAhorrosActivos(idUsuario));
    }

    @GetMapping("/usuario/{idUsuario}/completados")
    public ResponseEntity<List<AhorroFijoResponse>> obtenerAhorrosCompletados(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(service.obtenerAhorrosCompletados(idUsuario));
    }

    @GetMapping("/usuario/{idUsuario}/mayor-monto/{monto}")
    public ResponseEntity<List<AhorroFijoResponse>> obtenerAhorrosMayorMonto(
            @PathVariable Long idUsuario,
            @PathVariable BigDecimal monto) {
        return ResponseEntity.ok(service.filtrarAhorros(idUsuario, AhorroPredicates.montoMayorA(monto)));
    }

    @PostMapping("/{idAhorro}/agregar-monto")
    public ResponseEntity<AhorroFijo> agregarMonto(
            @PathVariable Long idAhorro,
            @RequestBody AporteMonto aporte) {
        AhorroFijo ahorroActualizado = service.agregarMonto(idAhorro, aporte.monto());
        return ResponseEntity.ok(ahorroActualizado);
    }

    @GetMapping("/usuario/{idUsuario}/total-ahorrado")
    public ResponseEntity<BigDecimal> obtenerTotalAhorrado(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(service.calcularTotalAhorrado(idUsuario));
    }

    @GetMapping("/usuario/{idUsuario}/resumen")
    public ResponseEntity<List<String>> obtenerResumen(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(service.obtenerResumenAhorros(idUsuario));
    }

    @PutMapping("/{idAhorro}/estado/{estado}")
    public ResponseEntity<AhorroFijo> cambiarEstado(
            @PathVariable Long idAhorro,
            @PathVariable String estado) {
        AhorroFijo.EstadoAhorro nuevoEstado = AhorroFijo.EstadoAhorro.valueOf(estado.toUpperCase());
        AhorroFijo ahorroActualizado = service.cambiarEstado(idAhorro, nuevoEstado);
        return ResponseEntity.ok(ahorroActualizado);
    }
}

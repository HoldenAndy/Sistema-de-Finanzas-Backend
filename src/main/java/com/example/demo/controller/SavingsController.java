package com.example.demo.controller;

import com.example.demo.dto.AhorroFijoRequest;
import com.example.demo.dto.AhorroFijoResponse;
import com.example.demo.dto.AporteMonto;
import com.example.demo.entity.AhorroFijo;
import com.example.demo.entity.Usuario;
import com.example.demo.service.AhorroFijoService;
import com.example.demo.service.SavingsService;
import com.example.demo.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/savings")
@CrossOrigin(origins = "*")
public class SavingsController {

    @Autowired
    private AhorroFijoService ahorroFijoService;
    
    @Autowired
    private SavingsService savingsService;
    
    @Autowired
    private UsuarioService usuarioService;

    // Método helper para obtener el usuario autenticado
    private Usuario getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return usuarioService.buscarPorEmail(userDetails.getUsername());
    }

    // GET /api/savings - Listar todos los ahorros del usuario autenticado
    @GetMapping
    public ResponseEntity<List<AhorroFijoResponse>> listarAhorros() {
        Usuario usuario = getAuthenticatedUser();
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<AhorroFijoResponse> ahorros = ahorroFijoService.obtenerAhorrosPorUsuario(Long.valueOf(usuario.getId()));
        return ResponseEntity.ok(ahorros);
    }

    // POST /api/savings - Crear nuevo ahorro
    @PostMapping
    public ResponseEntity<AhorroFijo> crearAhorro(@RequestBody AhorroFijoRequest request) {
        Usuario usuario = getAuthenticatedUser();
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        AhorroFijo ahorro = new AhorroFijo(
                Long.valueOf(usuario.getId()), // Usar el ID del usuario autenticado
                request.nombre(),
                request.montoObjetivo(),
                request.fechaObjetivo()
        );
        AhorroFijo ahorroCreado = ahorroFijoService.crearAhorro(ahorro);
        return ResponseEntity.status(HttpStatus.CREATED).body(ahorroCreado);
    }

    // GET /api/savings/:id - Obtener ahorro específico
    @GetMapping("/{id}")
    public ResponseEntity<AhorroFijoResponse> obtenerAhorro(@PathVariable Long id) {
        Optional<AhorroFijoResponse> ahorro = savingsService.obtenerAhorroPorId(id);
        return ahorro.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // PUT /api/savings/:id - Actualizar ahorro
    @PutMapping("/{id}")
    public ResponseEntity<AhorroFijo> actualizarAhorro(
            @PathVariable Long id, 
            @RequestBody AhorroFijoRequest request) {
        Optional<AhorroFijo> ahorroActualizado = savingsService.actualizarAhorro(id, request);
        return ahorroActualizado.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/savings/:id - Eliminar ahorro
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAhorro(@PathVariable Long id) {
        boolean eliminado = savingsService.eliminarAhorro(id);
        return eliminado ? ResponseEntity.noContent().build() 
                        : ResponseEntity.notFound().build();
    }

    // GET /api/savings/summary - Resumen de ahorros
    @GetMapping("/summary")
    public ResponseEntity<Map<String, Object>> obtenerResumen() {
        Usuario usuario = getAuthenticatedUser();
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Map<String, Object> resumen = savingsService.obtenerResumenAhorros(Long.valueOf(usuario.getId()));
        return ResponseEntity.ok(resumen);
    }

    // GET /api/savings/by-type - Ahorros por tipo
    @GetMapping("/by-type")
    public ResponseEntity<Map<String, Object>> obtenerAhorrosPorTipo() {
        Usuario usuario = getAuthenticatedUser();
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Map<String, Object> ahorrosPorTipo = savingsService.obtenerAhorrosPorTipo(Long.valueOf(usuario.getId()));
        return ResponseEntity.ok(ahorrosPorTipo);
    }

    // GET /api/savings/goals - Metas de ahorro
    @GetMapping("/goals")
    public ResponseEntity<Map<String, Object>> obtenerMetasAhorro() {
        Usuario usuario = getAuthenticatedUser();
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Map<String, Object> metas = savingsService.obtenerMetasAhorro(Long.valueOf(usuario.getId()));
        return ResponseEntity.ok(metas);
    }

    // Endpoints adicionales útiles para el frontend
    @PostMapping("/{id}/add-amount")
    public ResponseEntity<AhorroFijo> agregarMonto(
            @PathVariable Long id,
            @RequestBody AporteMonto aporte) {
        AhorroFijo ahorroActualizado = ahorroFijoService.agregarMonto(id, aporte.monto());
        return ResponseEntity.ok(ahorroActualizado);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<AhorroFijo> cambiarEstado(
            @PathVariable Long id,
            @RequestParam String status) {
        AhorroFijo.EstadoAhorro nuevoEstado = AhorroFijo.EstadoAhorro.valueOf(status.toUpperCase());
        AhorroFijo ahorroActualizado = ahorroFijoService.cambiarEstado(id, nuevoEstado);
        return ResponseEntity.ok(ahorroActualizado);
    }
}

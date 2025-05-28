package com.example.demo.service;

import com.example.demo.dto.AhorroFijoResponse;
import com.example.demo.entity.AhorroFijo;
import com.example.demo.predicates.AhorroPredicates;
import com.example.demo.repository.AhorroFijoRepository;
import com.example.demo.transformers.AhorroTransformers;
import com.example.demo.utils.FunctionalUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Service
@Transactional
public class AhorroFijoService {
    @Autowired
    private AhorroFijoRepository repository;

    // Crear nuevo ahorro - Programación funcional con valores inmutables
    public AhorroFijo crearAhorro(AhorroFijo ahorro) {
        return repository.save(ahorro);
    }

    // Obtener ahorros por usuario usando programación funcional
    public List<AhorroFijoResponse> obtenerAhorrosPorUsuario(Long idUsuario) {
        List<AhorroFijo> ahorros = repository.findByIdUsuario(idUsuario);
        return FunctionalUtils.mapear(ahorros, AhorroTransformers.TO_RESPONSE);
    }

    // Filtrar ahorros usando predicados - Programación lógica
    public List<AhorroFijoResponse> filtrarAhorros(Long idUsuario, Predicate<AhorroFijo> filtro) {
        List<AhorroFijo> ahorros = repository.findByIdUsuario(idUsuario);
        List<AhorroFijo> ahorrosFiltrados = FunctionalUtils.filtrar(ahorros, filtro);
        return FunctionalUtils.mapear(ahorrosFiltrados, AhorroTransformers.TO_RESPONSE);
    }

    // Obtener ahorros activos usando predicados
    public List<AhorroFijoResponse> obtenerAhorrosActivos(Long idUsuario) {
        return filtrarAhorros(idUsuario, AhorroPredicates.AHORRO_ACTIVO);
    }

    // Obtener ahorros completados usando predicados
    public List<AhorroFijoResponse> obtenerAhorrosCompletados(Long idUsuario) {
        return filtrarAhorros(idUsuario, AhorroPredicates.AHORRO_COMPLETADO);
    }

    // Agregar monto - Programación funcional con inmutabilidad
    public AhorroFijo agregarMonto(Long idAhorro, BigDecimal monto) {
        Optional<AhorroFijo> ahorroOpt = repository.findById(idAhorro);
        if (ahorroOpt.isPresent()) {
            AhorroFijo ahorro = ahorroOpt.get();
            AhorroFijo ahorroActualizado = ahorro.agregarMonto(monto);

            // Verificar si se completó el ahorro
            if (ahorroActualizado.estaCompleto()) {
                ahorroActualizado = ahorroActualizado.cambiarEstado(AhorroFijo.EstadoAhorro.COMPLETADO);
            }

            return repository.save(ahorroActualizado);
        }
        throw new RuntimeException("Ahorro no encontrado");
    }

    // Calcular total ahorrado por usuario - Función de orden superior
    public BigDecimal calcularTotalAhorrado(Long idUsuario) {
        List<AhorroFijo> ahorros = repository.findByIdUsuario(idUsuario);
        return FunctionalUtils.reducir(
                FunctionalUtils.mapear(ahorros, AhorroFijo::getMontoActual),
                BigDecimal::add
        ).orElse(BigDecimal.ZERO);
    }

    // Obtener resumen de ahorros - Función de transformación
    public List<String> obtenerResumenAhorros(Long idUsuario) {
        List<AhorroFijo> ahorros = repository.findByIdUsuario(idUsuario);
        return FunctionalUtils.mapear(ahorros, AhorroTransformers.TO_SUMMARY);
    }

    // Cambiar estado de ahorro
    public AhorroFijo cambiarEstado(Long idAhorro, AhorroFijo.EstadoAhorro nuevoEstado) {
        Optional<AhorroFijo> ahorroOpt = repository.findById(idAhorro);
        if (ahorroOpt.isPresent()) {
            AhorroFijo ahorro = ahorroOpt.get();
            AhorroFijo ahorroActualizado = ahorro.cambiarEstado(nuevoEstado);
            return repository.save(ahorroActualizado);
        }
        throw new RuntimeException("Ahorro no encontrado");
    }
}

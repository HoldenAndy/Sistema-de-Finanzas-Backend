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

    public AhorroFijo crearAhorro(AhorroFijo ahorro) {
        return repository.save(ahorro);
    }

    public List<AhorroFijoResponse> obtenerAhorrosPorUsuario(Long idUsuario) {
        List<AhorroFijo> ahorros = repository.findByIdUsuario(idUsuario);
        return FunctionalUtils.mapear(ahorros, AhorroTransformers.TO_RESPONSE);
    }

    public List<AhorroFijoResponse> filtrarAhorros(Long idUsuario, Predicate<AhorroFijo> filtro) {
        List<AhorroFijo> ahorros = repository.findByIdUsuario(idUsuario);
        List<AhorroFijo> ahorrosFiltrados = FunctionalUtils.filtrar(ahorros, filtro);
        return FunctionalUtils.mapear(ahorrosFiltrados, AhorroTransformers.TO_RESPONSE);
    }

    public List<AhorroFijoResponse> obtenerAhorrosActivos(Long idUsuario) {
        return filtrarAhorros(idUsuario, AhorroPredicates.AHORRO_ACTIVO);
    }

    public List<AhorroFijoResponse> obtenerAhorrosCompletados(Long idUsuario) {
        return filtrarAhorros(idUsuario, AhorroPredicates.AHORRO_COMPLETADO);
    }

    public AhorroFijo agregarMonto(Long idAhorro, BigDecimal monto) {
        Optional<AhorroFijo> ahorroOpt = repository.findById(idAhorro);
        if (ahorroOpt.isPresent()) {
            AhorroFijo ahorro = ahorroOpt.get();
            AhorroFijo ahorroActualizado = ahorro.agregarMonto(monto);
            if (ahorroActualizado.estaCompleto()) {
                ahorroActualizado = ahorroActualizado.cambiarEstado(AhorroFijo.EstadoAhorro.COMPLETADO);
            }

            return repository.save(ahorroActualizado);
        }
        throw new RuntimeException("Ahorro no encontrado");
    }

    public BigDecimal calcularTotalAhorrado(Long idUsuario) {
        List<AhorroFijo> ahorros = repository.findByIdUsuario(idUsuario);
        return FunctionalUtils.reducir(
                FunctionalUtils.mapear(ahorros, AhorroFijo::getMontoActual),
                BigDecimal::add
        ).orElse(BigDecimal.ZERO);
    }

    public List<String> obtenerResumenAhorros(Long idUsuario) {
        List<AhorroFijo> ahorros = repository.findByIdUsuario(idUsuario);
        return FunctionalUtils.mapear(ahorros, AhorroTransformers.TO_SUMMARY);
    }

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

package com.example.demo.service;

import com.example.demo.dto.AhorroFijoRequest;
import com.example.demo.dto.AhorroFijoResponse;
import com.example.demo.entity.AhorroFijo;
import com.example.demo.repository.AhorroFijoRepository;
import com.example.demo.transformers.AhorroTransformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SavingsService {

    @Autowired
    private AhorroFijoRepository ahorroFijoRepository;

    // Obtener ahorro específico por ID
    public Optional<AhorroFijoResponse> obtenerAhorroPorId(Long id) {
        Optional<AhorroFijo> ahorro = ahorroFijoRepository.findById(id);
        return ahorro.map(AhorroTransformers.TO_RESPONSE);
    }

    // Actualizar ahorro existente
    public Optional<AhorroFijo> actualizarAhorro(Long id, AhorroFijoRequest request) {
        Optional<AhorroFijo> ahorroExistente = ahorroFijoRepository.findById(id);
        
        if (ahorroExistente.isPresent()) {
            AhorroFijo ahorro = ahorroExistente.get();
            
            // Crear nuevo ahorro con datos actualizados
            AhorroFijo ahorroActualizado = new AhorroFijo.Builder(ahorro)
                    .conMontoActual(ahorro.getMontoActual()) // Mantener monto actual
                    .build();
            
            // Actualizar campos editables (esto requeriría métodos setter o un builder más completo)
            // Por ahora, mantenemos la funcionalidad básica
            return Optional.of(ahorroFijoRepository.save(ahorroActualizado));
        }
        
        return Optional.empty();
    }

    // Eliminar ahorro
    public boolean eliminarAhorro(Long id) {
        if (ahorroFijoRepository.existsById(id)) {
            ahorroFijoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Obtener resumen completo de ahorros
    public Map<String, Object> obtenerResumenAhorros(Long userId) {
        List<AhorroFijo> ahorros = ahorroFijoRepository.findByIdUsuario(userId);
        
        Map<String, Object> resumen = new HashMap<>();
        
        // Estadísticas básicas
        resumen.put("totalAhorros", ahorros.size());
        resumen.put("ahorrosActivos", ahorros.stream()
                .filter(a -> a.getEstado() == AhorroFijo.EstadoAhorro.ACTIVO)
                .count());
        resumen.put("ahorrosCompletados", ahorros.stream()
                .filter(a -> a.getEstado() == AhorroFijo.EstadoAhorro.COMPLETADO)
                .count());
        
        // Montos
        BigDecimal totalObjetivo = ahorros.stream()
                .map(AhorroFijo::getMontoObjetivo)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal totalActual = ahorros.stream()
                .map(AhorroFijo::getMontoActual)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        resumen.put("totalMontoObjetivo", totalObjetivo);
        resumen.put("totalMontoActual", totalActual);
        
        // Progreso general
        BigDecimal progresoGeneral = BigDecimal.ZERO;
        if (totalObjetivo.compareTo(BigDecimal.ZERO) > 0) {
            progresoGeneral = totalActual.divide(totalObjetivo, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
        }
        resumen.put("progresoGeneral", progresoGeneral);
        
        // Próximos a vencer (30 días)
        long proximosAVencer = ahorros.stream()
                .filter(a -> a.getEstado() == AhorroFijo.EstadoAhorro.ACTIVO)
                .filter(a -> a.getFechaObjetivo().isBefore(LocalDate.now().plusDays(30)))
                .count();
        resumen.put("proximosAVencer", proximosAVencer);
        
        return resumen;
    }

    // Obtener ahorros agrupados por tipo/estado
    public Map<String, Object> obtenerAhorrosPorTipo(Long userId) {
        List<AhorroFijo> ahorros = ahorroFijoRepository.findByIdUsuario(userId);
        
        Map<String, Object> resultado = new HashMap<>();
        
        // Agrupar por estado
        Map<String, List<AhorroFijoResponse>> porEstado = ahorros.stream()
                .collect(Collectors.groupingBy(
                        ahorro -> ahorro.getEstado().toString(),
                        Collectors.mapping(AhorroTransformers.TO_RESPONSE, Collectors.toList())
                ));
        
        resultado.put("porEstado", porEstado);
        
        // Agrupar por rango de monto
        Map<String, List<AhorroFijoResponse>> porRangoMonto = new HashMap<>();
        porRangoMonto.put("pequeño (< $1000)", new ArrayList<>());
        porRangoMonto.put("mediano ($1000 - $5000)", new ArrayList<>());
        porRangoMonto.put("grande (> $5000)", new ArrayList<>());
        
        for (AhorroFijo ahorro : ahorros) {
            AhorroFijoResponse response = AhorroTransformers.TO_RESPONSE.apply(ahorro);
            BigDecimal monto = ahorro.getMontoObjetivo();
            
            if (monto.compareTo(BigDecimal.valueOf(1000)) < 0) {
                porRangoMonto.get("pequeño (< $1000)").add(response);
            } else if (monto.compareTo(BigDecimal.valueOf(5000)) <= 0) {
                porRangoMonto.get("mediano ($1000 - $5000)").add(response);
            } else {
                porRangoMonto.get("grande (> $5000)").add(response);
            }
        }
        
        resultado.put("porRangoMonto", porRangoMonto);
        
        return resultado;
    }

    // Obtener metas y estadísticas de progreso
    public Map<String, Object> obtenerMetasAhorro(Long userId) {
        List<AhorroFijo> ahorros = ahorroFijoRepository.findByIdUsuario(userId);
        List<AhorroFijo> ahorrosActivos = ahorros.stream()
                .filter(a -> a.getEstado() == AhorroFijo.EstadoAhorro.ACTIVO)
                .collect(Collectors.toList());
        
        Map<String, Object> metas = new HashMap<>();
        
        // Metas por completar
        List<Map<String, Object>> metasPendientes = ahorrosActivos.stream()
                .map(ahorro -> {
                    Map<String, Object> meta = new HashMap<>();
                    meta.put("id", ahorro.getIdAhorroFijo());
                    meta.put("nombre", ahorro.getNombre());
                    meta.put("montoObjetivo", ahorro.getMontoObjetivo());
                    meta.put("montoActual", ahorro.getMontoActual());
                    meta.put("progreso", ahorro.calcularProgreso());
                    meta.put("fechaObjetivo", ahorro.getFechaObjetivo());
                    
                    // Días restantes
                    long diasRestantes = ChronoUnit.DAYS.between(LocalDate.now(), ahorro.getFechaObjetivo());
                    meta.put("diasRestantes", diasRestantes);
                    
                    // Monto diario necesario
                    BigDecimal montoFaltante = ahorro.getMontoObjetivo().subtract(ahorro.getMontoActual());
                    BigDecimal montoDiario = BigDecimal.ZERO;
                    if (diasRestantes > 0) {
                        montoDiario = montoFaltante.divide(BigDecimal.valueOf(diasRestantes), 2, RoundingMode.HALF_UP);
                    }
                    meta.put("montoDiarioNecesario", montoDiario);
                    
                    return meta;
                })
                .sorted((m1, m2) -> ((LocalDate) m1.get("fechaObjetivo")).compareTo((LocalDate) m2.get("fechaObjetivo")))
                .collect(Collectors.toList());
        
        metas.put("metasPendientes", metasPendientes);
        
        // Estadísticas de cumplimiento
        long metasCompletadas = ahorros.stream()
                .filter(a -> a.getEstado() == AhorroFijo.EstadoAhorro.COMPLETADO)
                .count();
        
        double tasaExito = ahorros.isEmpty() ? 0.0 : 
                (double) metasCompletadas / ahorros.size() * 100;
        
        metas.put("metasCompletadas", metasCompletadas);
        metas.put("totalMetas", ahorros.size());
        metas.put("tasaExito", Math.round(tasaExito * 100.0) / 100.0);
        
        // Próximas metas (próximos 30 días)
        List<Map<String, Object>> proximasMetas = metasPendientes.stream()
                .filter(meta -> ((Long) meta.get("diasRestantes")) <= 30 && ((Long) meta.get("diasRestantes")) > 0)
                .limit(5)
                .collect(Collectors.toList());
        
        metas.put("proximasMetas", proximasMetas);
        
        return metas;
    }
}

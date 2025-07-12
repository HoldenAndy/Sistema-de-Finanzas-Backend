package com.example.demo.service;

import com.example.demo.dto.PlanFinanzasDto;
import com.example.demo.entity.PlanFinanzas;
import com.example.demo.entity.Usuario;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.GastoRepository;
import com.example.demo.repository.IngresoRepository;
import com.example.demo.repository.PlanFinanzasRepository;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class PlanFinanzasService {

    private final PlanFinanzasRepository planFinanzasRepository;
    private final UsuarioRepository usuarioRepository;
    private final IngresoRepository ingresoRepository; // <-- Inyectar
    private final GastoRepository gastoRepository;     // <-- Inyectar

    public PlanFinanzasService(PlanFinanzasRepository planFinanzasRepository,
                               UsuarioRepository usuarioRepository,
                               IngresoRepository ingresoRepository, // <-- Añadir al constructor
                               GastoRepository gastoRepository) {   // <-- Añadir al constructor
        this.planFinanzasRepository = planFinanzasRepository;
        this.usuarioRepository = usuarioRepository;
        this.ingresoRepository = ingresoRepository; // <-- Asignar
        this.gastoRepository = gastoRepository;     // <-- Asignar
    }

    @Transactional
    public PlanFinanzas crearPlanFinanzas(PlanFinanzasDto planFinanzasDto) {
        Usuario usuario = getAuthenticatedUser();

        PlanFinanzas planFinanzas = new PlanFinanzas();
        planFinanzas.setSueldoBase(planFinanzasDto.getSueldoBase());
        planFinanzas.setFechaInicio(planFinanzasDto.getFechaInicio());
        planFinanzas.setFechaFin(planFinanzasDto.getFechaFin());
        planFinanzas.setEstado(planFinanzasDto.getEstado());
        planFinanzas.setUsuario(usuario);

        return planFinanzasRepository.save(planFinanzas);
    }

    @Transactional
    public PlanFinanzas actualizarPlanFinanzas(Integer idPlan, PlanFinanzasDto planFinanzasDto) {
        Usuario usuario = getAuthenticatedUser();

        PlanFinanzas planExistente = planFinanzasRepository.findByIdAndUsuario_Id(idPlan, usuario.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Plan de finanzas no encontrado o no pertenece al usuario autenticado."));

        planExistente.setSueldoBase(planFinanzasDto.getSueldoBase());
        planExistente.setFechaInicio(planFinanzasDto.getFechaInicio());
        planExistente.setFechaFin(planFinanzasDto.getFechaFin());
        planExistente.setEstado(planFinanzasDto.getEstado());

        return planFinanzasRepository.save(planExistente);
    }

    @Transactional
    public void eliminarPlanFinanzas(Integer idPlan) {
        Usuario usuario = getAuthenticatedUser();

        PlanFinanzas planExistente = planFinanzasRepository.findByIdAndUsuario_Id(idPlan, usuario.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Plan de finanzas no encontrado o no pertenece al usuario autenticado."));

        planFinanzasRepository.delete(planExistente);
    }

    @Transactional(readOnly = true)
    public PlanFinanzas obtenerPlanFinanzasPorId(Integer idPlan) {
        Usuario usuario = getAuthenticatedUser();
        return planFinanzasRepository.findByIdAndUsuario_Id(idPlan, usuario.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Plan de finanzas no encontrado o no pertenece al usuario autenticado."));
    }

    @Transactional(readOnly = true)
    public List<PlanFinanzas> listarPlanesFinanzasPorUsuario() {
        Usuario usuario = getAuthenticatedUser();
        return planFinanzasRepository.findByUsuarioId(usuario.getId());
    }

    @Transactional
    public PlanFinanzas activarPlanFinanzas(Integer idPlan) {
        Usuario usuario = getAuthenticatedUser();

        List<PlanFinanzas> planesActivos = planFinanzasRepository.findByUsuarioIdAndEstado(usuario.getId(), "activo");
        for (PlanFinanzas plan : planesActivos) {
            plan.setEstado("inactivo");
            planFinanzasRepository.save(plan);
        }

        PlanFinanzas planAActivar = planFinanzasRepository.findByIdAndUsuario_Id(idPlan, usuario.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Plan de finanzas no encontrado o no pertenece al usuario autenticado."));
        planAActivar.setEstado("activo");
        return planFinanzasRepository.save(planAActivar);
    }

    @Transactional(readOnly = true)
    public PlanFinanzas obtenerPlanFinanzasActivoPorUsuario() {
        Usuario usuario = getAuthenticatedUser();
        return planFinanzasRepository.findByUsuarioIdAndEstado(usuario.getId(), "activo")
                .stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró ningún plan activo para el usuario autenticado."));
    }

    @Transactional(readOnly = true)
    public BigDecimal calcularTotalIngresosPorPlan(Integer planId) {
        // Aseguramos que el plan exista y pertenezca al usuario autenticado
        Usuario usuario = getAuthenticatedUser();
        planFinanzasRepository.findByIdAndUsuario_Id(planId, usuario.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Plan de finanzas no encontrado o no pertenece al usuario autenticado."));

        // Usamos el método de IngresoRepository para sumar montos
        // Necesitarás un método en IngresoRepository para sumar ingresos por planId
        return Optional.ofNullable(ingresoRepository.sumMontoByPlanFinanzasId(planId))
                .orElse(BigDecimal.ZERO); // Si no hay ingresos, devuelve 0
    }

    @Transactional(readOnly = true)
    public BigDecimal calcularTotalGastosPorPlan(Integer planId) {
        // Aseguramos que el plan exista y pertenezca al usuario autenticado
        Usuario usuario = getAuthenticatedUser();
        planFinanzasRepository.findByIdAndUsuario_Id(planId, usuario.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Plan de finanzas no encontrado o no pertenece al usuario autenticado."));

        // Usamos el método de GastoRepository para sumar montos
        // Necesitarás un método en GastoRepository para sumar gastos por planId
        return Optional.ofNullable(gastoRepository.sumMontoByPlanFinanzasId(planId))
                .orElse(BigDecimal.ZERO); // Si no hay gastos, devuelve 0
    }

    @Transactional(readOnly = true)
    public BigDecimal calcularSaldoTotalPlan(Integer planId) {
        BigDecimal totalIngresos = calcularTotalIngresosPorPlan(planId);
        BigDecimal totalGastos = calcularTotalGastosPorPlan(planId);
        return totalIngresos.subtract(totalGastos);
    }

    private Usuario getAuthenticatedUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email;
        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else {
            email = principal.toString();
        }
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con el email: " + email));
    }
}
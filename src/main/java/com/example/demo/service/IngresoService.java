package com.example.demo.service;

import com.example.demo.dto.ActualizarIngresoDto;
import com.example.demo.dto.CrearIngresoDto;
import com.example.demo.entity.Ingreso;
import com.example.demo.entity.PlanFinanzas;
import com.example.demo.entity.Usuario;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.IngresoRepository;
import com.example.demo.repository.PlanFinanzasRepository;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class IngresoService {

    private final IngresoRepository ingresoRepository;
    private final PlanFinanzasRepository planFinanzasRepository;
    private final UsuarioRepository usuarioRepository;

    public IngresoService(IngresoRepository ingresoRepository,
                          PlanFinanzasRepository planFinanzasRepository,
                          UsuarioRepository usuarioRepository) {
        this.ingresoRepository = ingresoRepository;
        this.planFinanzasRepository = planFinanzasRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public Ingreso crearIngreso(CrearIngresoDto crearIngresoDto) {
        Usuario usuario = getAuthenticatedUser();

        PlanFinanzas planFinanzas = planFinanzasRepository.findByIdAndUsuario_Id(crearIngresoDto.getIdPlanFinanzas(), usuario.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Plan de finanzas no encontrado o no pertenece al usuario autenticado."));

        Ingreso ingreso = new Ingreso();
        ingreso.setMonto(crearIngresoDto.getMonto());
        ingreso.setDescripcion(crearIngresoDto.getDescripcion());
        ingreso.setFechaIngreso(crearIngresoDto.getFechaIngreso());
        ingreso.setCategoria(crearIngresoDto.getCategoria());
        ingreso.setPlanFinanzas(planFinanzas);
        ingreso.setTipo(crearIngresoDto.getTipo());

        return ingresoRepository.save(ingreso);
    }

    @Transactional
    public Ingreso actualizarIngreso(Integer id, ActualizarIngresoDto actualizarIngresoDto) {
        Usuario usuario = getAuthenticatedUser();

        Ingreso ingresoExistente = ingresoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ingreso no encontrado con ID: " + id));

        if (!ingresoExistente.getPlanFinanzas().getUsuario().getId().equals(usuario.getId())) {
            throw new ResourceNotFoundException("El ingreso no pertenece al usuario autenticado.");
        }

        ingresoExistente.setMonto(actualizarIngresoDto.getMonto());
        ingresoExistente.setDescripcion(actualizarIngresoDto.getDescripcion());
        ingresoExistente.setFechaIngreso(actualizarIngresoDto.getFechaIngreso());
        ingresoExistente.setCategoria(actualizarIngresoDto.getCategoria());
        ingresoExistente.setTipo(actualizarIngresoDto.getTipo());
        return ingresoRepository.save(ingresoExistente);
    }

    @Transactional
    public void eliminarIngreso(Integer id) {
        Usuario usuario = getAuthenticatedUser();

        Ingreso ingresoExistente = ingresoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ingreso no encontrado con ID: " + id));

        if (!ingresoExistente.getPlanFinanzas().getUsuario().getId().equals(usuario.getId())) {
            throw new ResourceNotFoundException("El ingreso no pertenece al usuario autenticado.");
        }

        ingresoRepository.delete(ingresoExistente);
    }

    @Transactional(readOnly = true)
    public Ingreso obtenerIngresoPorId(Integer id) {
        Usuario usuario = getAuthenticatedUser();

        Ingreso ingreso = ingresoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ingreso no encontrado con ID: " + id));

        if (!ingreso.getPlanFinanzas().getUsuario().getId().equals(usuario.getId())) {
            throw new ResourceNotFoundException("El ingreso no pertenece al usuario autenticado.");
        }
        return ingreso;
    }

    @Transactional(readOnly = true)
    public List<Ingreso> listarIngresosPorUsuarioYPlan(Integer planId) {
        Usuario usuario = getAuthenticatedUser();

        PlanFinanzas planFinanzas = planFinanzasRepository.findByIdAndUsuario_Id(planId, usuario.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Plan de finanzas no encontrado o no pertenece al usuario autenticado."));

        return ingresoRepository.findByPlanFinanzas_Usuario_IdAndPlanFinanzas_Id(usuario.getId(), planId);
    }

    @Transactional(readOnly = true)
    public BigDecimal obtenerResumenTotalIngresosPorUsuario() {
        Usuario usuario = getAuthenticatedUser();
        return ingresoRepository.findTotalIngresosByUserId(usuario.getId())
                .orElse(BigDecimal.ZERO);
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> obtenerIngresosPorTipoPorUsuario() {
        Usuario usuario = getAuthenticatedUser();
        return ingresoRepository.findIngresoSummaryByTypeForUser(usuario.getId());
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
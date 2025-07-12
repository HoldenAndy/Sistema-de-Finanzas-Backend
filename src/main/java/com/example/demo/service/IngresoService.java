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

import java.util.List;
import java.util.Map;
import java.util.Optional; // Asegúrate de importar Optional

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

        // 1. Validar que el plan de finanzas exista y pertenezca al usuario autenticado
        // Método usado: planFinanzasRepository.findByUsuarioIdAndId(Integer userId, Integer id)
        PlanFinanzas planFinanzas = planFinanzasRepository.findByIdAndUsuario_Id(crearIngresoDto.getIdPlanFinanzas(), usuario.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Plan de finanzas no encontrado o no pertenece al usuario autenticado."));

        Ingreso ingreso = new Ingreso();
        ingreso.setMonto(crearIngresoDto.getMonto());
        ingreso.setDescripcion(crearIngresoDto.getDescripcion());
        ingreso.setFechaIngreso(crearIngresoDto.getFechaIngreso());
        ingreso.setCategoria(crearIngresoDto.getCategoria());
        ingreso.setPlanFinanzas(planFinanzas);
        ingreso.setTipo(crearIngresoDto.getTipo());

        // Método usado: ingresoRepository.save(Ingreso ingreso) (de JpaRepository)
        return ingresoRepository.save(ingreso);
    }

    @Transactional
    public Ingreso actualizarIngreso(Integer id, ActualizarIngresoDto actualizarIngresoDto) {
        Usuario usuario = getAuthenticatedUser();

        // 1. Buscar el ingreso existente
        // Método usado: ingresoRepository.findById(Integer id) (de JpaRepository)
        Ingreso ingresoExistente = ingresoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ingreso no encontrado con ID: " + id));

        // 2. Validar que el ingreso pertenezca al usuario autenticado y a uno de sus planes
        if (!ingresoExistente.getPlanFinanzas().getUsuario().getId().equals(usuario.getId())) {
            throw new ResourceNotFoundException("El ingreso no pertenece al usuario autenticado.");
        }

        ingresoExistente.setMonto(actualizarIngresoDto.getMonto());
        ingresoExistente.setDescripcion(actualizarIngresoDto.getDescripcion());
        ingresoExistente.setFechaIngreso(actualizarIngresoDto.getFechaIngreso());
        ingresoExistente.setCategoria(actualizarIngresoDto.getCategoria());
        ingresoExistente.setTipo(actualizarIngresoDto.getTipo());
        // Método usado: ingresoRepository.save(Ingreso ingreso) (de JpaRepository)
        return ingresoRepository.save(ingresoExistente);
    }

    @Transactional
    public void eliminarIngreso(Integer id) {
        Usuario usuario = getAuthenticatedUser();

        // 1. Buscar el ingreso existente
        // Método usado: ingresoRepository.findById(Integer id) (de JpaRepository)
        Ingreso ingresoExistente = ingresoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ingreso no encontrado con ID: " + id));

        // 2. Validar que el ingreso pertenezca al usuario autenticado
        if (!ingresoExistente.getPlanFinanzas().getUsuario().getId().equals(usuario.getId())) {
            throw new ResourceNotFoundException("El ingreso no pertenece al usuario autenticado.");
        }

        // Método usado: ingresoRepository.delete(Ingreso ingreso) (de JpaRepository)
        ingresoRepository.delete(ingresoExistente);
    }

    @Transactional(readOnly = true)
    public Ingreso obtenerIngresoPorId(Integer id) {
        Usuario usuario = getAuthenticatedUser();

        // 1. Buscar el ingreso
        // Método usado: ingresoRepository.findById(Integer id) (de JpaRepository)
        Ingreso ingreso = ingresoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ingreso no encontrado con ID: " + id));

        // 2. Validar que el ingreso pertenezca al usuario autenticado
        if (!ingreso.getPlanFinanzas().getUsuario().getId().equals(usuario.getId())) {
            throw new ResourceNotFoundException("El ingreso no pertenece al usuario autenticado.");
        }
        return ingreso;
    }

    @Transactional(readOnly = true)
    public List<Ingreso> listarIngresosPorUsuarioYPlan(Integer planId) {
        Usuario usuario = getAuthenticatedUser();

        // 1. Verificar que el planId pertenezca al usuario autenticado
        // Método usado: planFinanzasRepository.findByUsuarioIdAndId(Integer userId, Integer id)
        PlanFinanzas planFinanzas = planFinanzasRepository.findByIdAndUsuario_Id(planId, usuario.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Plan de finanzas no encontrado o no pertenece al usuario autenticado."));

        // 2. Listar ingresos para ese plan y usuario
        // Método usado: ingresoRepository.findByPlanFinanzas_Usuario_IdAndPlanFinanzas_Id(Integer userId, Integer planId)
        return ingresoRepository.findByPlanFinanzas_Usuario_IdAndPlanFinanzas_Id(usuario.getId(), planId);
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> obtenerResumenIngresosMensualPorUsuario() {
        Usuario usuario = getAuthenticatedUser();
        // Método usado: ingresoRepository.findIngresoSummaryByMonthForUser(Integer userId)
        return ingresoRepository.findIngresoSummaryByMonthForUser(usuario.getId());
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> obtenerIngresosPorTipoPorUsuario() {
        Usuario usuario = getAuthenticatedUser();
        // Método usado: ingresoRepository.findIngresoSummaryByTypeForUser(Integer userId)
        return ingresoRepository.findIngresoSummaryByTypeForUser(usuario.getId());
    }

    // Método auxiliar para obtener el usuario autenticado
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
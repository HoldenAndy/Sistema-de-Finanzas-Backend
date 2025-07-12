package com.example.demo.service;

import com.example.demo.dto.ActualizarGastoDto;
import com.example.demo.dto.CrearGastoDto;
import com.example.demo.entity.Gasto;
import com.example.demo.entity.PlanFinanzas;
import com.example.demo.entity.Usuario;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.GastoRepository;
import com.example.demo.repository.PlanFinanzasRepository;
import com.example.demo.repository.UsuarioRepository; // Asegúrate de importar UsuarioRepository
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional; // Asegúrate de importar Optional

@Service
public class GastoService {

    private final GastoRepository gastoRepository;
    private final PlanFinanzasRepository planFinanzasRepository;
    private final UsuarioRepository usuarioRepository; // Necesario para getAuthenticatedUser()

    public GastoService(GastoRepository gastoRepository,
                        PlanFinanzasRepository planFinanzasRepository,
                        UsuarioRepository usuarioRepository) {
        this.gastoRepository = gastoRepository;
        this.planFinanzasRepository = planFinanzasRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public Gasto crearGasto(CrearGastoDto crearGastoDto) {
        Usuario usuario = getAuthenticatedUser();

        // 1. Validar que el plan de finanzas exista y pertenezca al usuario autenticado
        // NOTA: Usamos planId, luego userId, según la firma de findByIdAndUsuario_Id
        PlanFinanzas planFinanzas = planFinanzasRepository.findByIdAndUsuario_Id(crearGastoDto.getIdPlanFinanzas(), usuario.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Plan de finanzas no encontrado o no pertenece al usuario autenticado."));

        Gasto gasto = new Gasto();
        gasto.setMonto(crearGastoDto.getMonto());
        gasto.setDescripcion(crearGastoDto.getDescripcion());
        gasto.setFechaGasto(crearGastoDto.getFechaGasto());
        gasto.setCategoria(crearGastoDto.getCategoria());
        gasto.setPlanFinanzas(planFinanzas); // Asignar el plan de finanzas validado

        return gastoRepository.save(gasto);
    }

    @Transactional
    public Gasto actualizarGasto(Integer idGasto, ActualizarGastoDto actualizarGastoDto) {
        Usuario usuario = getAuthenticatedUser();

        // 1. Buscar el gasto existente y validar que pertenezca al usuario autenticado
        Gasto gastoExistente = gastoRepository.findByIdAndPlanFinanzas_Usuario_Id(idGasto, usuario.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Gasto no encontrado o no pertenece al usuario autenticado."));

        // Actualizar campos (solo si no son null en el DTO, para permitir actualizaciones parciales)
        if (actualizarGastoDto.getMonto() != null) {
            gastoExistente.setMonto(actualizarGastoDto.getMonto());
        }
        if (actualizarGastoDto.getDescripcion() != null) {
            gastoExistente.setDescripcion(actualizarGastoDto.getDescripcion());
        }
        if (actualizarGastoDto.getFechaGasto() != null) {
            gastoExistente.setFechaGasto(actualizarGastoDto.getFechaGasto());
        }
        if (actualizarGastoDto.getCategoria() != null) {
            gastoExistente.setCategoria(actualizarGastoDto.getCategoria());
        }

        return gastoRepository.save(gastoExistente);
    }

    @Transactional
    public void eliminarGasto(Integer idGasto) {
        Usuario usuario = getAuthenticatedUser();

        // 1. Buscar el gasto existente y validar que pertenezca al usuario autenticado
        Gasto gastoExistente = gastoRepository.findByIdAndPlanFinanzas_Usuario_Id(idGasto, usuario.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Gasto no encontrado o no pertenece al usuario autenticado."));

        gastoRepository.delete(gastoExistente);
    }

    @Transactional(readOnly = true)
    public Gasto obtenerGastoPorId(Integer idGasto) {
        Usuario usuario = getAuthenticatedUser();

        // 1. Buscar el gasto y validar que pertenezca al usuario autenticado
        return gastoRepository.findByIdAndPlanFinanzas_Usuario_Id(idGasto, usuario.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Gasto no encontrado o no pertenece al usuario autenticado."));
    }

    @Transactional(readOnly = true)
    public List<Gasto> listarGastosPorUsuarioYPlan(Integer planId) {
        Usuario usuario = getAuthenticatedUser();

        // 1. Verificar que el planId pertenezca al usuario autenticado
        PlanFinanzas planFinanzas = planFinanzasRepository.findByIdAndUsuario_Id(planId, usuario.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Plan de finanzas no encontrado o no pertenece al usuario autenticado."));

        // 2. Listar gastos para ese plan y usuario
        return gastoRepository.findByPlanFinanzas_Usuario_IdAndPlanFinanzas_Id(usuario.getId(), planId);
    }

    @Transactional(readOnly = true)
    public List<Gasto> listarTodosLosGastosDelUsuario() {
        Usuario usuario = getAuthenticatedUser();
        return gastoRepository.findByPlanFinanzas_Usuario_Id(usuario.getId());
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> obtenerResumenGastosMensualPorUsuario() {
        Usuario usuario = getAuthenticatedUser();
        return gastoRepository.findGastoSummaryByMonthForUser(usuario.getId());
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> obtenerGastosPorCategoriaPorUsuario() {
        Usuario usuario = getAuthenticatedUser();
        return gastoRepository.findGastoSummaryByCategoryForUser(usuario.getId());
    }

    @Transactional(readOnly = true)
    public List<String> obtenerCategoriasUnicasGastosPorUsuario() {
        Usuario usuario = getAuthenticatedUser(); // Obtiene el usuario autenticado
        // Llama al método del repositorio diseñado para categorías únicas
        return gastoRepository.findDistinctCategoriaByPlanFinanzas_Usuario_Id(usuario.getId());
    }

    // Puedes añadir un método para obtener gastos por un rango de fechas si es necesario para /by-month
    @Transactional(readOnly = true)
    public List<Gasto> obtenerGastosPorMesParaUsuario(int year, int month) {
        Usuario usuario = getAuthenticatedUser();
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        return gastoRepository.findByPlanFinanzas_Usuario_IdAndFechaGastoBetween(usuario.getId(), startDate, endDate);
    }

    // Método auxiliar para obtener el usuario autenticado (EXISTENTE EN TU PROYECTO)
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
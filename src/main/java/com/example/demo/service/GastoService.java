package com.example.demo.service;

import com.example.demo.dto.ActualizarGastoDto;
import com.example.demo.dto.CrearGastoDto;
import com.example.demo.entity.Gasto;
import com.example.demo.entity.PlanFinanzas;
import com.example.demo.entity.Usuario;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.GastoRepository;
import com.example.demo.repository.PlanFinanzasRepository;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class GastoService {

    private final GastoRepository gastoRepository;
    private final PlanFinanzasRepository planFinanzasRepository;
    private final UsuarioRepository usuarioRepository;

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

        PlanFinanzas planFinanzas = planFinanzasRepository.findByIdAndUsuario_Id(crearGastoDto.getIdPlanFinanzas(), usuario.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Plan de finanzas no encontrado o no pertenece al usuario autenticado."));

        Gasto gasto = new Gasto();
        gasto.setMonto(crearGastoDto.getMonto());
        gasto.setDescripcion(crearGastoDto.getDescripcion());
        gasto.setFechaGasto(crearGastoDto.getFechaGasto());
        gasto.setCategoria(crearGastoDto.getCategoria());
        gasto.setPlanFinanzas(planFinanzas);

        return gastoRepository.save(gasto);
    }

    @Transactional
    public Gasto actualizarGasto(Integer idGasto, ActualizarGastoDto actualizarGastoDto) {
        Usuario usuario = getAuthenticatedUser();

        Gasto gastoExistente = gastoRepository.findByIdAndPlanFinanzas_Usuario_Id(idGasto, usuario.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Gasto no encontrado o no pertenece al usuario autenticado."));

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

        Gasto gastoExistente = gastoRepository.findByIdAndPlanFinanzas_Usuario_Id(idGasto, usuario.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Gasto no encontrado o no pertenece al usuario autenticado."));

        gastoRepository.delete(gastoExistente);
    }

    @Transactional(readOnly = true)
    public Gasto obtenerGastoPorId(Integer idGasto) {
        Usuario usuario = getAuthenticatedUser();

        return gastoRepository.findByIdAndPlanFinanzas_Usuario_Id(idGasto, usuario.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Gasto no encontrado o no pertenece al usuario autenticado."));
    }

    @Transactional(readOnly = true)
    public List<Gasto> listarGastosPorUsuarioYPlan(Integer planId) {
        Usuario usuario = getAuthenticatedUser();

        PlanFinanzas planFinanzas = planFinanzasRepository.findByIdAndUsuario_Id(planId, usuario.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Plan de finanzas no encontrado o no pertenece al usuario autenticado."));

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
        return gastoRepository.findDistinctCategoriaByPlanFinanzas_Usuario_Id(usuario.getId());
    }

    @Transactional(readOnly = true)
    public List<Gasto> obtenerGastosPorMesParaUsuario(int year, int month) {
        Usuario usuario = getAuthenticatedUser();
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        return gastoRepository.findByPlanFinanzas_Usuario_IdAndFechaGastoBetween(usuario.getId(), startDate, endDate);
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
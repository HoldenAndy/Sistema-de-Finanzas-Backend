package com.example.demo.service;

import com.example.demo.entity.PlanFinanzas;
import com.example.demo.entity.Usuario;
import com.example.demo.repository.PlanFinanzasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Para manejar transacciones

import java.util.List;
import java.util.Optional;

@Service
public class PlanFinanzasService {

    private final PlanFinanzasRepository planFinanzasRepository;
    private final UsuarioService userService;
    @Autowired
    public PlanFinanzasService(PlanFinanzasRepository planFinanzasRepository, UsuarioService userService) {
        this.planFinanzasRepository = planFinanzasRepository;
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    public Optional<PlanFinanzas> getPlanActivoByUserId(Integer userId) {


        Optional<Usuario> userOptional = userService.findUserById(userId);
        if (userOptional.isEmpty()) {
            return Optional.empty(); // Usuario no encontrado
        }
        Usuario user = userOptional.get();
        return planFinanzasRepository.findByUsuarioAndEstado(user, "activo");

    }

    @Transactional(readOnly = true)
    public List<PlanFinanzas> getAllPlanesByUserId(Integer userId) {
        Optional<Usuario> userOptional = userService.findUserById(userId);
        if (userOptional.isEmpty()) {
            return List.of();
        }
        Usuario user = userOptional.get();
        return planFinanzasRepository.findByUsuario(user);
    }

    @Transactional
    public PlanFinanzas savePlan(PlanFinanzas plan) {
        return planFinanzasRepository.save(plan);
    }

    @Transactional
    public void deletePlan(Integer planId) {
        planFinanzasRepository.deleteById(planId);
    }

    @Transactional(readOnly = true)
    public Optional<PlanFinanzas> getPlanById(Integer id) {
        return planFinanzasRepository.findById(id);
    }

}
package com.example.demo.repository;

import com.example.demo.entity.AhorroFijo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AhorroFijoRepository extends JpaRepository<AhorroFijo, Long> {
    List<AhorroFijo> findByIdUsuario(Long idUsuario);

    List<AhorroFijo> findByIdUsuarioAndEstado(Long idUsuario, AhorroFijo.EstadoAhorro estado);

    @Query("SELECT af FROM AhorroFijo af WHERE af.idUsuario = ?1 AND af.estado = 'ACTIVO'")
    List<AhorroFijo> findAhorrosActivosByUsuario(Long idUsuario);

    @Query("SELECT af FROM AhorroFijo af WHERE af.montoActual >= af.montoObjetivo")
    List<AhorroFijo> findAhorrosCompletados();
}

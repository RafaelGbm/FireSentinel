package com.global_solution.fire_sentinel_App.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.global_solution.fire_sentinel_App.model.Risco;

@Repository
public interface RiscoRepository extends JpaRepository<Risco, Long> {
    
    Optional<Risco> findTopBySensorIdOrderByDataHoraAnaliseDesc(Long sensorId);
    
    List<Risco> findBySensorIdOrderByDataHoraAnaliseDesc(Long sensorId);
    
    List<Risco> findByDataHoraAnaliseBetween(LocalDateTime inicio, LocalDateTime fim);
    
    List<Risco> findByCategoriaAndDataHoraAnaliseBetween(String categoria, LocalDateTime inicio, LocalDateTime fim);
}

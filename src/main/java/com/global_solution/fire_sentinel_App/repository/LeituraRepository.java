package com.global_solution.fire_sentinel_App.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.global_solution.fire_sentinel_App.model.Leitura;

@Repository
public interface LeituraRepository extends JpaRepository<Leitura, Long> {
    
    List<Leitura> findBySensorIdOrderByDataHoraDesc(Long sensorId);
    
    List<Leitura> findByDataHoraBetween(LocalDateTime inicio, LocalDateTime fim);
    
    List<Leitura> findBySensorIdAndDataHoraBetween(Long sensorId, LocalDateTime inicio, LocalDateTime fim);
}

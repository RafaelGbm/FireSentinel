package com.global_solution.fire_sentinel_App.service;

import java.util.List;
import java.util.Optional;

import com.global_solution.fire_sentinel_App.dto.LeituraDTO;
import com.global_solution.fire_sentinel_App.model.Leitura;

public interface LeituraService {
    
    Leitura registrarLeitura(LeituraDTO leituraDTO);
    
    List<Leitura> listarTodas();
    
    Optional<Leitura> buscarPorId(Long id);
    
    List<Leitura> buscarPorSensor(Long sensorId);
    
    List<Leitura> obterUltimasLeiturasPorSensor();
}

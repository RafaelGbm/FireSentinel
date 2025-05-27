package com.global_solution.fire_sentinel_App.service;

import java.util.List;
import java.util.Optional;

import com.global_solution.fire_sentinel_App.dto.LeituraDTO;
import com.global_solution.fire_sentinel_App.dto.RiscoDTO;
import com.global_solution.fire_sentinel_App.model.Risco;

public interface RiscoService {
    
    RiscoDTO analisarRisco(LeituraDTO leituraDTO);
    
    Optional<RiscoDTO> obterUltimoRiscoPorSensor(Long sensorId);
    
    List<Risco> obterHistoricoRiscoPorSensor(Long sensorId);
    
    List<RiscoDTO> obterRiscosPorArea(Double latitude, Double longitude, Double raio);
}

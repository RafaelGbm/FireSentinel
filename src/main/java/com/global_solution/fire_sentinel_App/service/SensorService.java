package com.global_solution.fire_sentinel_App.service;

import java.util.List;
import java.util.Optional;

import com.global_solution.fire_sentinel_App.dto.SensorDTO;
import com.global_solution.fire_sentinel_App.model.Sensor;

public interface SensorService {
    
    Sensor cadastrarSensor(SensorDTO sensorDTO);
    
    List<Sensor> listarTodos();
    
    Optional<Sensor> buscarPorId(Long id);
    
    Optional<Sensor> atualizarSensor(Long id, SensorDTO sensorDTO);
    
    boolean removerSensor(Long id);
}

package com.global_solution.fire_sentinel_App.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.global_solution.fire_sentinel_App.dto.SensorDTO;
import com.global_solution.fire_sentinel_App.model.Sensor;
import com.global_solution.fire_sentinel_App.repository.SensorRepository;
import com.global_solution.fire_sentinel_App.service.SensorService;

@Service
public class SensorServiceImpl implements SensorService {
    
    @Autowired
    private SensorRepository sensorRepository;
    
    @Override
    public Sensor cadastrarSensor(SensorDTO sensorDTO) {
        Sensor sensor = new Sensor();
        sensor.setNome(sensorDTO.getNome());
        sensor.setTipo(sensorDTO.getTipo());
        sensor.setLocalizacao(sensorDTO.getLocalizacao());
        sensor.setLatitude(sensorDTO.getLatitude());
        sensor.setLongitude(sensorDTO.getLongitude());
        sensor.setAtivo(sensorDTO.isAtivo());
        
        return sensorRepository.save(sensor);
    }
    
    @Override
    public List<Sensor> listarTodos() {
        return sensorRepository.findAll();
    }
    
    @Override
    public Optional<Sensor> buscarPorId(Long id) {
        return sensorRepository.findById(id);
    }
    
    @Override
    public Optional<Sensor> atualizarSensor(Long id, SensorDTO sensorDTO) {
        Optional<Sensor> sensorExistente = sensorRepository.findById(id);
        
        if (sensorExistente.isPresent()) {
            Sensor sensor = sensorExistente.get();
            sensor.setNome(sensorDTO.getNome());
            sensor.setTipo(sensorDTO.getTipo());
            sensor.setLocalizacao(sensorDTO.getLocalizacao());
            sensor.setLatitude(sensorDTO.getLatitude());
            sensor.setLongitude(sensorDTO.getLongitude());
            sensor.setAtivo(sensorDTO.isAtivo());
            
            Sensor sensorAtualizado = sensorRepository.save(sensor);
            return Optional.of(sensorAtualizado);
        }
        
        return Optional.empty();
    }
    
    @Override
    public boolean removerSensor(Long id) {
        if (sensorRepository.existsById(id)) {
            sensorRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

package com.global_solution.fire_sentinel_App.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.global_solution.fire_sentinel_App.dto.LeituraDTO;
import com.global_solution.fire_sentinel_App.model.Leitura;
import com.global_solution.fire_sentinel_App.model.Sensor;
import com.global_solution.fire_sentinel_App.repository.LeituraRepository;
import com.global_solution.fire_sentinel_App.repository.SensorRepository;
import com.global_solution.fire_sentinel_App.service.LeituraService;
import com.global_solution.fire_sentinel_App.service.RiscoService;

@Service
public class LeituraServiceImpl implements LeituraService {

    @Autowired
    private LeituraRepository leituraRepository;
    
    @Autowired
    private SensorRepository sensorRepository;
    
    @Autowired
    private RiscoService riscoService;
    
    @Override
    public Leitura registrarLeitura(LeituraDTO leituraDTO) {
        Optional<Sensor> sensorOptional = sensorRepository.findById(leituraDTO.getSensorId());
        if (sensorOptional.isEmpty()) {
            throw new IllegalArgumentException("Sensor não encontrado com ID: " + leituraDTO.getSensorId());
        }
        
        Leitura leitura = new Leitura();
        leitura.setTemperatura(leituraDTO.getTemperatura());
        leitura.setUmidade(leituraDTO.getUmidade());
        leitura.setNivelFumaca(leituraDTO.getNivelFumaca());
        leitura.setCo2(leituraDTO.getCo2());
        leitura.setDataHora(leituraDTO.getDataHora() != null ? leituraDTO.getDataHora() : LocalDateTime.now());
        leitura.setSensor(sensorOptional.get());
        
        Leitura leituraSalva = leituraRepository.save(leitura);
        
        // Após registrar a leitura, analisar o risco automaticamente
        riscoService.analisarRisco(leituraDTO);
        
        return leituraSalva;
    }

    @Override
    public List<Leitura> listarTodas() {
        return leituraRepository.findAll();
    }

    @Override
    public Optional<Leitura> buscarPorId(Long id) {
        return leituraRepository.findById(id);
    }

    @Override
    public List<Leitura> buscarPorSensor(Long sensorId) {
        return leituraRepository.findBySensorIdOrderByDataHoraDesc(sensorId);
    }

    @Override
    public List<Leitura> obterUltimasLeiturasPorSensor() {
        List<Leitura> todasLeituras = leituraRepository.findAll();
        
        // Agrupa as leituras por sensor e pega a mais recente de cada
        Map<Sensor, List<Leitura>> leiturasPorSensor = todasLeituras.stream()
                .collect(Collectors.groupingBy(Leitura::getSensor));
        
        List<Leitura> ultimasLeituras = new ArrayList<>();
        
        leiturasPorSensor.forEach((sensor, leituras) -> {
            leituras.stream()
                    .sorted((l1, l2) -> l2.getDataHora().compareTo(l1.getDataHora()))
                    .findFirst()
                    .ifPresent(ultimasLeituras::add);
        });
        
        return ultimasLeituras;
    }
}

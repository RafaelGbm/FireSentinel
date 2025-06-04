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

/**
 * Implementação do serviço de gerenciamento de leituras de sensores.
 * Responsável por processar e armazenar as leituras dos sensores do sistema.
 * 
 * Esta implementação fornece:
 * - Registro de novas leituras
 * - Consulta de histórico
 * - Análise automática de risco
 * - Processamento de dados em tempo real
 * - Integração com serviço de risco
 */
@Service
public class LeituraServiceImpl implements LeituraService {

    @Autowired
    private LeituraRepository leituraRepository;
    
    @Autowired
    private SensorRepository sensorRepository;
    
    @Autowired
    private RiscoService riscoService;
    
    /**
     * Registra uma nova leitura de sensor no sistema.
     * Valida o sensor, persiste a leitura e dispara análise de risco.
     *
     * @param leituraDTO DTO contendo os dados da leitura
     * @return Leitura entidade da leitura registrada
     * @throws IllegalArgumentException se o sensor não for encontrado
     */
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

    /**
     * Lista todas as leituras registradas no sistema.
     *
     * @return List<Leitura> lista com todas as leituras
     */
    @Override
    public List<Leitura> listarTodas() {
        return leituraRepository.findAll();
    }

    /**
     * Busca uma leitura específica pelo seu ID.
     *
     * @param id ID da leitura a ser buscada
     * @return Optional<Leitura> contendo a leitura se encontrada
     */
    @Override
    public Optional<Leitura> buscarPorId(Long id) {
        return leituraRepository.findById(id);
    }

    /**
     * Busca todas as leituras de um sensor específico.
     * As leituras são ordenadas por data/hora em ordem decrescente.
     *
     * @param sensorId ID do sensor para buscar as leituras
     * @return List<Leitura> lista com as leituras do sensor
     */
    @Override
    public List<Leitura> buscarPorSensor(Long sensorId) {
        return leituraRepository.findBySensorIdOrderByDataHoraDesc(sensorId);
    }

    /**
     * Obtém a última leitura registrada para cada sensor ativo.
     * Útil para monitoramento em tempo real do estado dos sensores.
     *
     * O método:
     * 1. Busca todas as leituras
     * 2. Agrupa por sensor
     * 3. Seleciona a leitura mais recente de cada grupo
     *
     * @return List<Leitura> lista com a última leitura de cada sensor
     */
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

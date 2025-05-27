package com.global_solution.fire_sentinel_App.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.global_solution.fire_sentinel_App.dto.LeituraDTO;
import com.global_solution.fire_sentinel_App.dto.RiscoDTO;
import com.global_solution.fire_sentinel_App.model.Leitura;
import com.global_solution.fire_sentinel_App.model.Risco;
import com.global_solution.fire_sentinel_App.model.Sensor;
import com.global_solution.fire_sentinel_App.repository.LeituraRepository;
import com.global_solution.fire_sentinel_App.repository.RiscoRepository;
import com.global_solution.fire_sentinel_App.repository.SensorRepository;
import com.global_solution.fire_sentinel_App.service.RiscoService;

@Service
public class RiscoServiceImpl implements RiscoService {

    @Autowired
    private RiscoRepository riscoRepository;
    
    @Autowired
    private LeituraRepository leituraRepository;
    
    @Autowired
    private SensorRepository sensorRepository;
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Value("${ia.risk.analysis.url}")
    private String iaRiskAnalysisUrl;
    
    @Override
    public RiscoDTO analisarRisco(LeituraDTO leituraDTO) {
        // Buscar a leitura e o sensor
        Optional<Leitura> leituraOptional = leituraRepository.findById(leituraDTO.getId());
        Optional<Sensor> sensorOptional = sensorRepository.findById(leituraDTO.getSensorId());
        
        if (leituraOptional.isEmpty() || sensorOptional.isEmpty()) {
            throw new IllegalArgumentException("Leitura ou Sensor não encontrado");
        }
        
        Leitura leitura = leituraOptional.get();
        Sensor sensor = sensorOptional.get();
        
        // Chamar API da IA para análise de risco
        RiscoDTO riscoDTO = chamarAPIRiscoIA(leituraDTO);
        
        // Persistir o resultado da análise
        Risco risco = new Risco();
        risco.setNivelRisco(riscoDTO.getNivelRisco());
        risco.setCategoria(riscoDTO.getCategoria());
        risco.setDescricao(riscoDTO.getDescricao());
        risco.setDataHoraAnalise(LocalDateTime.now());
        risco.setLeitura(leitura);
        risco.setSensor(sensor);
        
        Risco riscoSalvo = riscoRepository.save(risco);
        
        // Atualizar o DTO com o ID salvo
        riscoDTO.setId(riscoSalvo.getId());
        
        return riscoDTO;
    }
    
    private RiscoDTO chamarAPIRiscoIA(LeituraDTO leituraDTO) {
        try {
            // Configurar cabeçalhos para a requisição HTTP
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            // Criar a entidade HTTP com os dados da leitura
            HttpEntity<LeituraDTO> requestEntity = new HttpEntity<>(leituraDTO, headers);
            
            // Chamar a API de IA para análise de risco
            RiscoDTO riscoAnalise = restTemplate.postForObject(iaRiskAnalysisUrl, requestEntity, RiscoDTO.class);
            
            if (riscoAnalise == null) {
                // Em caso de falha na API, criar uma análise padrão para não quebrar o fluxo
                riscoAnalise = new RiscoDTO();
                riscoAnalise.setNivelRisco(0.0);
                riscoAnalise.setCategoria("INDETERMINADO");
                riscoAnalise.setDescricao("Não foi possível analisar o risco. API indisponível.");
                riscoAnalise.setDataHoraAnalise(LocalDateTime.now());
                riscoAnalise.setLeituraId(leituraDTO.getId());
                riscoAnalise.setSensorId(leituraDTO.getSensorId());
            }
            
            return riscoAnalise;
            
        } catch (Exception e) {
            // Log do erro e criação de análise padrão em caso de exceção
            System.err.println("Erro ao chamar API de risco: " + e.getMessage());
            
            RiscoDTO riscoFallback = new RiscoDTO();
            riscoFallback.setNivelRisco(0.0);
            riscoFallback.setCategoria("ERRO");
            riscoFallback.setDescricao("Erro ao processar análise de risco: " + e.getMessage());
            riscoFallback.setDataHoraAnalise(LocalDateTime.now());
            riscoFallback.setLeituraId(leituraDTO.getId());
            riscoFallback.setSensorId(leituraDTO.getSensorId());
            
            return riscoFallback;
        }
    }

    @Override
    public Optional<RiscoDTO> obterUltimoRiscoPorSensor(Long sensorId) {
        Optional<Risco> ultimoRisco = riscoRepository.findTopBySensorIdOrderByDataHoraAnaliseDesc(sensorId);
        
        return ultimoRisco.map(this::converterParaDTO);
    }

    @Override
    public List<Risco> obterHistoricoRiscoPorSensor(Long sensorId) {
        return riscoRepository.findBySensorIdOrderByDataHoraAnaliseDesc(sensorId);
    }

    @Override
    public List<RiscoDTO> obterRiscosPorArea(Double latitude, Double longitude, Double raio) {
        // Buscar sensores na área especificada
        List<Sensor> sensoresNaArea = sensorRepository.findSensoresNaArea(latitude, longitude, raio);
        
        // Para cada sensor, buscar o último risco calculado
        List<RiscoDTO> riscosNaArea = new ArrayList<>();
        
        for (Sensor sensor : sensoresNaArea) {
            Optional<Risco> ultimoRisco = riscoRepository.findTopBySensorIdOrderByDataHoraAnaliseDesc(sensor.getId());
            ultimoRisco.map(this::converterParaDTO).ifPresent(riscosNaArea::add);
        }
        
        return riscosNaArea;
    }
    
    private RiscoDTO converterParaDTO(Risco risco) {
        RiscoDTO dto = new RiscoDTO();
        dto.setId(risco.getId());
        dto.setNivelRisco(risco.getNivelRisco());
        dto.setCategoria(risco.getCategoria());
        dto.setDescricao(risco.getDescricao());
        dto.setDataHoraAnalise(risco.getDataHoraAnalise());
        dto.setLeituraId(risco.getLeitura().getId());
        dto.setSensorId(risco.getSensor().getId());
        return dto;
    }
}

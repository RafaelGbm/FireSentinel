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

/**
 * Implementação do serviço de análise de riscos.
 * Responsável por processar, analisar e gerenciar riscos de incêndio no sistema.
 * 
 * Esta implementação fornece:
 * - Análise de risco usando IA
 * - Integração com API externa de análise
 * - Persistência de análises
 * - Consultas geográficas
 * - Histórico de riscos
 */
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
    
    /**
     * Analisa o risco de incêndio com base em uma leitura.
     * Integra com API externa de IA para análise avançada.
     *
     * @param leituraDTO DTO contendo os dados para análise
     * @return RiscoDTO contendo o resultado da análise
     * @throws IllegalArgumentException se a leitura ou sensor não forem encontrados
     */
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
    
    /**
     * Chama a API externa de IA para análise de risco.
     * Implementa tratamento de erros e fallback em caso de falha.
     *
     * @param leituraDTO DTO com dados para análise
     * @return RiscoDTO resultado da análise ou análise padrão em caso de erro
     */
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
                return criarAnalisePadrao(leituraDTO, "INDETERMINADO", "Não foi possível analisar o risco. API indisponível.");
            }
            
            return riscoAnalise;
            
        } catch (Exception e) {
            return criarAnalisePadrao(leituraDTO, "ERRO", "Erro ao processar análise de risco: " + e.getMessage());
        }
    }
    
    /**
     * Cria uma análise de risco padrão para casos de erro.
     *
     * @param leituraDTO DTO da leitura original
     * @param categoria Categoria do risco padrão
     * @param descricao Descrição do erro ou situação
     * @return RiscoDTO análise padrão
     */
    private RiscoDTO criarAnalisePadrao(LeituraDTO leituraDTO, String categoria, String descricao) {
        RiscoDTO riscoFallback = new RiscoDTO();
        riscoFallback.setNivelRisco(0.0);
        riscoFallback.setCategoria(categoria);
        riscoFallback.setDescricao(descricao);
        riscoFallback.setDataHoraAnalise(LocalDateTime.now());
        riscoFallback.setLeituraId(leituraDTO.getId());
        riscoFallback.setSensorId(leituraDTO.getSensorId());
        return riscoFallback;
    }

    /**
     * Obtém a última análise de risco realizada para um sensor.
     *
     * @param sensorId ID do sensor
     * @return Optional<RiscoDTO> contendo a última análise se existir
     */
    @Override
    public Optional<RiscoDTO> obterUltimoRiscoPorSensor(Long sensorId) {
        Optional<Risco> ultimoRisco = riscoRepository.findTopBySensorIdOrderByDataHoraAnaliseDesc(sensorId);
        return ultimoRisco.map(this::converterParaDTO);
    }

    /**
     * Retorna o histórico completo de análises de risco de um sensor.
     *
     * @param sensorId ID do sensor
     * @return List<Risco> lista com todo o histórico de análises
     */
    @Override
    public List<Risco> obterHistoricoRiscoPorSensor(Long sensorId) {
        return riscoRepository.findBySensorIdOrderByDataHoraAnaliseDesc(sensorId);
    }

    /**
     * Busca análises de risco em uma área geográfica específica.
     * Utiliza cálculo de distância para encontrar sensores na área.
     *
     * @param latitude Latitude do ponto central
     * @param longitude Longitude do ponto central
     * @param raio Raio em quilômetros
     * @return List<RiscoDTO> lista de riscos na área especificada
     */
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
    
    /**
     * Converte uma entidade Risco para RiscoDTO.
     *
     * @param risco Entidade a ser convertida
     * @return RiscoDTO correspondente
     */
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

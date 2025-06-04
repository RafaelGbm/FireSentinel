package com.global_solution.fire_sentinel_App.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.global_solution.fire_sentinel_App.dto.OcorrenciaDTO;
import com.global_solution.fire_sentinel_App.model.Ocorrencia;
import com.global_solution.fire_sentinel_App.repository.OcorrenciaRepository;
import com.global_solution.fire_sentinel_App.service.OcorrenciaService;

/**
 * Implementação do serviço de gerenciamento de ocorrências.
 * Responsável por toda a lógica de negócio relacionada às ocorrências do sistema.
 * 
 * Esta implementação fornece:
 * - Registro e atualização de ocorrências
 * - Consultas por diferentes critérios
 * - Análise de criticidade
 * - Conversão entre entidades e DTOs
 */
@Service
public class OcorrenciaServiceImpl implements OcorrenciaService {

    @Autowired
    private OcorrenciaRepository ocorrenciaRepository;
    
    @Override
    public OcorrenciaDTO registrarOcorrencia(OcorrenciaDTO ocorrenciaDTO) {
        Ocorrencia ocorrencia = ocorrenciaDTO.toEntity();
        Ocorrencia ocorrenciaSalva = ocorrenciaRepository.save(ocorrencia);
        return OcorrenciaDTO.fromEntity(ocorrenciaSalva);
    }

    @Override
    public List<OcorrenciaDTO> listarTodas() {
        return ocorrenciaRepository.findAll().stream()
                .map(OcorrenciaDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<OcorrenciaDTO> buscarPorId(String id) {
        return ocorrenciaRepository.findById(id)
                .map(OcorrenciaDTO::fromEntity);
    }

    @Override
    public List<OcorrenciaDTO> buscarPorRegiao(String regiao) {
        return ocorrenciaRepository.findByRegiao(regiao).stream()
                .map(OcorrenciaDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<OcorrenciaDTO> buscarPorSeveridadeMinima(int severidade) {
        return ocorrenciaRepository.findBySeveridadeGreaterThanEqual(severidade).stream()
                .map(OcorrenciaDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<OcorrenciaDTO> buscarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return ocorrenciaRepository.findByDataHoraBetween(inicio, fim).stream()
                .map(OcorrenciaDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<OcorrenciaDTO> buscarPorOrigem(String origem) {
        return ocorrenciaRepository.findByOrigem(origem).stream()
                .map(OcorrenciaDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<OcorrenciaDTO> buscarOcorrenciasCriticas() {
        // Consideramos críticas as ocorrências com severidade >= 8 nas últimas 24 horas
        LocalDateTime vinteQuatroHorasAtras = LocalDateTime.now().minusHours(24);
        
        return ocorrenciaRepository.findByDataHoraBetween(vinteQuatroHorasAtras, LocalDateTime.now())
                .stream()
                .filter(o -> o.isOcorrenciaCritica(8, 1440)) // 1440 minutos = 24 horas
                .map(OcorrenciaDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<OcorrenciaDTO> atualizarOcorrencia(String id, OcorrenciaDTO ocorrenciaDTO) {
        Optional<Ocorrencia> ocorrenciaExistente = ocorrenciaRepository.findById(id);
        
        if (ocorrenciaExistente.isPresent()) {
            Ocorrencia ocorrencia = ocorrenciaExistente.get();
            // Atualiza apenas os campos mutáveis
            ocorrencia.setDescricao(ocorrenciaDTO.getDescricao());
            ocorrencia.setOrigem(ocorrenciaDTO.getOrigem());
            
            Ocorrencia ocorrenciaAtualizada = ocorrenciaRepository.save(ocorrencia);
            return Optional.of(OcorrenciaDTO.fromEntity(ocorrenciaAtualizada));
        }
        
        return Optional.empty();
    }

    @Override
    public boolean removerOcorrencia(String id) {
        if (ocorrenciaRepository.existsById(id)) {
            ocorrenciaRepository.deleteById(id);
            return true;
        }
        return false;
    }
} 
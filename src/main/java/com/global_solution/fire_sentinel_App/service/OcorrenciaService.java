package com.global_solution.fire_sentinel_App.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.global_solution.fire_sentinel_App.dto.OcorrenciaDTO;
import com.global_solution.fire_sentinel_App.model.Ocorrencia;

/**
 * Interface de serviço para gerenciamento de ocorrências.
 * Define as operações disponíveis para manipulação de ocorrências no sistema.
 */
public interface OcorrenciaService {
    
    /**
     * Registra uma nova ocorrência no sistema.
     * 
     * @param ocorrenciaDTO dados da ocorrência a ser registrada
     * @return OcorrenciaDTO com os dados da ocorrência registrada
     */
    OcorrenciaDTO registrarOcorrencia(OcorrenciaDTO ocorrenciaDTO);
    
    /**
     * Lista todas as ocorrências registradas.
     * 
     * @return List<OcorrenciaDTO> lista de todas as ocorrências
     */
    List<OcorrenciaDTO> listarTodas();
    
    /**
     * Busca uma ocorrência específica pelo seu ID.
     * 
     * @param id identificador da ocorrência
     * @return Optional<OcorrenciaDTO> contendo a ocorrência se encontrada
     */
    Optional<OcorrenciaDTO> buscarPorId(String id);
    
    /**
     * Busca ocorrências por região.
     * 
     * @param regiao nome da região
     * @return List<OcorrenciaDTO> lista de ocorrências da região
     */
    List<OcorrenciaDTO> buscarPorRegiao(String regiao);
    
    /**
     * Busca ocorrências por nível mínimo de severidade.
     * 
     * @param severidade nível mínimo de severidade
     * @return List<OcorrenciaDTO> lista de ocorrências com severidade maior ou igual
     */
    List<OcorrenciaDTO> buscarPorSeveridadeMinima(int severidade);
    
    /**
     * Busca ocorrências em um intervalo de datas.
     * 
     * @param inicio data/hora inicial
     * @param fim data/hora final
     * @return List<OcorrenciaDTO> lista de ocorrências no período
     */
    List<OcorrenciaDTO> buscarPorPeriodo(LocalDateTime inicio, LocalDateTime fim);
    
    /**
     * Busca ocorrências por origem da detecção.
     * 
     * @param origem origem da detecção (ex: "Sensor IoT", "Denúncia")
     * @return List<OcorrenciaDTO> lista de ocorrências da origem especificada
     */
    List<OcorrenciaDTO> buscarPorOrigem(String origem);
    
    /**
     * Busca ocorrências críticas (alta severidade e recentes).
     * 
     * @return List<OcorrenciaDTO> lista de ocorrências críticas
     */
    List<OcorrenciaDTO> buscarOcorrenciasCriticas();
    
    /**
     * Atualiza os dados de uma ocorrência existente.
     * 
     * @param id identificador da ocorrência
     * @param ocorrenciaDTO novos dados da ocorrência
     * @return Optional<OcorrenciaDTO> contendo a ocorrência atualizada se encontrada
     */
    Optional<OcorrenciaDTO> atualizarOcorrencia(String id, OcorrenciaDTO ocorrenciaDTO);
    
    /**
     * Remove uma ocorrência do sistema.
     * 
     * @param id identificador da ocorrência a ser removida
     * @return boolean indicando se a remoção foi bem-sucedida
     */
    boolean removerOcorrencia(String id);
} 
package com.global_solution.fire_sentinel_App.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.global_solution.fire_sentinel_App.model.Ocorrencia;

/**
 * Repositório para a entidade Ocorrencia.
 * Fornece métodos de acesso a dados para operações com ocorrências.
 */
@Repository
public interface OcorrenciaRepository extends JpaRepository<Ocorrencia, String> {
    
    /**
     * Busca ocorrências por região.
     */
    List<Ocorrencia> findByRegiao(String regiao);
    
    /**
     * Busca ocorrências por nível de severidade maior ou igual ao especificado.
     */
    List<Ocorrencia> findBySeveridadeGreaterThanEqual(int severidade);
    
    /**
     * Busca ocorrências registradas em um intervalo de datas.
     */
    List<Ocorrencia> findByDataHoraBetween(LocalDateTime inicio, LocalDateTime fim);
    
    /**
     * Busca ocorrências por origem.
     */
    List<Ocorrencia> findByOrigem(String origem);
    
    /**
     * Busca ocorrências por região e severidade mínima.
     */
    List<Ocorrencia> findByRegiaoAndSeveridadeGreaterThanEqual(String regiao, int severidade);
    
    /**
     * Busca a ocorrência mais recente de uma região.
     */
    Optional<Ocorrencia> findFirstByRegiaoOrderByDataHoraDesc(String regiao);
} 
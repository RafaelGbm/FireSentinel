package com.global_solution.fire_sentinel_App.dto;

import java.time.LocalDateTime;

import com.global_solution.fire_sentinel_App.model.Ocorrencia;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) para a entidade Ocorrencia.
 * Usado para transferência de dados em operações de API.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OcorrenciaDTO {
    private String id;
    private String regiao;
    private int severidade;
    private LocalDateTime dataHora;
    private String origem;
    private String descricao;
    private String mensagemAlerta;
    private boolean ocorrenciaCritica;
    private long tempoDecorrido;
    
    /**
     * Converte um objeto Ocorrencia em OcorrenciaDTO
     * @param ocorrencia Objeto Ocorrencia a ser convertido
     * @return OcorrenciaDTO correspondente
     */
    public static OcorrenciaDTO fromEntity(Ocorrencia ocorrencia) {
        if (ocorrencia == null) return null;
        
        OcorrenciaDTO dto = new OcorrenciaDTO();
        dto.setId(ocorrencia.getId());
        dto.setRegiao(ocorrencia.getRegiao());
        dto.setSeveridade(ocorrencia.getSeveridade());
        dto.setDataHora(ocorrencia.getDataHora());
        dto.setOrigem(ocorrencia.getOrigem());
        dto.setDescricao(ocorrencia.getDescricao());
        dto.setMensagemAlerta(ocorrencia.gerarMensagemAlerta());
        dto.setOcorrenciaCritica(ocorrencia.isOcorrenciaCritica(8, 60)); // Severidade >= 8 e últimos 60 minutos
        dto.setTempoDecorrido(ocorrencia.calcularTempoDecorrido());
        
        return dto;
    }
    
    /**
     * Converte o DTO em uma entidade Ocorrencia
     * @return Objeto Ocorrencia correspondente
     */
    public Ocorrencia toEntity() {
        Ocorrencia ocorrencia = new Ocorrencia(this.regiao, this.severidade);
        ocorrencia.setDataHora(this.dataHora);
        ocorrencia.setOrigem(this.origem);
        ocorrencia.setDescricao(this.descricao);
        return ocorrencia;
    }
    
    /**
     * Cria um DTO resumido com apenas as informações essenciais
     * @return OcorrenciaDTO com informações resumidas
     */
    public OcorrenciaDTO toSummary() {
        OcorrenciaDTO summary = new OcorrenciaDTO();
        summary.setId(this.id);
        summary.setRegiao(this.regiao);
        summary.setSeveridade(this.severidade);
        summary.setDataHora(this.dataHora);
        summary.setOrigem(this.origem);
        summary.setMensagemAlerta(this.mensagemAlerta);
        summary.setOcorrenciaCritica(this.ocorrenciaCritica);
        return summary;
    }
} 
package com.global_solution.fire_sentinel_App.dto;

import java.time.LocalDateTime;

import com.global_solution.fire_sentinel_App.model.Risco;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) para a entidade Risco.
 * Usado para transferência de dados em operações de API.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RiscoDTO {
    private Long id;
    private Long sensorId;
    private String sensorNome;
    private Long leituraId;
    private LocalDateTime dataHoraAnalise;
    private Double nivelRisco;
    private String categoria;
    private String descricao;
    private String recomendacao;
    
    /**
     * Converte um objeto Risco em RiscoDTO
     * @param risco Objeto Risco a ser convertido
     * @return RiscoDTO correspondente
     */
    public static RiscoDTO fromEntity(Risco risco) {
        if (risco == null) return null;
        
        RiscoDTO dto = new RiscoDTO();
        dto.setId(risco.getId());
        dto.setSensorId(risco.getSensor().getId());
        dto.setSensorNome(risco.getSensor().getNome());
        dto.setLeituraId(risco.getLeitura().getId());
        dto.setDataHoraAnalise(risco.getDataHoraAnalise());
        dto.setNivelRisco(risco.getNivelRisco());
        dto.setCategoria(risco.getCategoria());
        dto.setDescricao(risco.getDescricao());
        dto.setRecomendacao(risco.gerarRecomendacao());
        
        return dto;
    }
    
    /**
     * Converte o DTO em uma entidade Risco
     * @return Objeto Risco correspondente
     */
    public Risco toEntity() {
        Risco risco = new Risco();
        risco.setId(this.id);
        risco.setDataHoraAnalise(this.dataHoraAnalise);
        risco.setNivelRisco(this.nivelRisco);
        risco.setCategoria(this.categoria);
        risco.setDescricao(this.descricao);
        return risco;
    }
    
    /**
     * Cria um DTO resumido com apenas as informações essenciais
     * @return RiscoDTO com informações resumidas
     */
    public RiscoDTO toSummary() {
        RiscoDTO summary = new RiscoDTO();
        summary.setId(this.id);
        summary.setSensorId(this.sensorId);
        summary.setSensorNome(this.sensorNome);
        summary.setDataHoraAnalise(this.dataHoraAnalise);
        summary.setNivelRisco(this.nivelRisco);
        summary.setCategoria(this.categoria);
        summary.setRecomendacao(this.recomendacao);
        return summary;
    }
} 
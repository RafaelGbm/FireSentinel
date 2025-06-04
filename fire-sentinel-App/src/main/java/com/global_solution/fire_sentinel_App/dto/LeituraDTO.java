package com.global_solution.fire_sentinel_App.dto;

import java.time.LocalDateTime;

import com.global_solution.fire_sentinel_App.model.Leitura;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) para a entidade Leitura.
 * Usado para transferência de dados em operações de API.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeituraDTO {
    private Long id;
    private Long sensorId;
    private String sensorNome;
    private LocalDateTime dataHora;
    private Double temperatura;
    private Double umidade;
    private Double nivelFumaca;
    private Double co2;
    private Double indiceRisco;
    
    /**
     * Converte um objeto Leitura em LeituraDTO
     * @param leitura Objeto Leitura a ser convertido
     * @return LeituraDTO correspondente
     */
    public static LeituraDTO fromEntity(Leitura leitura) {
        if (leitura == null) return null;
        
        LeituraDTO dto = new LeituraDTO();
        dto.setId(leitura.getId());
        dto.setSensorId(leitura.getSensor().getId());
        dto.setSensorNome(leitura.getSensor().getNome());
        dto.setDataHora(leitura.getDataHora());
        dto.setTemperatura(leitura.getTemperatura());
        dto.setUmidade(leitura.getUmidade());
        dto.setNivelFumaca(leitura.getNivelFumaca());
        dto.setCo2(leitura.getCo2());
        dto.setIndiceRisco(leitura.calcularIndiceRisco());
        
        return dto;
    }
    
    /**
     * Converte o DTO em uma entidade Leitura
     * @return Objeto Leitura correspondente
     */
    public Leitura toEntity() {
        Leitura leitura = new Leitura();
        leitura.setId(this.id);
        leitura.setDataHora(this.dataHora);
        leitura.setTemperatura(this.temperatura);
        leitura.setUmidade(this.umidade);
        leitura.setNivelFumaca(this.nivelFumaca);
        leitura.setCo2(this.co2);
        return leitura;
    }
    
    /**
     * Cria um DTO resumido com apenas as informações essenciais
     * @return LeituraDTO com informações resumidas
     */
    public LeituraDTO toSummary() {
        LeituraDTO summary = new LeituraDTO();
        summary.setId(this.id);
        summary.setSensorId(this.sensorId);
        summary.setSensorNome(this.sensorNome);
        summary.setDataHora(this.dataHora);
        summary.setTemperatura(this.temperatura);
        summary.setUmidade(this.umidade);
        summary.setIndiceRisco(this.indiceRisco);
        return summary;
    }
} 
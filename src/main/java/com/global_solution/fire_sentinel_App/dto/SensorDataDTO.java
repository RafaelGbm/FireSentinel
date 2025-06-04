package com.global_solution.fire_sentinel_App.dto;

import java.time.LocalDateTime;

import com.global_solution.fire_sentinel_App.model.SensorData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) para a entidade SensorData.
 * Usado para transferência de dados em operações de API.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SensorDataDTO {
    private Long id;
    private Long sensorId;
    private String sensorNome;
    private LocalDateTime dataHora;
    private Double temperatura;
    private Double umidade;
    private Double nivelFumaca;
    private Double co2;
    private boolean situacaoRisco;
    
    /**
     * Converte um objeto SensorData em SensorDataDTO
     * @param sensorData Objeto SensorData a ser convertido
     * @return SensorDataDTO correspondente
     */
    public static SensorDataDTO fromEntity(SensorData sensorData) {
        if (sensorData == null) return null;
        
        return new SensorDataDTO(
            sensorData.getId(),
            sensorData.getSensor().getId(),
            sensorData.getSensor().getNome(),
            sensorData.getDataHora(),
            sensorData.getTemperatura(),
            sensorData.getUmidade(),
            sensorData.getNivelFumaca(),
            sensorData.getCo2(),
            sensorData.isSituacaoRisco()
        );
    }
    
    /**
     * Converte o DTO em uma entidade SensorData
     * @param sensor O sensor associado à leitura
     * @return Objeto SensorData correspondente
     */
    public SensorData toEntity() {
        SensorData sensorData = new SensorData();
        sensorData.setId(this.id);
        sensorData.setDataHora(this.dataHora);
        sensorData.setTemperatura(this.temperatura);
        sensorData.setUmidade(this.umidade);
        sensorData.setNivelFumaca(this.nivelFumaca);
        sensorData.setCo2(this.co2);
        return sensorData;
    }
    
    /**
     * Cria um DTO resumido com apenas as informações essenciais
     * @return SensorDataDTO com informações resumidas
     */
    public SensorDataDTO toSummary() {
        return new SensorDataDTO(
            this.id,
            this.sensorId,
            this.sensorNome,
            this.dataHora,
            this.temperatura,
            this.umidade,
            null, // omitindo nivelFumaca
            null, // omitindo co2
            this.situacaoRisco
        );
    }
} 
package com.global_solution.fire_sentinel_App.dto;

import com.global_solution.fire_sentinel_App.model.Sensor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) para a entidade Sensor.
 * Usado para transferência de dados em operações de API.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SensorDTO {
    private Long id;
    private String nome;
    private String tipo;
    private String localizacao;
    private Double latitude;
    private Double longitude;
    private boolean ativo;
    
    /**
     * Converte um objeto Sensor em SensorDTO
     * @param sensor Objeto Sensor a ser convertido
     * @return SensorDTO correspondente
     */
    public static SensorDTO fromEntity(Sensor sensor) {
        if (sensor == null) return null;
        
        return new SensorDTO(
            sensor.getId(),
            sensor.getNome(),
            sensor.getTipo(),
            sensor.getLocalizacao(),
            sensor.getLatitude(),
            sensor.getLongitude(),
            sensor.isAtivo()
        );
    }
    
    /**
     * Converte o DTO em uma entidade Sensor
     * @return Objeto Sensor correspondente
     */
    public Sensor toEntity() {
        Sensor sensor = new Sensor();
        sensor.setId(this.id);
        sensor.setNome(this.nome);
        sensor.setTipo(this.tipo);
        sensor.setLocalizacao(this.localizacao);
        sensor.setLatitude(this.latitude);
        sensor.setLongitude(this.longitude);
        sensor.setAtivo(this.ativo);
        return sensor;
    }
} 
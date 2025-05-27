package com.global_solution.fire_sentinel_App.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeituraDTO {
    
    private Long id;
    private Double temperatura;
    private Double umidade;
    private Double nivelFumaca;
    private Double co2;
    private LocalDateTime dataHora;
    private Long sensorId;
}

package com.global_solution.fire_sentinel_App.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RiscoDTO {
    
    private Long id;
    private Double nivelRisco;
    private String categoria;
    private String descricao;
    private LocalDateTime dataHoraAnalise;
    private Long leituraId;
    private Long sensorId;
}

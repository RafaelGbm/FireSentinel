package com.global_solution.fire_sentinel_App.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}

package com.global_solution.fire_sentinel_App.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "riscos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Risco {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Double nivelRisco; // Valor entre 0 e 1 representando a probabilidade de incêndio
    
    private String categoria; // BAIXO, MÉDIO, ALTO, CRÍTICO
    
    private String descricao;
    
    private LocalDateTime dataHoraAnalise;
    
    @ManyToOne
    @JoinColumn(name = "leitura_id")
    private Leitura leitura;
    
    @ManyToOne
    @JoinColumn(name = "sensor_id")
    private Sensor sensor;
}

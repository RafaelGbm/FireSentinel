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
@Table(name = "leituras")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Leitura {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Double temperatura;
    
    private Double umidade;
    
    private Double nivelFumaca;
    
    private Double co2;
    
    private LocalDateTime dataHora;
    
    @ManyToOne
    @JoinColumn(name = "sensor_id")
    private Sensor sensor;
}

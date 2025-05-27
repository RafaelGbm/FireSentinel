package com.global_solution.fire_sentinel_App.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sensores")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sensor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nome;
    
    private String tipo; // temperatura, umidade, fumaça, etc.
    
    private String localizacao;
    
    private Double latitude;
    
    private Double longitude;
    
    private boolean ativo = true;
    
    @OneToMany(mappedBy = "sensor")
    private List<Leitura> leituras;
}

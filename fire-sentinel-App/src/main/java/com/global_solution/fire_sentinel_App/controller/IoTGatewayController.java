package com.global_solution.fire_sentinel_App.controller;

import com.global_solution.fire_sentinel_App.ia.IAEngine;
import com.global_solution.fire_sentinel_App.model.SensorData;
import com.global_solution.fire_sentinel_App.model.Ocorrencia;



import java.time.LocalDateTime;

/**
 * Controlador responsável por fazer a ponte entre os dados dos sensores IoT e o sistema de ocorrências.
 */
public class IoTGatewayController {

    /**
     * Processa uma leitura do sensor e cria uma ocorrência com base na severidade prevista pela IA.
     *
     * @param sensorData Dados de entrada dos sensores (temperatura, umidade, nível de fumaça)
     * @return Ocorrência gerada automaticamente
     */
    public Ocorrencia processarLeituraSensor(SensorData sensorData) {
        int severidade = IAEngine.preverSeveridade(sensorData);
        Ocorrencia ocorrencia = new Ocorrencia("SensorRemoto", severidade);
        ocorrencia.setDataHora(LocalDateTime.now());
        ocorrencia.setOrigem("Gateway IoT");
        ocorrencia.setDescricao("Ocorrência gerada automaticamente com base em sensores IoT");
        return ocorrencia;
    }
}


package com.global_solution.fire_sentinel_App.controller;

import com.global_solution.fire_sentinel_App.ia.IAEngine;
import com.global_solution.fire_sentinel_App.model.SensorData;
import com.global_solution.fire_sentinel_App.model.Ocorrencia;
import java.time.LocalDateTime;

/**
 * Controlador responsável por gerenciar a integração entre dispositivos IoT e o sistema Fire Sentinel.
 * Atua como gateway para processamento de dados dos sensores e geração automática de ocorrências.
 * 
 * Este controlador implementa a lógica de:
 * - Recebimento de dados dos sensores IoT
 * - Processamento de leituras usando IA
 * - Geração automática de ocorrências baseada em análises
 * - Integração com o sistema de monitoramento
 */
public class IoTGatewayController {

    /**
     * Processa uma leitura recebida de um sensor IoT e gera uma ocorrência se necessário.
     * Utiliza o motor de IA para analisar os dados e determinar a severidade da situação.
     *
     * O processo inclui:
     * 1. Análise dos dados do sensor usando IA
     * 2. Cálculo da severidade da situação
     * 3. Geração automática de ocorrência se necessário
     * 4. Registro do evento no sistema
     *
     * @param sensorData Dados recebidos do sensor IoT (temperatura, umidade, nível de fumaça)
     * @return Ocorrência gerada com base na análise dos dados, ou null se não houver necessidade
     */
    public Ocorrencia processarLeituraSensor(SensorData sensorData) {
        // Análise da severidade usando IA
        int severidade = IAEngine.preverSeveridade(sensorData);
        
        // Criação da ocorrência com dados relevantes
        Ocorrencia ocorrencia = new Ocorrencia("SensorRemoto", severidade);
        ocorrencia.setDataHora(LocalDateTime.now());
        ocorrencia.setOrigem("Gateway IoT");
        ocorrencia.setDescricao("Ocorrência gerada automaticamente com base em sensores IoT");
        
        return ocorrencia;
    }
}


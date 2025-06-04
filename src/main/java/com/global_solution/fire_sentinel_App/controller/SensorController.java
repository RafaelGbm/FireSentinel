package com.global_solution.fire_sentinel_App.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.global_solution.fire_sentinel_App.dto.SensorDTO;
import com.global_solution.fire_sentinel_App.model.Sensor;
import com.global_solution.fire_sentinel_App.service.SensorService;

/**
 * Controller REST para gerenciamento de sensores no sistema Fire Sentinel.
 * Fornece endpoints para operações CRUD em sensores.
 * 
 * @RestController indica que esta classe é um controlador REST
 * @RequestMapping("/sensores") mapeia todas as requisições para /sensores
 */
@RestController
@RequestMapping("/sensores")
public class SensorController {
    
    @Autowired
    private SensorService sensorService;
    
    /**
     * Cadastra um novo sensor no sistema.
     * 
     * @param sensorDTO DTO contendo os dados do sensor a ser cadastrado
     * @return ResponseEntity com o sensor cadastrado e status HTTP 201 (CREATED)
     */
    @PostMapping
    public ResponseEntity<Sensor> cadastrarSensor(@RequestBody SensorDTO sensorDTO) {
        Sensor novoSensor = sensorService.cadastrarSensor(sensorDTO);
        return new ResponseEntity<>(novoSensor, HttpStatus.CREATED);
    }
    
    /**
     * Lista todos os sensores cadastrados no sistema.
     * 
     * @return ResponseEntity com a lista de sensores e status HTTP 200 (OK)
     */
    @GetMapping
    public ResponseEntity<List<Sensor>> listarSensores() {
        List<Sensor> sensores = sensorService.listarTodos();
        return ResponseEntity.ok(sensores);
    }
    
    /**
     * Busca um sensor específico pelo seu ID.
     * 
     * @param id ID do sensor a ser buscado
     * @return ResponseEntity com o sensor encontrado (200) ou não encontrado (404)
     */
    @GetMapping("/{id}")
    public ResponseEntity<Sensor> obterSensor(@PathVariable Long id) {
        Optional<Sensor> sensor = sensorService.buscarPorId(id);
        return sensor.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Atualiza os dados de um sensor existente.
     * 
     * @param id ID do sensor a ser atualizado
     * @param sensorDTO DTO contendo os novos dados do sensor
     * @return ResponseEntity com o sensor atualizado (200) ou não encontrado (404)
     */
    @PutMapping("/{id}")
    public ResponseEntity<Sensor> atualizarSensor(@PathVariable Long id, @RequestBody SensorDTO sensorDTO) {
        Optional<Sensor> sensorAtualizado = sensorService.atualizarSensor(id, sensorDTO);
        return sensorAtualizado.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Remove um sensor do sistema.
     * 
     * @param id ID do sensor a ser removido
     * @return ResponseEntity sem conteúdo (204) se removido ou não encontrado (404)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerSensor(@PathVariable Long id) {
        boolean removido = sensorService.removerSensor(id);
        return removido ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}

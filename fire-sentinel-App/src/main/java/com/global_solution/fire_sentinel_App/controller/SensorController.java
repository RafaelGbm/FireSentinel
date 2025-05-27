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

@RestController
@RequestMapping("/sensores")
public class SensorController {
    
    @Autowired
    private SensorService sensorService;
    
    @PostMapping
    public ResponseEntity<Sensor> cadastrarSensor(@RequestBody SensorDTO sensorDTO) {
        Sensor novoSensor = sensorService.cadastrarSensor(sensorDTO);
        return new ResponseEntity<>(novoSensor, HttpStatus.CREATED);
    }
    
    @GetMapping
    public ResponseEntity<List<Sensor>> listarSensores() {
        List<Sensor> sensores = sensorService.listarTodos();
        return ResponseEntity.ok(sensores);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Sensor> obterSensor(@PathVariable Long id) {
        Optional<Sensor> sensor = sensorService.buscarPorId(id);
        return sensor.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Sensor> atualizarSensor(@PathVariable Long id, @RequestBody SensorDTO sensorDTO) {
        Optional<Sensor> sensorAtualizado = sensorService.atualizarSensor(id, sensorDTO);
        return sensorAtualizado.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerSensor(@PathVariable Long id) {
        boolean removido = sensorService.removerSensor(id);
        return removido ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}

package com.global_solution.fire_sentinel_App.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.global_solution.fire_sentinel_App.dto.LeituraDTO;
import com.global_solution.fire_sentinel_App.model.Leitura;
import com.global_solution.fire_sentinel_App.service.LeituraService;

/**
 * Controller REST para gerenciamento de leituras de sensores no sistema Fire Sentinel.
 * Fornece endpoints para registro e consulta de leituras ambientais.
 * 
 * @RestController indica que esta classe é um controlador REST
 * @RequestMapping("/leituras") mapeia todas as requisições para /leituras
 */
@RestController
@RequestMapping("/leituras")
public class LeituraController {
    
    @Autowired
    private LeituraService leituraService;
    
    /**
     * Registra uma nova leitura de sensor no sistema.
     * 
     * @param leituraDTO DTO contendo os dados da leitura a ser registrada
     * @return ResponseEntity com a leitura registrada e status HTTP 201 (CREATED)
     */
    @PostMapping
    public ResponseEntity<Leitura> registrarLeitura(@RequestBody LeituraDTO leituraDTO) {
        Leitura novaLeitura = leituraService.registrarLeitura(leituraDTO);
        return new ResponseEntity<>(novaLeitura, HttpStatus.CREATED);
    }
    
    /**
     * Lista todas as leituras do sistema, com opção de filtrar por sensor.
     * 
     * @param sensorId ID opcional do sensor para filtrar as leituras
     * @return ResponseEntity com a lista de leituras e status HTTP 200 (OK)
     */
    @GetMapping
    public ResponseEntity<List<Leitura>> listarLeituras(
            @RequestParam(required = false) Long sensorId) {
        
        List<Leitura> leituras;
        if (sensorId != null) {
            leituras = leituraService.buscarPorSensor(sensorId);
        } else {
            leituras = leituraService.listarTodas();
        }
        
        return ResponseEntity.ok(leituras);
    }
    
    /**
     * Busca uma leitura específica pelo seu ID.
     * 
     * @param id ID da leitura a ser buscada
     * @return ResponseEntity com a leitura encontrada (200) ou não encontrada (404)
     */
    @GetMapping("/{id}")
    public ResponseEntity<Leitura> obterLeitura(@PathVariable Long id) {
        Optional<Leitura> leitura = leituraService.buscarPorId(id);
        return leitura.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Retorna as últimas leituras registradas para cada sensor.
     * Útil para monitoramento em tempo real do estado dos sensores.
     * 
     * @return ResponseEntity com a lista das últimas leituras e status HTTP 200 (OK)
     */
    @GetMapping("/ultimas")
    public ResponseEntity<List<Leitura>> obterUltimasLeituras() {
        List<Leitura> ultimasLeituras = leituraService.obterUltimasLeiturasPorSensor();
        return ResponseEntity.ok(ultimasLeituras);
    }
}

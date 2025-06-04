package com.global_solution.fire_sentinel_App.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.global_solution.fire_sentinel_App.dto.LeituraDTO;
import com.global_solution.fire_sentinel_App.dto.RiscoDTO;
import com.global_solution.fire_sentinel_App.model.Risco;
import com.global_solution.fire_sentinel_App.service.RiscoService;

/**
 * Controller REST para gerenciamento de análises de risco no sistema Fire Sentinel.
 * Fornece endpoints para análise, consulta e monitoramento de riscos de incêndio.
 * 
 * @RestController indica que esta classe é um controlador REST
 * @RequestMapping("/risco") mapeia todas as requisições para /risco
 */
@RestController
@RequestMapping("/risco")
public class RiscoController {
    
    @Autowired
    private RiscoService riscoService;
    
    /**
     * Analisa o risco de incêndio com base em uma leitura de sensor.
     * Utiliza o serviço de IA para processar os dados e gerar uma análise.
     * 
     * @param leituraDTO DTO contendo os dados da leitura para análise
     * @return ResponseEntity com a análise de risco e status HTTP 200 (OK)
     */
    @PostMapping("/analisar")
    public ResponseEntity<RiscoDTO> analisarRisco(@RequestBody LeituraDTO leituraDTO) {
        // Este endpoint recebe uma leitura e retorna uma análise de risco
        // O serviço chamará a API de IA para fazer a análise
        RiscoDTO riscoAnalise = riscoService.analisarRisco(leituraDTO);
        return ResponseEntity.ok(riscoAnalise);
    }
    
    /**
     * Obtém a última análise de risco realizada para um sensor específico.
     * 
     * @param sensorId ID do sensor para buscar o último risco
     * @return ResponseEntity com o último risco (200) ou não encontrado (404)
     */
    @GetMapping("/{sensorId}")
    public ResponseEntity<RiscoDTO> obterUltimoRiscoPorSensor(@PathVariable Long sensorId) {
        // Retorna o último risco calculado para um sensor específico
        Optional<RiscoDTO> ultimoRisco = riscoService.obterUltimoRiscoPorSensor(sensorId);
        return ultimoRisco.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Retorna o histórico completo de análises de risco para um sensor.
     * 
     * @param sensorId ID do sensor para buscar o histórico
     * @return ResponseEntity com a lista de riscos e status HTTP 200 (OK)
     */
    @GetMapping("/sensor/{sensorId}/historico")
    public ResponseEntity<List<Risco>> obterHistoricoRiscoPorSensor(@PathVariable Long sensorId) {
        List<Risco> historicoRiscos = riscoService.obterHistoricoRiscoPorSensor(sensorId);
        return ResponseEntity.ok(historicoRiscos);
    }
    
    /**
     * Busca análises de risco em uma área geográfica específica.
     * Útil para monitoramento de regiões e identificação de zonas de risco.
     * 
     * @param latitude Latitude do ponto central da busca
     * @param longitude Longitude do ponto central da busca
     * @param raio Raio em quilômetros para a busca
     * @return ResponseEntity com a lista de riscos na área e status HTTP 200 (OK)
     */
    @GetMapping("/area/{latitude}/{longitude}/{raio}")
    public ResponseEntity<List<RiscoDTO>> obterRiscosPorArea(
            @PathVariable Double latitude,
            @PathVariable Double longitude,
            @PathVariable Double raio) {
        List<RiscoDTO> riscosNaArea = riscoService.obterRiscosPorArea(latitude, longitude, raio);
        return ResponseEntity.ok(riscosNaArea);
    }
}

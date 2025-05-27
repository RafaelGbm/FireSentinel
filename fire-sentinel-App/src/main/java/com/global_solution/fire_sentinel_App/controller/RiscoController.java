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

@RestController
@RequestMapping("/risco")
public class RiscoController {
    
    @Autowired
    private RiscoService riscoService;
    
    @PostMapping("/analisar")
    public ResponseEntity<RiscoDTO> analisarRisco(@RequestBody LeituraDTO leituraDTO) {
        // Este endpoint recebe uma leitura e retorna uma análise de risco
        // O serviço chamará a API de IA para fazer a análise
        RiscoDTO riscoAnalise = riscoService.analisarRisco(leituraDTO);
        return ResponseEntity.ok(riscoAnalise);
    }
    
    @GetMapping("/{sensorId}")
    public ResponseEntity<RiscoDTO> obterUltimoRiscoPorSensor(@PathVariable Long sensorId) {
        // Retorna o último risco calculado para um sensor específico
        Optional<RiscoDTO> ultimoRisco = riscoService.obterUltimoRiscoPorSensor(sensorId);
        return ultimoRisco.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/sensor/{sensorId}/historico")
    public ResponseEntity<List<Risco>> obterHistoricoRiscoPorSensor(@PathVariable Long sensorId) {
        List<Risco> historicoRiscos = riscoService.obterHistoricoRiscoPorSensor(sensorId);
        return ResponseEntity.ok(historicoRiscos);
    }
    
    @GetMapping("/area/{latitude}/{longitude}/{raio}")
    public ResponseEntity<List<RiscoDTO>> obterRiscosPorArea(
            @PathVariable Double latitude,
            @PathVariable Double longitude,
            @PathVariable Double raio) {
        List<RiscoDTO> riscosNaArea = riscoService.obterRiscosPorArea(latitude, longitude, raio);
        return ResponseEntity.ok(riscosNaArea);
    }
}

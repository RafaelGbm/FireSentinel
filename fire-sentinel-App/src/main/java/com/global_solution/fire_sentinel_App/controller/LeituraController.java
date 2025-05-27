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

@RestController
@RequestMapping("/leituras")
public class LeituraController {
    
    @Autowired
    private LeituraService leituraService;
    
    @PostMapping
    public ResponseEntity<Leitura> registrarLeitura(@RequestBody LeituraDTO leituraDTO) {
        Leitura novaLeitura = leituraService.registrarLeitura(leituraDTO);
        return new ResponseEntity<>(novaLeitura, HttpStatus.CREATED);
    }
    
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
    
    @GetMapping("/{id}")
    public ResponseEntity<Leitura> obterLeitura(@PathVariable Long id) {
        Optional<Leitura> leitura = leituraService.buscarPorId(id);
        return leitura.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/ultimas")
    public ResponseEntity<List<Leitura>> obterUltimasLeituras() {
        List<Leitura> ultimasLeituras = leituraService.obterUltimasLeiturasPorSensor();
        return ResponseEntity.ok(ultimasLeituras);
    }
}

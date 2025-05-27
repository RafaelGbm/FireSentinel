package com.global_solution.fire_sentinel_App.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/leituras")
@RequiredArgsConstructor
public class LeituraController {
    private final LeituraService leituraService;

    @PostMapping
    public ResponseEntity<LeituraDTO> registrar(@RequestBody LeituraDTO leitura) {
        return ResponseEntity.status(HttpStatus.CREATED).body(leituraService.salvar(leitura));
    }

    @GetMapping
    public List<LeituraDTO> listarTodas() {
        return leituraService.listar();
    }

    @GetMapping("/ultimas")
    public List<LeituraDTO> ultimasLeituras() {
        return leituraService.ultimasPorSensor();
    }

    @GetMapping(params = "sensorId")
    public List<LeituraDTO> porSensor(@RequestParam Long sensorId) {
        return leituraService.buscarPorSensor(sensorId);
    }
}
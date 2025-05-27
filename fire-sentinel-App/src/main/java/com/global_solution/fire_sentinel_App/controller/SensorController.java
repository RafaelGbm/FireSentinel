package com.global_solution.fire_sentinel_App.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sensores")
@RequiredArgsConstructor
public class SensorController {
    private final SensorService sensorService;

    @PostMapping
    public ResponseEntity<SensorDTO> cadastrar(@RequestBody @Valid SensorDTO sensor) {
        return ResponseEntity.status(HttpStatus.CREATED).body(sensorService.salvar(sensor));
    }

    @GetMapping
    public List<SensorDTO> listar() {
        return sensorService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SensorDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(sensorService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SensorDTO> atualizar(@PathVariable Long id, @RequestBody SensorDTO dto) {
        return ResponseEntity.ok(sensorService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        sensorService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
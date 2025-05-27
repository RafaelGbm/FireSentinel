package com.global_solution.fire_sentinel_App.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/risco")
@RequiredArgsConstructor
public class RiscoController {
    private final RiscoService riscoService;

    @PostMapping("/analisar")
    public ResponseEntity<RiscoDTO> analisar(@RequestBody LeituraDTO leitura) {
        return ResponseEntity.ok(riscoService.analisarRisco(leitura));
    }

    @GetMapping("/{sensorId}")
    public ResponseEntity<RiscoDTO> ultimoRisco(@PathVariable Long sensorId) {
        return ResponseEntity.ok(riscoService.ultimoRisco(sensorId));
    }
}

package com.global_solution.fire_sentinel_App.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.global_solution.fire_sentinel_App.dto.OcorrenciaDTO;
import com.global_solution.fire_sentinel_App.service.OcorrenciaService;

/**
 * Controller REST para gerenciamento de ocorrências no sistema Fire Sentinel.
 * Fornece endpoints para registro, consulta e monitoramento de ocorrências.
 * 
 * @RestController indica que esta classe é um controlador REST
 * @RequestMapping("/ocorrencias") mapeia todas as requisições para /ocorrencias
 */
@RestController
@RequestMapping("/ocorrencias")
public class OcorrenciaController {
    
    @Autowired
    private OcorrenciaService ocorrenciaService;
    
    /**
     * Registra uma nova ocorrência no sistema.
     * 
     * @param ocorrenciaDTO DTO contendo os dados da ocorrência
     * @return ResponseEntity com a ocorrência registrada e status HTTP 201 (CREATED)
     */
    @PostMapping
    public ResponseEntity<OcorrenciaDTO> registrarOcorrencia(@RequestBody OcorrenciaDTO ocorrenciaDTO) {
        OcorrenciaDTO ocorrenciaSalva = ocorrenciaService.registrarOcorrencia(ocorrenciaDTO);
        return new ResponseEntity<>(ocorrenciaSalva, HttpStatus.CREATED);
    }
    
    /**
     * Lista todas as ocorrências registradas no sistema.
     * 
     * @return ResponseEntity com a lista de ocorrências e status HTTP 200 (OK)
     */
    @GetMapping
    public ResponseEntity<List<OcorrenciaDTO>> listarOcorrencias() {
        List<OcorrenciaDTO> ocorrencias = ocorrenciaService.listarTodas();
        return ResponseEntity.ok(ocorrencias);
    }
    
    /**
     * Busca uma ocorrência específica pelo seu ID.
     * 
     * @param id ID da ocorrência a ser buscada
     * @return ResponseEntity com a ocorrência encontrada (200) ou não encontrada (404)
     */
    @GetMapping("/{id}")
    public ResponseEntity<OcorrenciaDTO> buscarOcorrencia(@PathVariable String id) {
        return ocorrenciaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Busca ocorrências por região.
     * 
     * @param regiao nome da região para filtrar
     * @return ResponseEntity com a lista de ocorrências da região e status HTTP 200 (OK)
     */
    @GetMapping("/regiao/{regiao}")
    public ResponseEntity<List<OcorrenciaDTO>> buscarPorRegiao(@PathVariable String regiao) {
        List<OcorrenciaDTO> ocorrencias = ocorrenciaService.buscarPorRegiao(regiao);
        return ResponseEntity.ok(ocorrencias);
    }
    
    /**
     * Busca ocorrências por nível mínimo de severidade.
     * 
     * @param severidade nível mínimo de severidade para filtrar
     * @return ResponseEntity com a lista de ocorrências e status HTTP 200 (OK)
     */
    @GetMapping("/severidade/{severidade}")
    public ResponseEntity<List<OcorrenciaDTO>> buscarPorSeveridade(@PathVariable int severidade) {
        List<OcorrenciaDTO> ocorrencias = ocorrenciaService.buscarPorSeveridadeMinima(severidade);
        return ResponseEntity.ok(ocorrencias);
    }
    
    /**
     * Busca ocorrências em um intervalo de datas.
     * 
     * @param inicio data/hora inicial do período
     * @param fim data/hora final do período
     * @return ResponseEntity com a lista de ocorrências e status HTTP 200 (OK)
     */
    @GetMapping("/periodo")
    public ResponseEntity<List<OcorrenciaDTO>> buscarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {
        List<OcorrenciaDTO> ocorrencias = ocorrenciaService.buscarPorPeriodo(inicio, fim);
        return ResponseEntity.ok(ocorrencias);
    }
    
    /**
     * Busca ocorrências por origem da detecção.
     * 
     * @param origem origem para filtrar (ex: "Sensor IoT", "Denúncia")
     * @return ResponseEntity com a lista de ocorrências e status HTTP 200 (OK)
     */
    @GetMapping("/origem/{origem}")
    public ResponseEntity<List<OcorrenciaDTO>> buscarPorOrigem(@PathVariable String origem) {
        List<OcorrenciaDTO> ocorrencias = ocorrenciaService.buscarPorOrigem(origem);
        return ResponseEntity.ok(ocorrencias);
    }
    
    /**
     * Lista todas as ocorrências críticas (alta severidade e recentes).
     * 
     * @return ResponseEntity com a lista de ocorrências críticas e status HTTP 200 (OK)
     */
    @GetMapping("/criticas")
    public ResponseEntity<List<OcorrenciaDTO>> listarOcorrenciasCriticas() {
        List<OcorrenciaDTO> ocorrenciasCriticas = ocorrenciaService.buscarOcorrenciasCriticas();
        return ResponseEntity.ok(ocorrenciasCriticas);
    }
    
    /**
     * Atualiza os dados de uma ocorrência existente.
     * 
     * @param id ID da ocorrência a ser atualizada
     * @param ocorrenciaDTO DTO contendo os novos dados
     * @return ResponseEntity com a ocorrência atualizada (200) ou não encontrada (404)
     */
    @PutMapping("/{id}")
    public ResponseEntity<OcorrenciaDTO> atualizarOcorrencia(
            @PathVariable String id,
            @RequestBody OcorrenciaDTO ocorrenciaDTO) {
        return ocorrenciaService.atualizarOcorrencia(id, ocorrenciaDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Remove uma ocorrência do sistema.
     * 
     * @param id ID da ocorrência a ser removida
     * @return ResponseEntity sem conteúdo (204) se removida ou não encontrada (404)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerOcorrencia(@PathVariable String id) {
        boolean removido = ocorrenciaService.removerOcorrencia(id);
        return removido ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
} 
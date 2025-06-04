package com.global_solution.fire_sentinel_App;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.global_solution.fire_sentinel_App.model.Ocorrencia;

class OcorrenciaTest {
    
    private Ocorrencia ocorrencia;
    
    @BeforeEach
    void setUp() {
        ocorrencia = new Ocorrencia("Região Sul", 7);
        ocorrencia.setOrigem("Teste Unitário");
        ocorrencia.setDescricao("Ocorrência de teste para validação");
        // Define uma data/hora específica para testes
        ocorrencia.setDataHora(LocalDateTime.now().minusMinutes(30));
    }
    
    @Test
    void testCalcularTempoDecorridoComUnidade() {
        long tempoHoras = ocorrencia.calcularTempoDecorrido(ChronoUnit.HOURS);
        assertEquals(0, tempoHoras);
        
        long tempoMinutos = ocorrencia.calcularTempoDecorrido(ChronoUnit.MINUTES);
        assertTrue(tempoMinutos >= 29 && tempoMinutos <= 31);
    }
    
    @Test
    void testCalcularTempoDecorrido() {
        long tempoMinutos = ocorrencia.calcularTempoDecorrido();
        assertTrue(tempoMinutos >= 29 && tempoMinutos <= 31);
    }
    
    @Test
    void testIsOcorrenciaCritica() {
        // Deve ser crítica (severidade 7 >= 6 e tempo <= 60 minutos)
        assertTrue(ocorrencia.isOcorrenciaCritica(6, 60));
        
        // Não deve ser crítica (severidade 7 < 8)
        assertFalse(ocorrencia.isOcorrenciaCritica(8, 60));
        
        // Não deve ser crítica (tempo > 15 minutos)
        assertFalse(ocorrencia.isOcorrenciaCritica(6, 15));
    }
    
    @Test
    void testGerarMensagemAlerta() {
        String mensagem = ocorrencia.gerarMensagemAlerta();
        
        assertTrue(mensagem.contains("ALTO"));
        assertTrue(mensagem.contains("Região Sul"));
        assertTrue(mensagem.contains("Severidade: 7/10"));
        assertTrue(mensagem.contains("Teste Unitário"));
    }
    
    @Test
    void testToString() {
        String resultado = ocorrencia.toString();
        
        assertTrue(resultado.contains("Região Sul"));
        assertTrue(resultado.contains("Severidade=7"));
        assertTrue(resultado.contains("Teste Unitário"));
        assertTrue(resultado.contains("Ocorrência de teste para validação"));
    }
    
    @Test
    void testConstrutorEGetters() {
        assertNotNull(ocorrencia.getId());
        assertEquals("Região Sul", ocorrencia.getRegiao());
        assertEquals(7, ocorrencia.getSeveridade());
        assertNotNull(ocorrencia.getDataHora());
        assertEquals("Teste Unitário", ocorrencia.getOrigem());
        assertEquals("Ocorrência de teste para validação", ocorrencia.getDescricao());
    }
} 
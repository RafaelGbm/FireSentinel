package com.global_solution.fire_sentinel_App;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.global_solution.fire_sentinel_App.model.Leitura;
import com.global_solution.fire_sentinel_App.model.Risco;
import com.global_solution.fire_sentinel_App.model.Sensor;

class RiscoTest {
    
    private Risco risco;
    private Sensor sensor;
    private Leitura leitura;
    
    @BeforeEach
    void setUp() {
        sensor = new Sensor();
        sensor.setId(1L);
        sensor.setNome("Sensor Teste");
        
        leitura = new Leitura();
        leitura.setId(1L);
        leitura.setSensor(sensor);
        
        risco = new Risco();
        risco.setId(1L);
        risco.setNivelRisco(0.7);
        risco.setCategoria("ALTO");
        risco.setDescricao("Análise de risco de teste");
        risco.setDataHoraAnalise(LocalDateTime.now().minusMinutes(30));
        risco.setLeitura(leitura);
        risco.setSensor(sensor);
    }
    
    @Test
    void testCalcularTempoDesdeAnaliseComUnidade() {
        long tempoHoras = risco.calcularTempoDesdeAnalise(ChronoUnit.HOURS);
        assertEquals(0, tempoHoras);
        
        long tempoMinutos = risco.calcularTempoDesdeAnalise(ChronoUnit.MINUTES);
        assertTrue(tempoMinutos >= 29 && tempoMinutos <= 31);
    }
    
    @Test
    void testCalcularTempoDesdeAnalise() {
        long tempoMinutos = risco.calcularTempoDesdeAnalise();
        assertTrue(tempoMinutos >= 29 && tempoMinutos <= 31);
    }
    
    @Test
    void testIsAnaliseAtualizada() {
        // Deve estar atualizada (tempo <= 60 minutos)
        assertTrue(risco.isAnaliseAtualizada(60));
        
        // Não deve estar atualizada (tempo > 15 minutos)
        assertFalse(risco.isAnaliseAtualizada(15));
    }
    
    @Test
    void testCalcularVariacaoRisco() {
        Risco riscoAnterior = new Risco();
        riscoAnterior.setNivelRisco(0.5);
        
        double variacao = risco.calcularVariacaoRisco(riscoAnterior);
        assertEquals(40.0, variacao, 0.1); // Aumento de 40% (de 0.5 para 0.7)
        
        // Teste com risco anterior nulo
        assertEquals(0.0, risco.calcularVariacaoRisco(null));
        
        // Teste com risco anterior zero
        riscoAnterior.setNivelRisco(0.0);
        assertEquals(0.0, risco.calcularVariacaoRisco(riscoAnterior));
    }
    
    @Test
    void testGerarRecomendacao() {
        // Teste para diferentes níveis de risco
        risco.setNivelRisco(0.9);
        assertTrue(risco.gerarRecomendacao().contains("EVACUAÇÃO IMEDIATA"));
        
        risco.setNivelRisco(0.7);
        assertTrue(risco.gerarRecomendacao().contains("ALERTA ALTO"));
        
        risco.setNivelRisco(0.5);
        assertTrue(risco.gerarRecomendacao().contains("ATENÇÃO"));
        
        risco.setNivelRisco(0.3);
        assertTrue(risco.gerarRecomendacao().contains("Manter monitoramento regular"));
    }
    
    @Test
    void testToString() {
        String resultado = risco.toString();
        
        // Verifica as informações principais
        assertTrue(resultado.contains("ID=1"));
        assertTrue(resultado.contains("Nível=" + String.format("%.2f", risco.getNivelRisco())));
        assertTrue(resultado.contains("Categoria=ALTO"));
        assertTrue(resultado.contains("Sensor Teste"));
        assertTrue(resultado.contains("Análise de risco de teste"));
        assertTrue(resultado.contains(risco.getDataHoraAnalise().toString()));
    }
} 
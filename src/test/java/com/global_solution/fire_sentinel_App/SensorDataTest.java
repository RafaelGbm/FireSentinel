package com.global_solution.fire_sentinel_App;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalDouble;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.global_solution.fire_sentinel_App.model.Sensor;
import com.global_solution.fire_sentinel_App.model.SensorData;

class SensorDataTest {
    
    private SensorData sensorData;
    private Sensor sensor;
    
    @BeforeEach
    void setUp() {
        sensor = new Sensor();
        sensor.setId(1L);
        sensor.setNome("Sensor Teste");
        
        sensorData = new SensorData();
        sensorData.setId(1L);
        sensorData.setSensor(sensor);
        sensorData.setDataHora(LocalDateTime.now().minusHours(2));
        sensorData.setTemperatura(32.5);
        sensorData.setUmidade(45.0);
        sensorData.setNivelFumaca(80.0);
        sensorData.setCo2(450.0);
    }
    
    @Test
    void testCalcularTempoDesdeColeta() {
        long tempoHoras = sensorData.calcularTempoDesdeColeta(ChronoUnit.HOURS);
        assertEquals(2, tempoHoras, "Deve retornar 2 horas");
        
        long tempoMinutos = sensorData.calcularTempoDesdeColeta(ChronoUnit.MINUTES);
        assertTrue(tempoMinutos >= 119 && tempoMinutos <= 121, 
                "Deve retornar aproximadamente 120 minutos");
    }
    
    @Test
    void testIsSituacaoRisco() {
        // Caso normal - sem risco
        assertFalse(sensorData.isSituacaoRisco(), "Situação normal não deve indicar risco");
        
        // Temperatura alta e umidade baixa
        sensorData.setTemperatura(36.0);
        sensorData.setUmidade(29.0);
        assertTrue(sensorData.isSituacaoRisco(), "Alta temperatura e baixa umidade devem indicar risco");
        
        // Reset para valores normais
        sensorData.setTemperatura(32.5);
        sensorData.setUmidade(45.0);
        
        // Nível de fumaça alto
        sensorData.setNivelFumaca(101.0);
        assertTrue(sensorData.isSituacaoRisco(), "Alto nível de fumaça deve indicar risco");
        
        // Reset nível de fumaça
        sensorData.setNivelFumaca(80.0);
        
        // CO2 alto
        sensorData.setCo2(5001.0);
        assertTrue(sensorData.isSituacaoRisco(), "Alto nível de CO2 deve indicar risco");
    }
    
    @Test
    void testAnalisarVariacoes() {
        LocalDateTime agora = LocalDateTime.now();
        
        SensorData anterior = new SensorData();
        anterior.setId(2L);
        anterior.setSensor(sensor);
        anterior.setDataHora(agora.minusHours(3));
        anterior.setTemperatura(30.0);
        anterior.setUmidade(50.0);
        anterior.setNivelFumaca(60.0);
        anterior.setCo2(400.0);
        
        sensorData.setDataHora(agora.minusHours(2)); // 1 hora depois do anterior
        
        String analise = sensorData.analisarVariacoes(anterior);
        System.out.println("Análise de variações:");
        System.out.println(analise);
        
        assertTrue(analise.contains("Análise de Variações (últimas 1 horas)"), "Deve mostrar 1 hora de diferença");
        assertTrue(analise.contains("32.5°C (+8.3%)"), "Deve mostrar temperatura correta");
        assertTrue(analise.contains("45.0% (-10.0%)"), "Deve mostrar umidade correta");
        assertTrue(analise.contains("80.0 ppm (+33.3%)"), "Deve mostrar nível de fumaça correto");
        assertTrue(analise.contains("450.0 ppm (+12.5%)"), "Deve mostrar CO2 correto");
    }
    
    @Test
    void testAnalisarVariacoesSemAnterior() {
        String analise = sensorData.analisarVariacoes(null);
        assertEquals("Sem dados anteriores para comparação", analise);
    }
    
    @Test
    void testCalcularMediaMedida() {
        List<SensorData> leituras = Arrays.asList(
            criarLeitura(30.0, 60.0, 50.0, 400.0),
            criarLeitura(32.0, 55.0, 70.0, 420.0),
            criarLeitura(31.0, 58.0, 60.0, 410.0)
        );
        
        OptionalDouble mediaTemp = SensorData.calcularMediaMedida(leituras, SensorData::getTemperatura);
        assertTrue(mediaTemp.isPresent());
        assertEquals(31.0, mediaTemp.getAsDouble(), 0.1);
        
        OptionalDouble mediaUmidade = SensorData.calcularMediaMedida(leituras, SensorData::getUmidade);
        assertTrue(mediaUmidade.isPresent());
        assertEquals(57.67, mediaUmidade.getAsDouble(), 0.1);
        
        // Teste com lista vazia
        assertTrue(SensorData.calcularMediaMedida(null, SensorData::getTemperatura).isEmpty());
        assertTrue(SensorData.calcularMediaMedida(Arrays.asList(), SensorData::getTemperatura).isEmpty());
    }
    
    @Test
    void testToString() {
        String resultado = sensorData.toString();
        
        assertTrue(resultado.contains("ID=1"));
        assertTrue(resultado.contains(sensorData.getDataHora().toString()));
        assertTrue(resultado.contains("T=32.5°C"));
        assertTrue(resultado.contains("U=45.0%"));
        assertTrue(resultado.contains("F=80.0 ppm"));
        assertTrue(resultado.contains("CO2=450.0 ppm"));
    }
    
    private SensorData criarLeitura(double temp, double umid, double fumaca, double co2) {
        SensorData leitura = new SensorData();
        leitura.setSensor(sensor);
        leitura.setDataHora(LocalDateTime.now());
        leitura.setTemperatura(temp);
        leitura.setUmidade(umid);
        leitura.setNivelFumaca(fumaca);
        leitura.setCo2(co2);
        return leitura;
    }
} 
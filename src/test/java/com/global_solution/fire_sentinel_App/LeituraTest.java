package com.global_solution.fire_sentinel_App;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.global_solution.fire_sentinel_App.model.Leitura;
import com.global_solution.fire_sentinel_App.model.Sensor;

class LeituraTest {
    
    private Leitura leitura;
    private Sensor sensor;
    
    @BeforeEach
    void setUp() {
        sensor = new Sensor();
        sensor.setId(1L);
        sensor.setNome("Sensor Teste");
        
        leitura = new Leitura();
        leitura.setId(1L);
        leitura.setTemperatura(35.0);
        leitura.setUmidade(45.0);
        leitura.setNivelFumaca(20.0);
        leitura.setCo2(400.0);
        leitura.setDataHora(LocalDateTime.now());
        leitura.setSensor(sensor);
    }
    
    @Test
    void testCalcularIndiceRisco() {
        Double indice = leitura.calcularIndiceRisco();
        
        assertNotNull(indice);
        assertTrue(indice >= 0 && indice <= 1);
    }
    
    @Test
    void testCalcularIndiceRiscoComPesos() {
        Double indice = leitura.calcularIndiceRisco(0.4, 0.3, 0.3);
        
        assertNotNull(indice);
        assertTrue(indice >= 0 && indice <= 1);
    }
    
    @Test
    void testCalcularIndiceRiscoComPesosInvalidos() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            leitura.calcularIndiceRisco(0.5, 0.5, 0.5);
        });
        
        assertEquals("A soma dos pesos deve ser igual a 1", exception.getMessage());
    }
    
    @Test
    void testIsLeituraDentroLimites() {
        assertTrue(leitura.isLeituraDentroLimites(40.0, 30.0, 30.0));
        assertFalse(leitura.isLeituraDentroLimites(30.0, 50.0, 10.0));
    }
    
    @Test
    void testCalcularDiferencaPercentual() {
        Leitura outraLeitura = new Leitura();
        outraLeitura.setTemperatura(38.5);
        outraLeitura.setUmidade(40.0);
        outraLeitura.setNivelFumaca(25.0);
        
        Double diferenca = leitura.calcularDiferencaPercentual(outraLeitura);
        
        assertNotNull(diferenca);
        assertTrue(diferenca > 0);
    }
    
    @Test
    void testToString() {
        String resultado = leitura.toString();
        
        // Verifica se contém as informações principais
        assertTrue(resultado.contains("id=" + leitura.getId()));
        assertTrue(resultado.contains("Sensor Teste"));
        assertTrue(resultado.contains(String.valueOf((int)leitura.getTemperatura().doubleValue()))); // Verifica apenas o número inteiro
        assertTrue(resultado.contains(String.valueOf((int)leitura.getUmidade().doubleValue())));
        assertTrue(resultado.contains(String.valueOf((int)leitura.getNivelFumaca().doubleValue())));
        assertTrue(resultado.contains(String.valueOf((int)leitura.getCo2().doubleValue())));
        assertTrue(resultado.contains(leitura.getDataHora().toString()));
    }
} 
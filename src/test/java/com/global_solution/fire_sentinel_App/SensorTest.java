package com.global_solution.fire_sentinel_App;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.global_solution.fire_sentinel_App.model.Leitura;
import com.global_solution.fire_sentinel_App.model.Sensor;

class SensorTest {
    
    private Sensor sensor;
    private List<Leitura> leituras;
    
    @BeforeEach
    void setUp() {
        sensor = new Sensor();
        sensor.setId(1L);
        sensor.setNome("Sensor Amazônia");
        sensor.setTipo("Temperatura");
        sensor.setLocalizacao("Floresta Amazônica - Setor Norte");
        sensor.setLatitude(-3.4653);
        sensor.setLongitude(-62.2159);
        sensor.setAtivo(true);
        
        // Criar algumas leituras de teste
        leituras = new ArrayList<>();
        
        Leitura leituraRecente = new Leitura();
        leituraRecente.setId(1L);
        leituraRecente.setTemperatura(35.0);
        leituraRecente.setUmidade(70.0);
        leituraRecente.setNivelFumaca(15.0);
        leituraRecente.setCo2(400.0);
        leituraRecente.setDataHora(LocalDateTime.now().minusMinutes(30));
        leituraRecente.setSensor(sensor);
        
        Leitura leituraAntiga = new Leitura();
        leituraAntiga.setId(2L);
        leituraAntiga.setTemperatura(32.0);
        leituraAntiga.setUmidade(75.0);
        leituraAntiga.setNivelFumaca(10.0);
        leituraAntiga.setCo2(380.0);
        leituraAntiga.setDataHora(LocalDateTime.now().minusHours(2));
        leituraAntiga.setSensor(sensor);
        
        leituras.add(leituraRecente);
        leituras.add(leituraAntiga);
        sensor.setLeituras(leituras);
    }
    
    @Test
    void testCalcularDistancia() {
        // Teste com um ponto próximo (aproximadamente 50km de distância)
        double distancia = sensor.calcularDistancia(-3.8853, -62.4159);
        System.out.println("Distância próxima calculada: " + distancia + " km");
        assertTrue(distancia >= 40 && distancia <= 60, 
                "Distância próxima fora do intervalo esperado (40-60km): " + distancia);
        
        // Teste com um ponto distante (aproximadamente 1000km de distância)
        distancia = sensor.calcularDistancia(-11.4653, -60.2159);
        System.out.println("Distância longa calculada: " + distancia + " km");
        assertTrue(distancia >= 900 && distancia <= 1100, 
                "Distância longa fora do intervalo esperado (900-1100km): " + distancia);
    }
    
    @Test
    void testEstaDentroArea() {
        // Deve estar dentro de uma área com raio de 100km
        assertTrue(sensor.estaDentroArea(-3.8853, -62.4159, 100));
        
        // Não deve estar dentro de uma área com raio de 30km
        assertFalse(sensor.estaDentroArea(-3.8853, -62.4159, 30));
    }
    
    @Test
    void testObterUltimaLeitura() {
        Leitura ultimaLeitura = sensor.obterUltimaLeitura();
        
        assertNotNull(ultimaLeitura);
        assertEquals(1L, ultimaLeitura.getId());
        assertTrue(ultimaLeitura.getDataHora().isAfter(LocalDateTime.now().minusHours(1)));
    }
    
    @Test
    void testIsOperacional() {
        // Deve estar operacional (ativo e última leitura < 60 minutos)
        assertTrue(sensor.isOperacional(60));
        
        // Não deve estar operacional (última leitura > 15 minutos)
        assertFalse(sensor.isOperacional(15));
        
        // Não deve estar operacional quando inativo
        sensor.setAtivo(false);
        assertFalse(sensor.isOperacional(60));
        
        // Não deve estar operacional sem leituras
        sensor.setAtivo(true);
        sensor.setLeituras(new ArrayList<>());
        assertFalse(sensor.isOperacional(60));
    }
    
    @Test
    void testGerarRelatorioStatus() {
        String relatorio = sensor.gerarRelatorioStatus();
        System.out.println("Relatório gerado:");
        System.out.println(relatorio);
        
        // Coordenadas exatas como aparecem no relatório
        String coordenadas = String.format(Locale.US, "(-3.465300, -62.215900)");
        
        assertTrue(relatorio.contains("Sensor: Sensor Amazônia (ID: 1)"), "Falha ao verificar nome e ID do sensor");
        assertTrue(relatorio.contains("Tipo: Temperatura"), "Falha ao verificar tipo do sensor");
        assertTrue(relatorio.contains("Localização: Floresta Amazônica - Setor Norte"), "Falha ao verificar localização");
        assertTrue(relatorio.contains(coordenadas), "Falha ao verificar coordenadas");
        assertTrue(relatorio.contains("Estado: ATIVO"), "Falha ao verificar estado");
        assertTrue(relatorio.contains("35.0°C"), "Falha ao verificar temperatura");
        assertTrue(relatorio.contains("70.0%"), "Falha ao verificar umidade");
        assertTrue(relatorio.contains("15.0 ppm"), "Falha ao verificar nível de fumaça");
        assertTrue(relatorio.contains("400.0 ppm"), "Falha ao verificar CO2");
    }
    
    @Test
    void testGerarRelatorioStatusSemLeituras() {
        sensor.setLeituras(new ArrayList<>());
        String relatorio = sensor.gerarRelatorioStatus();
        
        assertTrue(relatorio.contains("Sensor: Sensor Amazônia (ID: 1)"));
        assertTrue(relatorio.contains("Sem leituras registradas"));
    }
    
    @Test
    void testToString() {
        String resultado = sensor.toString();
        
        assertTrue(resultado.contains("ID=1"));
        assertTrue(resultado.contains("Sensor Amazônia"));
        assertTrue(resultado.contains("Tipo=Temperatura"));
        assertTrue(resultado.contains("Local=Floresta Amazônica - Setor Norte"));
        assertTrue(resultado.contains(String.format("%.6f", sensor.getLatitude())));
        assertTrue(resultado.contains(String.format("%.6f", sensor.getLongitude())));
        assertTrue(resultado.contains("ATIVO"));
    }
} 
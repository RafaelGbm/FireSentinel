package com.global_solution.fire_sentinel_App;

import com.global_solution.fire_sentinel_App.dto.LeituraDTO;
import com.global_solution.fire_sentinel_App.dto.RiscoDTO;
import com.global_solution.fire_sentinel_App.model.Leitura;
import com.global_solution.fire_sentinel_App.model.Risco;
import com.global_solution.fire_sentinel_App.model.Sensor;
import com.global_solution.fire_sentinel_App.repository.LeituraRepository;
import com.global_solution.fire_sentinel_App.repository.RiscoRepository;
import com.global_solution.fire_sentinel_App.repository.SensorRepository;
import com.global_solution.fire_sentinel_App.service.impl.RiscoServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RiscoServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private RiscoRepository riscoRepository;

    @Mock
    private LeituraRepository leituraRepository;

    @Mock
    private SensorRepository sensorRepository;

    @InjectMocks
    private RiscoServiceImpl riscoService;

    @Test
    void deveAnalisarRiscoComSucesso() {
        // Configurar a URL da IA usando ReflectionTestUtils
        ReflectionTestUtils.setField(riscoService, "iaRiskAnalysisUrl", "http://mock-ia-api.com/analyze");

        // Criação dos dados de entrada
        LeituraDTO leituraDTO = new LeituraDTO();
        leituraDTO.setId(1L);
        leituraDTO.setSensorId(10L);
        leituraDTO.setTemperatura(36.5);
        leituraDTO.setUmidade(55.2);

        // Criação da Leitura mockada
        Leitura leitura = new Leitura();
        leitura.setId(1L);
        leitura.setTemperatura(36.5);
        leitura.setUmidade(55.2);

        // Criação do Sensor mockado
        Sensor sensor = new Sensor();
        sensor.setId(10L);

        // Criação do RiscoDTO retornado pela IA
        RiscoDTO riscoIA = new RiscoDTO();
        riscoIA.setNivelRisco(0.8);
        riscoIA.setCategoria("ALTO");
        riscoIA.setDescricao("Risco alto de incêndio detectado");
        riscoIA.setDataHoraAnalise(LocalDateTime.now());
        riscoIA.setLeituraId(1L);
        riscoIA.setSensorId(10L);

        // Criação do Risco salvo
        Risco riscoSalvo = new Risco();
        riscoSalvo.setId(100L);
        riscoSalvo.setNivelRisco(0.8);
        riscoSalvo.setCategoria("ALTO");
        riscoSalvo.setDescricao("Risco alto de incêndio detectado");
        riscoSalvo.setDataHoraAnalise(LocalDateTime.now());
        riscoSalvo.setLeitura(leitura);
        riscoSalvo.setSensor(sensor);

        // Configuração dos mocks
        when(leituraRepository.findById(1L)).thenReturn(Optional.of(leitura));
        when(sensorRepository.findById(10L)).thenReturn(Optional.of(sensor));
        // Mock mais genérico para o RestTemplate
        when(restTemplate.postForObject(anyString(), any(), eq(RiscoDTO.class)))
                .thenReturn(riscoIA);
        when(riscoRepository.save(any(Risco.class))).thenReturn(riscoSalvo);

        // Execução do teste
        RiscoDTO resultado = riscoService.analisarRisco(leituraDTO);

        // Verificações
        assertNotNull(resultado);
        assertEquals(100L, resultado.getId());
        assertEquals(0.8, resultado.getNivelRisco());
        assertEquals("ALTO", resultado.getCategoria());
        assertEquals("Risco alto de incêndio detectado", resultado.getDescricao());
        assertNotNull(resultado.getDataHoraAnalise());
        assertEquals(1L, resultado.getLeituraId());
        assertEquals(10L, resultado.getSensorId());
    }

    @Test
    void deveLancarExcecaoQuandoLeituraNaoEncontrada() {
        // Dados de entrada
        LeituraDTO leituraDTO = new LeituraDTO();
        leituraDTO.setId(999L);
        leituraDTO.setSensorId(10L);

        // Mock retornando vazio para leitura
        when(leituraRepository.findById(999L)).thenReturn(Optional.empty());
        when(sensorRepository.findById(10L)).thenReturn(Optional.of(new Sensor()));

        // Verificação da exceção
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> riscoService.analisarRisco(leituraDTO)
        );

        assertEquals("Leitura ou Sensor não encontrado", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoSensorNaoEncontrado() {
        // Dados de entrada
        LeituraDTO leituraDTO = new LeituraDTO();
        leituraDTO.setId(1L);
        leituraDTO.setSensorId(999L);

        // Mock retornando vazio para sensor
        when(leituraRepository.findById(1L)).thenReturn(Optional.of(new Leitura()));
        when(sensorRepository.findById(999L)).thenReturn(Optional.empty());

        // Verificação da exceção
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> riscoService.analisarRisco(leituraDTO)
        );

        assertEquals("Leitura ou Sensor não encontrado", exception.getMessage());
    }

    @Test
    void deveRetornarRiscoFallbackQuandoAPIFalhar() {
        // Configurar a URL da IA
        ReflectionTestUtils.setField(riscoService, "iaRiskAnalysisUrl", "http://mock-ia-api.com/analyze");

        // Criação dos dados de entrada
        LeituraDTO leituraDTO = new LeituraDTO();
        leituraDTO.setId(1L);
        leituraDTO.setSensorId(10L);

        // Criação da Leitura e Sensor mockados
        Leitura leitura = new Leitura();
        leitura.setId(1L);
        Sensor sensor = new Sensor();
        sensor.setId(10L);

        // Criação do Risco salvo com dados de fallback
        Risco riscoSalvo = new Risco();
        riscoSalvo.setId(100L);
        riscoSalvo.setNivelRisco(0.0);
        riscoSalvo.setCategoria("ERRO");
        riscoSalvo.setLeitura(leitura);
        riscoSalvo.setSensor(sensor);

        // Configuração dos mocks
        when(leituraRepository.findById(1L)).thenReturn(Optional.of(leitura));
        when(sensorRepository.findById(10L)).thenReturn(Optional.of(sensor));
        when(restTemplate.postForObject(anyString(), any(), eq(RiscoDTO.class)))
                .thenThrow(new RuntimeException("API indisponível"));
        when(riscoRepository.save(any(Risco.class))).thenReturn(riscoSalvo);

        // Execução do teste
        RiscoDTO resultado = riscoService.analisarRisco(leituraDTO);

        // Verificações do fallback
        assertNotNull(resultado);
        assertEquals(100L, resultado.getId());
        assertEquals(0.0, resultado.getNivelRisco());
        assertEquals("ERRO", resultado.getCategoria());
        assertTrue(resultado.getDescricao().contains("Erro ao processar análise de risco"));
    }
}
package com.global_solution.fire_sentinel_App;

import com.global_solution.fire_sentinel_App.dto.LeituraDTO;
import com.global_solution.fire_sentinel_App.model.Leitura;
import com.global_solution.fire_sentinel_App.model.Sensor;
import com.global_solution.fire_sentinel_App.repository.LeituraRepository;
import com.global_solution.fire_sentinel_App.repository.SensorRepository;
import com.global_solution.fire_sentinel_App.service.RiscoService;

import com.global_solution.fire_sentinel_App.service.impl.LeituraServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LeituraServiceImplTest {

    @Mock
    private LeituraRepository leituraRepository;

    @Mock
    private SensorRepository sensorRepository;

    @Mock
    private RiscoService riscoService;

    @InjectMocks
    private LeituraServiceImpl leituraService;

    private Sensor criarSensor() {
        Sensor sensor = new Sensor();
        sensor.setId(1L);
        sensor.setNome("Sensor Fumaça Norte");
        return sensor;
    }

    private LeituraDTO criarLeituraDTO() {
        LeituraDTO dto = new LeituraDTO();
        dto.setSensorId(1L);
        dto.setTemperatura(35.0);
        dto.setUmidade(40.0);
        dto.setNivelFumaca(0.6);
        dto.setCo2(450.0);
        dto.setDataHora(LocalDateTime.now());
        return dto;
    }

    private Leitura criarLeitura(Sensor sensor) {
        Leitura leitura = new Leitura();
        leitura.setId(100L);
        leitura.setSensor(sensor);
        leitura.setTemperatura(35.0);
        leitura.setUmidade(40.0);
        leitura.setNivelFumaca(0.6);
        leitura.setCo2(450.0);
        leitura.setDataHora(LocalDateTime.now());
        return leitura;
    }

    @Test
    void deveRegistrarLeituraComSucesso() {
        Sensor sensor = criarSensor();
        LeituraDTO dto = criarLeituraDTO();
        Leitura leitura = criarLeitura(sensor);

        when(sensorRepository.findById(1L)).thenReturn(Optional.of(sensor));
        when(leituraRepository.save(any(Leitura.class))).thenReturn(leitura);

        Leitura resultado = leituraService.registrarLeitura(dto);

        assertNotNull(resultado);
        assertEquals(35.0, resultado.getTemperatura());
        assertEquals(0.6, resultado.getNivelFumaca());
        verify(riscoService).analisarRisco(dto);
    }

    @Test
    void deveLancarExcecaoSeSensorNaoExistir() {
        LeituraDTO dto = criarLeituraDTO();
        dto.setSensorId(99L);

        when(sensorRepository.findById(99L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> leituraService.registrarLeitura(dto)
        );

        assertEquals("Sensor não encontrado com ID: 99", exception.getMessage());
        verify(leituraRepository, never()).save(any());
    }

    @Test
    void deveListarTodasAsLeituras() {
        when(leituraRepository.findAll()).thenReturn(List.of(new Leitura(), new Leitura()));

        List<Leitura> leituras = leituraService.listarTodas();

        assertEquals(2, leituras.size());
    }

    @Test
    void deveBuscarLeituraPorId() {
        Leitura leitura = new Leitura();
        leitura.setId(10L);

        when(leituraRepository.findById(10L)).thenReturn(Optional.of(leitura));

        Optional<Leitura> resultado = leituraService.buscarPorId(10L);

        assertTrue(resultado.isPresent());
        assertEquals(10L, resultado.get().getId());
    }

    @Test
    void deveBuscarLeiturasPorSensor() {
        when(leituraRepository.findBySensorIdOrderByDataHoraDesc(1L))
                .thenReturn(List.of(new Leitura(), new Leitura()));

        List<Leitura> resultado = leituraService.buscarPorSensor(1L);

        assertEquals(2, resultado.size());
    }

    @Test
    void deveRetornarUltimaLeituraDeCadaSensor() {
        Sensor sensor1 = criarSensor();
        Sensor sensor2 = new Sensor();
        sensor2.setId(2L);
        sensor2.setNome("Sensor Umidade Sul");

        Leitura leitura1 = criarLeitura(sensor1);
        leitura1.setDataHora(LocalDateTime.now().minusMinutes(10));

        Leitura leitura2 = criarLeitura(sensor1);
        leitura2.setDataHora(LocalDateTime.now());

        Leitura leitura3 = criarLeitura(sensor2);
        leitura3.setDataHora(LocalDateTime.now().minusMinutes(5));

        when(leituraRepository.findAll()).thenReturn(List.of(leitura1, leitura2, leitura3));

        List<Leitura> ultimas = leituraService.obterUltimasLeiturasPorSensor();

        assertEquals(2, ultimas.size());
        assertTrue(ultimas.contains(leitura2)); // última do sensor 1
        assertTrue(ultimas.contains(leitura3)); // única do sensor 2
    }
}

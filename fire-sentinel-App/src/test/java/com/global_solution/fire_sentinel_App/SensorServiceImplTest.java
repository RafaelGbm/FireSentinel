package com.global_solution.fire_sentinel_App;

import com.global_solution.fire_sentinel_App.dto.SensorDTO;
import com.global_solution.fire_sentinel_App.model.Sensor;
import com.global_solution.fire_sentinel_App.repository.SensorRepository;
import com.global_solution.fire_sentinel_App.service.impl.SensorServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SensorServiceImplTest {

    @Mock
    private SensorRepository sensorRepository;

    @InjectMocks
    private SensorServiceImpl sensorService;

    private SensorDTO criarSensorDTO() {
        SensorDTO dto = new SensorDTO();
        dto.setNome("Sensor Umidade Leste");
        dto.setTipo("Umidade");
        dto.setLocalizacao("Região Amazônica - Leste");
        dto.setLatitude(-2.123);
        dto.setLongitude(-60.987);
        dto.setAtivo(true);
        return dto;
    }

    private Sensor criarSensorSalvo() {
        Sensor sensor = new Sensor();
        sensor.setId(1L);
        sensor.setNome("Sensor Umidade Leste");
        sensor.setTipo("Umidade");
        sensor.setLocalizacao("Região Amazônica - Leste");
        sensor.setLatitude(-2.123);
        sensor.setLongitude(-60.987);
        sensor.setAtivo(true);
        return sensor;
    }

    @Test
    void deveCadastrarSensorAmbientalComSucesso() {
        SensorDTO dto = criarSensorDTO();
        Sensor sensorSalvo = criarSensorSalvo();

        when(sensorRepository.save(any(Sensor.class))).thenReturn(sensorSalvo);

        Sensor resultado = sensorService.cadastrarSensor(dto);

        assertNotNull(resultado);
        assertEquals("Sensor Umidade Leste", resultado.getNome());
        assertEquals("Umidade", resultado.getTipo());
        assertEquals("Região Amazônica - Leste", resultado.getLocalizacao());
        verify(sensorRepository, times(1)).save(any(Sensor.class));
    }

    @Test
    void deveListarTodosSensoresAmbientais() {
        Sensor sensor1 = criarSensorSalvo();
        Sensor sensor2 = new Sensor();
        sensor2.setId(2L);
        sensor2.setNome("Sensor Fumaça Norte");
        sensor2.setTipo("Fumaça");
        sensor2.setLocalizacao("Pantanal - Setor Norte");
        sensor2.setLatitude(-16.265);
        sensor2.setLongitude(-56.624);
        sensor2.setAtivo(true);

        when(sensorRepository.findAll()).thenReturn(Arrays.asList(sensor1, sensor2));

        List<Sensor> sensores = sensorService.listarTodos();

        assertEquals(2, sensores.size());
        verify(sensorRepository, times(1)).findAll();
    }

    @Test
    void deveBuscarSensorPorIdAmbiental() {
        Sensor sensor = criarSensorSalvo();

        when(sensorRepository.findById(1L)).thenReturn(Optional.of(sensor));

        Optional<Sensor> resultado = sensorService.buscarPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
    }

    @Test
    void deveRetornarVazioAoBuscarSensorInexistente() {
        when(sensorRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Sensor> resultado = sensorService.buscarPorId(999L);

        assertTrue(resultado.isEmpty());
    }

    @Test
    void deveAtualizarSensorAmbientalComSucesso() {
        Sensor sensorExistente = criarSensorSalvo();

        SensorDTO dto = new SensorDTO();
        dto.setNome("Sensor Fumaça Atualizado");
        dto.setTipo("Fumaça");
        dto.setLocalizacao("Mata Atlântica - Sul");
        dto.setLatitude(-25.0);
        dto.setLongitude(-48.0);
        dto.setAtivo(false);

        when(sensorRepository.findById(1L)).thenReturn(Optional.of(sensorExistente));
        when(sensorRepository.save(any(Sensor.class))).thenReturn(sensorExistente);

        Optional<Sensor> resultado = sensorService.atualizarSensor(1L, dto);

        assertTrue(resultado.isPresent());
        assertEquals("Sensor Fumaça Atualizado", resultado.get().getNome());
        assertEquals("Fumaça", resultado.get().getTipo());
        assertEquals("Mata Atlântica - Sul", resultado.get().getLocalizacao());
        assertFalse(resultado.get().isAtivo());
    }

    @Test
    void naoDeveAtualizarSensorInexistente() {
        when(sensorRepository.findById(123L)).thenReturn(Optional.empty());

        SensorDTO dto = criarSensorDTO();
        Optional<Sensor> resultado = sensorService.atualizarSensor(123L, dto);

        assertTrue(resultado.isEmpty());
        verify(sensorRepository, never()).save(any());
    }

    @Test
    void deveRemoverSensorAmbientalSeExistir() {
        when(sensorRepository.existsById(1L)).thenReturn(true);

        boolean resultado = sensorService.removerSensor(1L);

        assertTrue(resultado);
        verify(sensorRepository).deleteById(1L);
    }

    @Test
    void naoDeveRemoverSensorAmbientalSeNaoExistir() {
        when(sensorRepository.existsById(999L)).thenReturn(false);

        boolean resultado = sensorService.removerSensor(999L);

        assertFalse(resultado);
        verify(sensorRepository, never()).deleteById(anyLong());
    }
}

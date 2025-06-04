package com.global_solution.fire_sentinel_App.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.global_solution.fire_sentinel_App.dto.SensorDTO;
import com.global_solution.fire_sentinel_App.model.Sensor;
import com.global_solution.fire_sentinel_App.repository.SensorRepository;
import com.global_solution.fire_sentinel_App.service.SensorService;

/**
 * Implementação do serviço de gerenciamento de sensores.
 * Responsável por toda a lógica de negócio relacionada aos sensores do sistema.
 * 
 * Esta implementação fornece:
 * - Operações CRUD para sensores
 * - Validações de dados
 * - Conversão entre DTOs e entidades
 * - Integração com o repositório de dados
 */
@Service
public class SensorServiceImpl implements SensorService {
    
    @Autowired
    private SensorRepository sensorRepository;
    
    /**
     * Cadastra um novo sensor no sistema.
     * Converte o DTO recebido em uma entidade Sensor e persiste no banco de dados.
     *
     * @param sensorDTO DTO contendo os dados do novo sensor
     * @return Sensor entidade do sensor cadastrado com ID gerado
     */
    @Override
    public Sensor cadastrarSensor(SensorDTO sensorDTO) {
        Sensor sensor = new Sensor();
        sensor.setNome(sensorDTO.getNome());
        sensor.setTipo(sensorDTO.getTipo());
        sensor.setLocalizacao(sensorDTO.getLocalizacao());
        sensor.setLatitude(sensorDTO.getLatitude());
        sensor.setLongitude(sensorDTO.getLongitude());
        sensor.setAtivo(sensorDTO.isAtivo());
        
        return sensorRepository.save(sensor);
    }
    
    /**
     * Retorna todos os sensores cadastrados no sistema.
     *
     * @return List<Sensor> lista com todos os sensores
     */
    @Override
    public List<Sensor> listarTodos() {
        return sensorRepository.findAll();
    }
    
    /**
     * Busca um sensor específico pelo seu ID.
     *
     * @param id ID do sensor a ser buscado
     * @return Optional<Sensor> contendo o sensor se encontrado
     */
    @Override
    public Optional<Sensor> buscarPorId(Long id) {
        return sensorRepository.findById(id);
    }
    
    /**
     * Atualiza os dados de um sensor existente.
     * Verifica se o sensor existe antes de tentar atualizar.
     *
     * @param id ID do sensor a ser atualizado
     * @param sensorDTO DTO contendo os novos dados do sensor
     * @return Optional<Sensor> contendo o sensor atualizado se encontrado
     */
    @Override
    public Optional<Sensor> atualizarSensor(Long id, SensorDTO sensorDTO) {
        Optional<Sensor> sensorExistente = sensorRepository.findById(id);
        
        if (sensorExistente.isPresent()) {
            Sensor sensor = sensorExistente.get();
            sensor.setNome(sensorDTO.getNome());
            sensor.setTipo(sensorDTO.getTipo());
            sensor.setLocalizacao(sensorDTO.getLocalizacao());
            sensor.setLatitude(sensorDTO.getLatitude());
            sensor.setLongitude(sensorDTO.getLongitude());
            sensor.setAtivo(sensorDTO.isAtivo());
            
            Sensor sensorAtualizado = sensorRepository.save(sensor);
            return Optional.of(sensorAtualizado);
        }
        
        return Optional.empty();
    }
    
    /**
     * Remove um sensor do sistema.
     * Verifica se o sensor existe antes de tentar remover.
     *
     * @param id ID do sensor a ser removido
     * @return boolean true se removido com sucesso, false se não encontrado
     */
    @Override
    public boolean removerSensor(Long id) {
        if (sensorRepository.existsById(id)) {
            sensorRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

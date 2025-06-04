package com.global_solution.fire_sentinel_App;

import com.global_solution.fire_sentinel_App.model.Sensor;
import com.global_solution.fire_sentinel_App.model.SensorData;
import com.global_solution.fire_sentinel_App.model.Ocorrencia;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.OptionalDouble;

public class FireSentinelMain {
    private static Scanner scanner = new Scanner(System.in);
    private static List<Sensor> sensores = new ArrayList<>();
    private static List<SensorData> leituras = new ArrayList<>();
    private static List<Ocorrencia> ocorrencias = new ArrayList<>();
    private static Long nextSensorId = 1L;
    private static Long nextLeituraId = 1L;
    private static Long nextOcorrenciaId = 1L;

    public static void main(String[] args) {
        System.out.println("Bem-vindo ao Sistema Fire Sentinel");
        
        while (true) {
            exibirMenu();
            int opcao = lerOpcao();
            
            switch (opcao) {
                case 1:
                    cadastrarSensor();
                    break;
                case 2:
                    registrarLeitura();
                    break;
                case 3:
                    listarSensores();
                    break;
                case 4:
                    analisarLeituras();
                    break;
                case 5:
                    registrarOcorrencia();
                    break;
                case 6:
                    listarOcorrencias();
                    break;
                case 7:
                    buscarOcorrenciasPorRegiao();
                    break;
                case 8:
                    listarOcorrenciasCriticas();
                    break;
                case 9:
                    System.out.println("Saindo do sistema...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    private static void exibirMenu() {
        System.out.println("\n=== Menu Fire Sentinel ===");
        System.out.println("1. Cadastrar novo sensor");
        System.out.println("2. Registrar leitura");
        System.out.println("3. Listar sensores");
        System.out.println("4. Analisar leituras");
        System.out.println("5. Registrar ocorrência");
        System.out.println("6. Listar ocorrências");
        System.out.println("7. Buscar ocorrências por região");
        System.out.println("8. Listar ocorrências críticas");
        System.out.println("9. Sair");
        System.out.print("Escolha uma opção: ");
    }

    private static int lerOpcao() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private static void cadastrarSensor() {
        System.out.println("\n=== Cadastro de Sensor ===");
        
        System.out.print("Nome do sensor: ");
        String nome = scanner.nextLine();
        
        System.out.print("Tipo do sensor: ");
        String tipo = scanner.nextLine();
        
        System.out.print("Localização: ");
        String localizacao = scanner.nextLine();
        
        System.out.print("Latitude: ");
        double latitude = Double.parseDouble(scanner.nextLine());
        
        System.out.print("Longitude: ");
        double longitude = Double.parseDouble(scanner.nextLine());
        
        Sensor sensor = new Sensor();
        sensor.setId(nextSensorId++);
        sensor.setNome(nome);
        sensor.setTipo(tipo);
        sensor.setLocalizacao(localizacao);
        sensor.setLatitude(latitude);
        sensor.setLongitude(longitude);
        sensor.setAtivo(true);
        
        sensores.add(sensor);
        System.out.println("\nSensor cadastrado com sucesso!");
        System.out.println(sensor);
    }

    private static void registrarLeitura() {
        if (sensores.isEmpty()) {
            System.out.println("\nNão há sensores cadastrados!");
            return;
        }

        System.out.println("\n=== Registro de Leitura ===");
        
        System.out.println("\nSensores disponíveis:");
        for (Sensor sensor : sensores) {
            System.out.println(sensor.getId() + " - " + sensor.getNome());
        }
        
        System.out.print("\nEscolha o ID do sensor: ");
        Long sensorId = Long.parseLong(scanner.nextLine());
        
        Sensor sensorEscolhido = sensores.stream()
                .filter(s -> s.getId().equals(sensorId))
                .findFirst()
                .orElse(null);
                
        if (sensorEscolhido == null) {
            System.out.println("Sensor não encontrado!");
            return;
        }

        try {
            System.out.print("Temperatura (°C): ");
            double temperatura = Double.parseDouble(scanner.nextLine());
            
            System.out.print("Umidade (%): ");
            double umidade = Double.parseDouble(scanner.nextLine());
            
            System.out.print("Nível de Fumaça (ppm): ");
            double nivelFumaca = Double.parseDouble(scanner.nextLine());
            
            System.out.print("CO2 (ppm): ");
            double co2 = Double.parseDouble(scanner.nextLine());
            
            SensorData leitura = new SensorData();
            leitura.setId(nextLeituraId++);
            leitura.setSensor(sensorEscolhido);
            leitura.setDataHora(LocalDateTime.now());
            leitura.setTemperatura(temperatura);
            leitura.setUmidade(umidade);
            leitura.setNivelFumaca(nivelFumaca);
            leitura.setCo2(co2);
            
            leituras.add(leitura);
            
            System.out.println("\nLeitura registrada com sucesso!");
            System.out.println(leitura);
            
            if (leitura.isSituacaoRisco()) {
                System.out.println("\nATENÇÃO: Situação de risco detectada!");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Erro: Valor inválido informado!");
        }
    }

    private static void listarSensores() {
        if (sensores.isEmpty()) {
            System.out.println("\nNão há sensores cadastrados!");
            return;
        }

        System.out.println("\n=== Sensores Cadastrados ===");
        for (Sensor sensor : sensores) {
            System.out.println("\n" + sensor.gerarRelatorioStatus());
        }
    }

    private static void analisarLeituras() {
        if (leituras.isEmpty()) {
            System.out.println("\nNão há leituras registradas!");
            return;
        }

        System.out.println("\n=== Análise de Leituras ===");
        
        // Agrupar leituras por sensor
        for (Sensor sensor : sensores) {
            List<SensorData> leiturasDoSensor = leituras.stream()
                    .filter(l -> l.getSensor().equals(sensor))
                    .toList();
                    
            if (!leiturasDoSensor.isEmpty()) {
                System.out.println("\nSensor: " + sensor.getNome());
                
                // Calcular médias
                OptionalDouble mediaTemp = SensorData.calcularMediaMedida(leiturasDoSensor, SensorData::getTemperatura);
                OptionalDouble mediaUmid = SensorData.calcularMediaMedida(leiturasDoSensor, SensorData::getUmidade);
                OptionalDouble mediaFumaca = SensorData.calcularMediaMedida(leiturasDoSensor, SensorData::getNivelFumaca);
                OptionalDouble mediaCO2 = SensorData.calcularMediaMedida(leiturasDoSensor, SensorData::getCo2);
                
                System.out.printf("Temperatura média: %.1f°C\n", mediaTemp.orElse(0.0));
                System.out.printf("Umidade média: %.1f%%\n", mediaUmid.orElse(0.0));
                System.out.printf("Nível médio de fumaça: %.1f ppm\n", mediaFumaca.orElse(0.0));
                System.out.printf("CO2 médio: %.1f ppm\n", mediaCO2.orElse(0.0));
                
                // Mostrar última leitura
                SensorData ultimaLeitura = leiturasDoSensor.get(leiturasDoSensor.size() - 1);
                if (leiturasDoSensor.size() > 1) {
                    SensorData penultimaLeitura = leiturasDoSensor.get(leiturasDoSensor.size() - 2);
                    System.out.println("\nVariações da última leitura:");
                    System.out.println(ultimaLeitura.analisarVariacoes(penultimaLeitura));
                }
            }
        }
    }

    private static void registrarOcorrencia() {
        System.out.println("\n=== Registro de Ocorrência ===");
        
        System.out.print("Região: ");
        String regiao = scanner.nextLine();
        
        System.out.print("Severidade (1-10): ");
        int severidade = Integer.parseInt(scanner.nextLine());
        
        System.out.print("Origem (Sensor/Manual/Denúncia): ");
        String origem = scanner.nextLine();
        
        System.out.print("Descrição: ");
        String descricao = scanner.nextLine();
        
        Ocorrencia ocorrencia = new Ocorrencia(regiao, severidade);
        ocorrencia.setOrigem(origem);
        ocorrencia.setDescricao(descricao);
        
        ocorrencias.add(ocorrencia);
        System.out.println("\nOcorrência registrada com sucesso!");
        System.out.println(ocorrencia);
    }

    private static void listarOcorrencias() {
        if (ocorrencias.isEmpty()) {
            System.out.println("\nNão há ocorrências registradas!");
            return;
        }

        System.out.println("\n=== Ocorrências Registradas ===");
        for (Ocorrencia ocorrencia : ocorrencias) {
            System.out.println("\n" + ocorrencia);
        }
    }

    private static void buscarOcorrenciasPorRegiao() {
        if (ocorrencias.isEmpty()) {
            System.out.println("\nNão há ocorrências registradas!");
            return;
        }

        System.out.print("\nInforme a região: ");
        String regiao = scanner.nextLine();

        System.out.println("\n=== Ocorrências na Região: " + regiao + " ===");
        boolean encontrou = false;
        for (Ocorrencia ocorrencia : ocorrencias) {
            if (ocorrencia.getRegiao().equalsIgnoreCase(regiao)) {
                System.out.println("\n" + ocorrencia);
                encontrou = true;
            }
        }

        if (!encontrou) {
            System.out.println("Nenhuma ocorrência encontrada para esta região.");
        }
    }

    private static void listarOcorrenciasCriticas() {
        if (ocorrencias.isEmpty()) {
            System.out.println("\nNão há ocorrências registradas!");
            return;
        }

        System.out.println("\n=== Ocorrências Críticas ===");
        boolean encontrou = false;
        LocalDateTime vinteQuatroHorasAtras = LocalDateTime.now().minusHours(24);

        for (Ocorrencia ocorrencia : ocorrencias) {
            if (ocorrencia.getSeveridade() >= 8 && ocorrencia.getDataHora().isAfter(vinteQuatroHorasAtras)) {
                System.out.println("\n" + ocorrencia);
                encontrou = true;
            }
        }

        if (!encontrou) {
            System.out.println("Nenhuma ocorrência crítica nas últimas 24 horas.");
        }
    }
} 
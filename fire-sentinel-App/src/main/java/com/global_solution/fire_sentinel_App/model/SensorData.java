package com.global_solution.fire_sentinel_App.model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;
import java.util.OptionalDouble;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Classe que representa os dados coletados por um sensor em um determinado momento.
 * Esta classe é responsável por armazenar e analisar as medições realizadas,
 * incluindo temperatura, umidade, níveis de fumaça e CO2.
 */
@Entity
@Table(name = "sensor_data")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SensorData {
    
    /**
     * Identificador único do registro de dados.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Sensor que realizou a medição.
     */
    @ManyToOne
    private Sensor sensor;
    
    /**
     * Data e hora da coleta dos dados.
     */
    private LocalDateTime dataHora;
    
    /**
     * Temperatura registrada em graus Celsius.
     */
    private Double temperatura;
    
    /**
     * Umidade relativa do ar em porcentagem.
     */
    private Double umidade;
    
    /**
     * Nível de fumaça detectado em partículas por milhão (ppm).
     */
    private Double nivelFumaca;
    
    /**
     * Nível de CO2 detectado em partículas por milhão (ppm).
     */
    private Double co2;

    /**
     * Calcula o tempo decorrido desde a coleta dos dados.
     * 
     * @param unidade Unidade de tempo para o cálculo (ex: MINUTES, HOURS)
     * @return long representando o tempo decorrido na unidade especificada
     */
    public long calcularTempoDesdeColeta(ChronoUnit unidade) {
        return unidade.between(dataHora, LocalDateTime.now());
    }

    /**
     * Verifica se os dados coletados indicam uma situação de risco.
     * Uma situação é considerada de risco quando:
     * - Temperatura > 35°C e Umidade < 30% (risco de incêndio)
     * - Nível de fumaça > 100 ppm (possível incêndio em curso)
     * - CO2 > 5000 ppm (nível perigoso para saúde)
     * 
     * @return boolean indicando se há situação de risco
     */
    public boolean isSituacaoRisco() {
        return (temperatura > 35 && umidade < 30) ||
               nivelFumaca > 100 ||
               co2 > 5000;
    }

    /**
     * Calcula a variação percentual de uma medida em relação a uma medida anterior.
     * 
     * @param valorAtual valor atual da medida
     * @param valorAnterior valor anterior da medida
     * @return double representando a variação percentual ou 0 se valorAnterior for nulo ou zero
     */
    private double calcularVariacaoPercentual(Double valorAtual, Double valorAnterior) {
        if (valorAnterior == null || valorAnterior == 0) {
            return 0.0;
        }
        return ((valorAtual - valorAnterior) / valorAnterior) * 100;
    }

    /**
     * Analisa as variações nas medidas em relação a uma leitura anterior.
     * 
     * @param anterior SensorData anterior para comparação
     * @return String contendo um relatório das variações
     */
    public String analisarVariacoes(SensorData anterior) {
        if (anterior == null) {
            return "Sem dados anteriores para comparação";
        }

        StringBuilder analise = new StringBuilder();
        long horasDecorridas = Math.abs(ChronoUnit.HOURS.between(anterior.getDataHora(), this.dataHora));
        
        // Ajusta para mostrar pelo menos 1 hora se a diferença for menor
        if (horasDecorridas == 0 && !anterior.getDataHora().equals(this.dataHora)) {
            horasDecorridas = 1;
        }
        
        analise.append(String.format(Locale.US, "Análise de Variações (últimas %d horas):\n",
                horasDecorridas));
        
        analise.append(String.format(Locale.US, "Temperatura: %.1f°C (%+.1f%%)\n",
                temperatura,
                calcularVariacaoPercentual(temperatura, anterior.getTemperatura())));
        
        analise.append(String.format(Locale.US, "Umidade: %.1f%% (%+.1f%%)\n",
                umidade,
                calcularVariacaoPercentual(umidade, anterior.getUmidade())));
        
        analise.append(String.format(Locale.US, "Nível de Fumaça: %.1f ppm (%+.1f%%)\n",
                nivelFumaca,
                calcularVariacaoPercentual(nivelFumaca, anterior.getNivelFumaca())));
        
        analise.append(String.format(Locale.US, "CO2: %.1f ppm (%+.1f%%)",
                co2,
                calcularVariacaoPercentual(co2, anterior.getCo2())));
        
        return analise.toString();
    }

    /**
     * Calcula a média de uma medida específica em uma lista de leituras.
     * 
     * @param leituras Lista de leituras para análise
     * @param medida Função que retorna o valor da medida desejada
     * @return OptionalDouble contendo a média ou vazio se não houver dados
     */
    public static OptionalDouble calcularMediaMedida(List<SensorData> leituras, 
            java.util.function.Function<SensorData, Double> medida) {
        if (leituras == null || leituras.isEmpty()) {
            return OptionalDouble.empty();
        }
        
        return leituras.stream()
                .map(medida)
                .filter(valor -> valor != null)
                .mapToDouble(Double::doubleValue)
                .average();
    }

    /**
     * Gera um relatório resumido dos dados coletados.
     * 
     * @return String contendo o relatório dos dados
     */
    @Override
    public String toString() {
        return String.format(Locale.US, "SensorData [ID=%d] %s | T=%.1f°C | U=%.1f%% | F=%.1f ppm | CO2=%.1f ppm",
                id,
                dataHora,
                temperatura,
                umidade,
                nivelFumaca,
                co2);
    }
}

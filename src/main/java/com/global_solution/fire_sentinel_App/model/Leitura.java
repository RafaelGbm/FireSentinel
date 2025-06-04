package com.global_solution.fire_sentinel_App.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Classe que representa uma leitura de dados ambientais coletados por um sensor.
 * Esta classe é responsável por armazenar informações como temperatura, umidade,
 * nível de fumaça e CO2, além da data e hora da leitura e o sensor que a realizou.
 */
@Entity
@Table(name = "leituras")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Leitura {
    
    /**
     * Identificador único da leitura.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Temperatura registrada em graus Celsius.
     */
    private Double temperatura;
    
    /**
     * Umidade relativa do ar em porcentagem.
     */
    private Double umidade;
    
    /**
     * Nível de fumaça detectado (em ppm - partes por milhão).
     */
    private Double nivelFumaca;
    
    /**
     * Nível de CO2 detectado (em ppm - partes por milhão).
     */
    private Double co2;
    
    /**
     * Data e hora em que a leitura foi realizada.
     */
    private LocalDateTime dataHora;
    
    /**
     * Sensor responsável pela coleta dos dados.
     */
    @ManyToOne
    @JoinColumn(name = "sensor_id")
    private Sensor sensor;

    /**
     * Calcula o índice de risco de incêndio com base nos parâmetros ambientais.
     * Este método considera a temperatura, umidade e nível de fumaça para determinar
     * o risco em uma escala de 0 a 1.
     *
     * @return Double valor entre 0 e 1, onde 0 representa risco mínimo e 1 risco máximo
     */
    public Double calcularIndiceRisco() {
        double pesoTemperatura = 0.3;
        double pesoUmidade = 0.3;
        double pesoFumaca = 0.4;
        
        // Normalização dos valores
        double tempNormalizada = Math.min(Math.max((temperatura - 20) / 30, 0), 1);
        double umidadeNormalizada = 1 - Math.min(Math.max(umidade / 100, 0), 1);
        double fumacaNormalizada = Math.min(Math.max(nivelFumaca / 100, 0), 1);
        
        return (tempNormalizada * pesoTemperatura) + 
               (umidadeNormalizada * pesoUmidade) + 
               (fumacaNormalizada * pesoFumaca);
    }

    /**
     * Calcula o índice de risco de incêndio considerando pesos personalizados para cada parâmetro.
     * Sobrecarga do método calcularIndiceRisco que permite ajustar a importância de cada variável.
     *
     * @param pesoTemperatura peso da temperatura no cálculo (entre 0 e 1)
     * @param pesoUmidade peso da umidade no cálculo (entre 0 e 1)
     * @param pesoFumaca peso do nível de fumaça no cálculo (entre 0 e 1)
     * @return Double valor entre 0 e 1, onde 0 representa risco mínimo e 1 risco máximo
     * @throws IllegalArgumentException se a soma dos pesos não for igual a 1
     */
    public Double calcularIndiceRisco(double pesoTemperatura, double pesoUmidade, double pesoFumaca) {
        if (Math.abs((pesoTemperatura + pesoUmidade + pesoFumaca) - 1.0) > 0.001) {
            throw new IllegalArgumentException("A soma dos pesos deve ser igual a 1");
        }
        
        // Normalização dos valores
        double tempNormalizada = Math.min(Math.max((temperatura - 20) / 30, 0), 1);
        double umidadeNormalizada = 1 - Math.min(Math.max(umidade / 100, 0), 1);
        double fumacaNormalizada = Math.min(Math.max(nivelFumaca / 100, 0), 1);
        
        return (tempNormalizada * pesoTemperatura) + 
               (umidadeNormalizada * pesoUmidade) + 
               (fumacaNormalizada * pesoFumaca);
    }

    /**
     * Verifica se os níveis de leitura estão dentro dos limites seguros.
     * Este método analisa cada parâmetro e retorna true se todos estiverem
     * dentro dos limites considerados seguros.
     *
     * @param limiteTemperatura temperatura máxima considerada segura
     * @param limiteUmidadeMin umidade mínima considerada segura
     * @param limiteFumaca nível máximo de fumaça considerado seguro
     * @return boolean true se todos os parâmetros estiverem dentro dos limites seguros
     */
    public boolean isLeituraDentroLimites(double limiteTemperatura, double limiteUmidadeMin, double limiteFumaca) {
        return temperatura <= limiteTemperatura &&
               umidade >= limiteUmidadeMin &&
               nivelFumaca <= limiteFumaca;
    }

    /**
     * Compara esta leitura com outra e retorna a diferença percentual média entre os parâmetros.
     * Este método é útil para detectar variações significativas entre leituras consecutivas.
     *
     * @param outraLeitura leitura a ser comparada
     * @return Double diferença percentual média entre as leituras
     */
    public Double calcularDiferencaPercentual(Leitura outraLeitura) {
        double difTemperatura = Math.abs((temperatura - outraLeitura.getTemperatura()) / temperatura) * 100;
        double difUmidade = Math.abs((umidade - outraLeitura.getUmidade()) / umidade) * 100;
        double difFumaca = Math.abs((nivelFumaca - outraLeitura.getNivelFumaca()) / nivelFumaca) * 100;
        
        return (difTemperatura + difUmidade + difFumaca) / 3;
    }

    /**
     * Sobrescreve o método toString para fornecer uma representação textual formatada da leitura.
     *
     * @return String representação formatada da leitura
     */
    @Override
    public String toString() {
        return String.format(
            "Leitura [id=%d, sensor=%s, temperatura=%.1f°C, umidade=%.1f%%, fumaça=%.1fppm, CO2=%.1fppm, data=%s]",
            id,
            sensor != null ? sensor.getNome() : "N/A",
            temperatura,
            umidade,
            nivelFumaca,
            co2,
            dataHora
        );
    }
}

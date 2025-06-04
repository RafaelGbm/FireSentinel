package com.global_solution.fire_sentinel_App.model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

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
 * Classe que representa uma análise de risco de incêndio.
 * Esta classe é responsável por armazenar informações sobre o nível de risco
 * calculado a partir dos dados coletados pelos sensores, incluindo sua categorização
 * e descrição detalhada.
 */
@Entity
@Table(name = "riscos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Risco {
    
    /**
     * Identificador único do risco.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Nível de risco calculado (entre 0 e 1).
     * Onde 0 representa risco mínimo e 1 representa risco máximo.
     */
    private Double nivelRisco;
    
    /**
     * Categoria do risco (BAIXO, MÉDIO, ALTO, CRÍTICO).
     */
    private String categoria;
    
    /**
     * Descrição detalhada da análise de risco.
     */
    private String descricao;
    
    /**
     * Data e hora em que a análise foi realizada.
     */
    private LocalDateTime dataHoraAnalise;
    
    /**
     * Leitura que originou esta análise de risco.
     */
    @ManyToOne
    @JoinColumn(name = "leitura_id")
    private Leitura leitura;
    
    /**
     * Sensor responsável pela leitura que gerou esta análise.
     */
    @ManyToOne
    @JoinColumn(name = "sensor_id")
    private Sensor sensor;

    /**
     * Calcula o tempo decorrido desde a análise de risco.
     * 
     * @param unidade Unidade de tempo para o cálculo (ex: ChronoUnit.MINUTES)
     * @return long representando o tempo decorrido na unidade especificada
     */
    public long calcularTempoDesdeAnalise(ChronoUnit unidade) {
        return unidade.between(dataHoraAnalise, LocalDateTime.now());
    }

    /**
     * Calcula o tempo decorrido desde a análise em minutos.
     * Sobrecarga do método calcularTempoDesdeAnalise.
     * 
     * @return long representando o tempo decorrido em minutos
     */
    public long calcularTempoDesdeAnalise() {
        return calcularTempoDesdeAnalise(ChronoUnit.MINUTES);
    }

    /**
     * Verifica se a análise está atualizada com base em um limite de tempo.
     * 
     * @param limiteMinutos tempo máximo em minutos para considerar a análise atualizada
     * @return boolean indicando se a análise está atualizada
     */
    public boolean isAnaliseAtualizada(long limiteMinutos) {
        return calcularTempoDesdeAnalise() <= limiteMinutos;
    }

    /**
     * Calcula a variação do risco em relação a uma análise anterior.
     * 
     * @param riscoAnterior análise de risco anterior para comparação
     * @return double representando a variação percentual do nível de risco
     */
    public double calcularVariacaoRisco(Risco riscoAnterior) {
        if (riscoAnterior == null || riscoAnterior.getNivelRisco() == 0) {
            return 0.0;
        }
        return ((nivelRisco - riscoAnterior.getNivelRisco()) / riscoAnterior.getNivelRisco()) * 100;
    }

    /**
     * Gera uma recomendação com base no nível e categoria de risco.
     * 
     * @return String contendo a recomendação apropriada para o nível de risco
     */
    public String gerarRecomendacao() {
        if (nivelRisco >= 0.8) {
            return "EVACUAÇÃO IMEDIATA! Risco crítico de incêndio detectado. Acionar autoridades.";
        } else if (nivelRisco >= 0.6) {
            return "ALERTA ALTO! Monitorar constantemente e preparar plano de evacuação.";
        } else if (nivelRisco >= 0.4) {
            return "ATENÇÃO! Manter vigilância e verificar medidas preventivas.";
        } else {
            return "Manter monitoramento regular. Risco dentro dos limites aceitáveis.";
        }
    }

    /**
     * Sobrescreve o método toString para fornecer uma representação textual formatada do risco.
     *
     * @return String representação formatada do risco
     */
    @Override
    public String toString() {
        return String.format("Risco [ID=%d] Nível=%.2f | Categoria=%s | Data=%s\nSensor=%s\nDescrição: %s",
                id, nivelRisco, categoria, dataHoraAnalise.toString(),
                sensor != null ? sensor.getNome() : "N/A",
                descricao);
    }
}

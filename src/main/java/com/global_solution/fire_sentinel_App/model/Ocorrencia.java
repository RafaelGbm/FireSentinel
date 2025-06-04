package com.global_solution.fire_sentinel_App.model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Classe que representa uma ocorrência de incêndio ou risco de incêndio.
 * Esta classe é responsável por armazenar informações sobre eventos detectados,
 * incluindo sua localização, severidade, origem e descrição detalhada.
 */
@Entity
@Table(name = "ocorrencias")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ocorrencia {
    /**
     * Identificador único da ocorrência (UUID).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Região onde a ocorrência foi detectada.
     */
    private String regiao;
    
    /**
     * Nível de severidade da ocorrência (1-10).
     */
    private int severidade;
    
    /**
     * Data e hora em que a ocorrência foi registrada.
     */
    private LocalDateTime dataHora = LocalDateTime.now();
    
    /**
     * Origem da detecção (ex: "Sensor IoT", "Denúncia", etc.).
     */
    private String origem;
    
    /**
     * Descrição detalhada da ocorrência.
     */
    private String descricao;

    /**
     * Construtor para criar uma nova ocorrência (mantido para compatibilidade).
     *
     * @param regiao Região onde a ocorrência foi detectada
     * @param severidade Nível de severidade da ocorrência (1-10)
     */
    public Ocorrencia(String regiao, int severidade) {
        this.regiao = regiao;
        this.severidade = severidade;
    }

    /**
     * Calcula o tempo decorrido desde o registro da ocorrência.
     * 
     * @param unidade Unidade de tempo para o cálculo (ex: ChronoUnit.MINUTES)
     * @return long representando o tempo decorrido na unidade especificada
     */
    public long calcularTempoDecorrido(ChronoUnit unidade) {
        return unidade.between(dataHora, LocalDateTime.now());
    }

    /**
     * Calcula o tempo decorrido desde o registro da ocorrência em minutos.
     * Sobrecarga do método calcularTempoDecorrido.
     * 
     * @return long representando o tempo decorrido em minutos
     */
    public long calcularTempoDecorrido() {
        return calcularTempoDecorrido(ChronoUnit.MINUTES);
    }

    /**
     * Verifica se a ocorrência é crítica com base em sua severidade e tempo decorrido.
     * Uma ocorrência é considerada crítica se tiver severidade alta e for recente.
     *
     * @param limiteSeveridade Severidade mínima para ser considerada crítica
     * @param limiteTempoMinutos Tempo máximo em minutos para ser considerada recente
     * @return boolean indicando se a ocorrência é crítica
     */
    public boolean isOcorrenciaCritica(int limiteSeveridade, long limiteTempoMinutos) {
        return severidade >= limiteSeveridade && 
               calcularTempoDecorrido() <= limiteTempoMinutos;
    }

    /**
     * Gera uma mensagem de alerta formatada com base na severidade da ocorrência.
     * 
     * @return String contendo a mensagem de alerta formatada
     */
    public String gerarMensagemAlerta() {
        String nivelAlerta;
        if (severidade >= 8) {
            nivelAlerta = "CRÍTICO";
        } else if (severidade >= 6) {
            nivelAlerta = "ALTO";
        } else if (severidade >= 4) {
            nivelAlerta = "MÉDIO";
        } else {
            nivelAlerta = "BAIXO";
        }
        
        return String.format("ALERTA %s: Ocorrência detectada na região %s. Severidade: %d/10. Origem: %s",
                nivelAlerta, regiao, severidade, origem);
    }

    /**
     * Sobrescreve o método toString para fornecer uma representação textual formatada da ocorrência.
     *
     * @return String representação formatada da ocorrência
     */
    @Override
    public String toString() {
        return String.format("Ocorrência [%s]: Região=%s | Severidade=%d | %s\nOrigem: %s\n%s",
                id, regiao, severidade, dataHora.toString(), origem, descricao);
    }
}
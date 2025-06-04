package com.global_solution.fire_sentinel_App.model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

/**
 * Classe que representa uma ocorrência de incêndio ou risco de incêndio.
 * Esta classe é responsável por armazenar informações sobre eventos detectados,
 * incluindo sua localização, severidade, origem e descrição detalhada.
 */
public class Ocorrencia {
    /**
     * Identificador único da ocorrência (UUID).
     */
    private String id;
    
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
    private LocalDateTime dataHora;
    
    /**
     * Origem da detecção (ex: "Sensor IoT", "Denúncia", etc.).
     */
    private String origem;
    
    /**
     * Descrição detalhada da ocorrência.
     */
    private String descricao;

    /**
     * Construtor para criar uma nova ocorrência.
     *
     * @param regiao Região onde a ocorrência foi detectada
     * @param severidade Nível de severidade da ocorrência (1-10)
     */
    public Ocorrencia(String regiao, int severidade) {
        this.id = UUID.randomUUID().toString();
        this.regiao = regiao;
        this.severidade = severidade;
        this.dataHora = LocalDateTime.now();
    }

    /**
     * Obtém o ID da ocorrência.
     * @return String contendo o UUID da ocorrência
     */
    public String getId() {
        return id;
    }

    /**
     * Obtém a região da ocorrência.
     * @return String contendo o nome da região
     */
    public String getRegiao() {
        return regiao;
    }

    /**
     * Obtém o nível de severidade da ocorrência.
     * @return int representando o nível de severidade (1-10)
     */
    public int getSeveridade() {
        return severidade;
    }

    /**
     * Obtém a data e hora da ocorrência.
     * @return LocalDateTime com a data e hora do registro
     */
    public LocalDateTime getDataHora() {
        return dataHora;
    }

    /**
     * Define a data e hora da ocorrência.
     * @param dataHora Nova data e hora para a ocorrência
     */
    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    /**
     * Obtém a origem da ocorrência.
     * @return String contendo a fonte da detecção
     */
    public String getOrigem() {
        return origem;
    }

    /**
     * Define a origem da ocorrência.
     * @param origem Nova origem para a ocorrência
     */
    public void setOrigem(String origem) {
        this.origem = origem;
    }

    /**
     * Obtém a descrição da ocorrência.
     * @return String contendo a descrição detalhada
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * Define a descrição da ocorrência.
     * @param descricao Nova descrição para a ocorrência
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
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

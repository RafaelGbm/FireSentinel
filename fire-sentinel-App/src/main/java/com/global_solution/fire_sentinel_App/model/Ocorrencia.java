package com.global_solution.fire_sentinel_App.model;

import java.time.LocalDateTime;
import java.util.UUID;


public class Ocorrencia {
    private String id;
    private String regiao;
    private int severidade;
    private LocalDateTime dataHora;
    private String origem;
    private String descricao;

    public Ocorrencia(String regiao, int severidade) {
        this.id = UUID.randomUUID().toString();
        this.regiao = regiao;
        this.severidade = severidade;
        this.dataHora = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public String getRegiao() {
        return regiao;
    }

    public int getSeveridade() {
        return severidade;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return String.format("Ocorrência [%s]: Região=%s | Severidade=%d | %s\nOrigem: %s\n%s",
                id, regiao, severidade, dataHora.toString(), origem, descricao);
    }
}

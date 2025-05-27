package com.global_solution.fire_sentinel_App.model;


public class SensorData {
    private double temperatura;
    private double umidade;
    private double nivelFumaca;

    public SensorData(double temperatura, double umidade, double nivelFumaca) {
        this.temperatura = temperatura;
        this.umidade = umidade;
        this.nivelFumaca = nivelFumaca;
    }

    public double getTemperatura() {
        return temperatura;
    }

    public double getUmidade() {
        return umidade;
    }

    public double getNivelFumaca() {
        return nivelFumaca;
    }

    @Override
    public String toString() {
        return String.format("SensorData [temperatura=%.2f, umidade=%.2f, fumaça=%.2f]",
                temperatura, umidade, nivelFumaca);
    }
}

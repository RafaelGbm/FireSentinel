package com.global_solution.fire_sentinel_App.ia;


import com.global_solution.fire_sentinel_App.model.SensorData;

/**
 * Motor de IA simples para previsão de severidade com base em dados de sensores.
 */
public class IAEngine {

    /**
     * Calcula a severidade da ocorrência com base em uma fórmula heurística.
     *
     * @param sensor Dados captados pelo sensor
     * @return Nível de severidade (1 a 10)
     */
    public static int preverSeveridade(SensorData sensor) {
        double score = (sensor.getTemperatura() * 0.4) +
                (sensor.getNivelFumaca() * 100) -
                (sensor.getUmidade() * 0.5);

        if (score > 90) return 10;
        else if (score > 70) return 8;
        else if (score > 50) return 6;
        else if (score > 30) return 4;
        else return 2;
    }
}

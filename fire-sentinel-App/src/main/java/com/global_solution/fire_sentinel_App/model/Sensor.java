package com.global_solution.fire_sentinel_App.model;

import java.util.List;
import java.time.LocalDateTime;
import java.util.Locale;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Classe que representa um sensor de monitoramento ambiental.
 * Esta classe é responsável por armazenar informações sobre os sensores
 * instalados em campo, incluindo sua localização, tipo e estado operacional.
 */
@Entity
@Table(name = "sensores")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sensor {
    
    /**
     * Identificador único do sensor.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Nome descritivo do sensor.
     */
    private String nome;
    
    /**
     * Tipo do sensor (temperatura, umidade, fumaça, etc.).
     */
    private String tipo;
    
    /**
     * Descrição da localização física do sensor.
     */
    private String localizacao;
    
    /**
     * Latitude da localização do sensor.
     */
    private Double latitude;
    
    /**
     * Longitude da localização do sensor.
     */
    private Double longitude;
    
    /**
     * Estado operacional do sensor (ativo/inativo).
     */
    private boolean ativo = true;
    
    /**
     * Lista de leituras realizadas por este sensor.
     */
    @OneToMany(mappedBy = "sensor")
    private List<Leitura> leituras;

    /**
     * Calcula a distância em quilômetros entre este sensor e um ponto específico.
     * Utiliza a fórmula de Haversine para calcular a distância entre dois pontos na Terra.
     *
     * @param latitude Latitude do ponto de referência
     * @param longitude Longitude do ponto de referência
     * @return double representando a distância em quilômetros
     */
    public double calcularDistancia(double latitude, double longitude) {
        final int RAIO_TERRA = 6371; // Raio médio da Terra em quilômetros
        
        double lat1 = Math.toRadians(this.latitude);
        double lat2 = Math.toRadians(latitude);
        double deltaLat = Math.toRadians(latitude - this.latitude);
        double deltaLon = Math.toRadians(longitude - this.longitude);
        
        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
        
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        return RAIO_TERRA * c;
    }

    /**
     * Verifica se o sensor está dentro de uma área circular específica.
     * Sobrecarga do método que utiliza a distância calculada para determinar se está na área.
     *
     * @param latitude Latitude do centro da área
     * @param longitude Longitude do centro da área
     * @param raioKm Raio da área em quilômetros
     * @return boolean indicando se o sensor está dentro da área
     */
    public boolean estaDentroArea(double latitude, double longitude, double raioKm) {
        return calcularDistancia(latitude, longitude) <= raioKm;
    }

    /**
     * Obtém a última leitura registrada pelo sensor.
     * 
     * @return Leitura mais recente ou null se não houver leituras
     */
    public Leitura obterUltimaLeitura() {
        if (leituras == null || leituras.isEmpty()) {
            return null;
        }
        
        return leituras.stream()
                .max((l1, l2) -> l1.getDataHora().compareTo(l2.getDataHora()))
                .orElse(null);
    }

    /**
     * Verifica se o sensor está operacional com base em seu estado e última leitura.
     * Um sensor é considerado operacional se estiver ativo e tiver uma leitura recente.
     *
     * @param limiteMinutos tempo máximo em minutos desde a última leitura
     * @return boolean indicando se o sensor está operacional
     */
    public boolean isOperacional(long limiteMinutos) {
        if (!ativo) {
            return false;
        }
        
        Leitura ultimaLeitura = obterUltimaLeitura();
        if (ultimaLeitura == null) {
            return false;
        }
        
        LocalDateTime limiteHorario = LocalDateTime.now().minusMinutes(limiteMinutos);
        return ultimaLeitura.getDataHora().isAfter(limiteHorario);
    }

    /**
     * Gera um relatório de status do sensor incluindo sua localização e estado operacional.
     * 
     * @return String contendo o relatório de status do sensor
     */
    public String gerarRelatorioStatus() {
        StringBuilder relatorio = new StringBuilder();
        relatorio.append(String.format(Locale.US, "Sensor: %s (ID: %d)\n", nome, id));
        relatorio.append(String.format(Locale.US, "Tipo: %s\n", tipo));
        relatorio.append(String.format(Locale.US, "Localização: %s (%.6f, %.6f)\n", localizacao, latitude, longitude));
        relatorio.append(String.format(Locale.US, "Estado: %s\n", ativo ? "ATIVO" : "INATIVO"));
        
        Leitura ultimaLeitura = obterUltimaLeitura();
        if (ultimaLeitura != null) {
            relatorio.append(String.format(Locale.US, "Última Leitura: %s\n", ultimaLeitura.getDataHora()));
            relatorio.append(String.format(Locale.US, "Temperatura: %.1f°C\n", ultimaLeitura.getTemperatura()));
            relatorio.append(String.format(Locale.US, "Umidade: %.1f%%\n", ultimaLeitura.getUmidade()));
            relatorio.append(String.format(Locale.US, "Nível de Fumaça: %.1f ppm\n", ultimaLeitura.getNivelFumaca()));
            relatorio.append(String.format(Locale.US, "CO2: %.1f ppm", ultimaLeitura.getCo2()));
        } else {
            relatorio.append("Sem leituras registradas");
        }
        
        return relatorio.toString();
    }

    /**
     * Sobrescreve o método toString para fornecer uma representação textual formatada do sensor.
     *
     * @return String representação formatada do sensor
     */
    @Override
    public String toString() {
        return String.format("Sensor [ID=%d] %s | Tipo=%s | Local=%s (%.6f, %.6f) | %s",
                id, nome, tipo, localizacao, latitude, longitude,
                ativo ? "ATIVO" : "INATIVO");
    }
}

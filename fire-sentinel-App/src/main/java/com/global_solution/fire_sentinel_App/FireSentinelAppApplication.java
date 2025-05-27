package com.global_solution.fire_sentinel_App;

import com.global_solution.fire_sentinel_App.model.SensorData;
import com.global_solution.fire_sentinel_App.controller.IoTGatewayController;
import com.global_solution.fire_sentinel_App.model.Ocorrencia;

public class FireSentinelAppApplication {
	public static void main(String[] args) {
		SensorData sensor = new SensorData(45.2, 20.1, 0.75); // dados simulados
		IoTGatewayController gateway = new IoTGatewayController();

		Ocorrencia ocorrencia = gateway.processarLeituraSensor(sensor);

		System.out.println("Ocorrência gerada com IA e IoT:");
		System.out.println(ocorrencia);
	}
}


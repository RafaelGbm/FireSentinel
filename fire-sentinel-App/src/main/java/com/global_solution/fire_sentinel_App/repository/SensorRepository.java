package com.global_solution.fire_sentinel_App.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.global_solution.fire_sentinel_App.model.Sensor;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, Long> {
    
    List<Sensor> findByAtivo(boolean ativo);
    
    @Query(value = "SELECT s FROM Sensor s WHERE " +
           "6371 * acos(cos(radians(:latitude)) * cos(radians(s.latitude)) * " +
           "cos(radians(s.longitude) - radians(:longitude)) + " +
           "sin(radians(:latitude)) * sin(radians(s.latitude))) <= :raio")
    List<Sensor> findSensoresNaArea(
            @Param("latitude") Double latitude, 
            @Param("longitude") Double longitude, 
            @Param("raio") Double raio);
}
